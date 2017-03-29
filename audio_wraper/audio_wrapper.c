/*
 * Copyright (c) 2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "YMU831_wrapper"
/* #define LOG_NDEBUG 0 */

#include <errno.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <pthread.h>
#include <fcntl.h>

#include <cutils/log.h>

#include <hardware/hardware.h>
#include <system/audio.h>
#include <hardware/audio.h>

/* Input */
struct wrapper_in_stream {
    struct audio_stream_in *stream_in;
    struct audio_stream_in *copy_stream_in;
};

static struct wrapper_in_stream *in_streams = NULL;
static int n_in_streams = 0;
static pthread_mutex_t in_streams_mutex = PTHREAD_MUTEX_INITIALIZER;

/* HAL */
static struct audio_hw_device *copy_hw_dev = NULL;
static void *dso_handle = NULL;
static int in_use = 0;
static pthread_mutex_t in_use_mutex = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t in_use_cond = PTHREAD_COND_INITIALIZER;

#define WAIT_FOR_FREE() do { pthread_mutex_lock(&in_use_mutex); \
                             while (in_use) { \
                                 pthread_cond_wait(&in_use_cond, &in_use_mutex); \
                             } } while(0)

#define UNLOCK_FREE() do { pthread_cond_signal(&in_use_cond); \
                           pthread_mutex_unlock(&in_use_mutex); } while (0)

+static int in_get_capture_position(const struct audio_stream_in *stream,
 +                                   int64_t *frames, int64_t *time){
	return 0;
}

/* Input stream */
static void wrapper_close_input_stream(struct audio_hw_device *dev,
                                       struct audio_stream_in *stream_in)
{
    int i;

    pthread_mutex_lock(&in_streams_mutex);
    for (i = 0; i < n_in_streams; i++) {
        if (in_streams[i].stream_in == stream_in) {
            free(in_streams[i].copy_stream_in);
            n_in_streams--;
            memmove(in_streams + i,
                    in_streams + i + 1,
                    sizeof(struct wrapper_in_stream) * (n_in_streams - i));
            in_streams = realloc(in_streams,
                                  sizeof(struct wrapper_in_stream) * n_in_streams);
            ALOGI("Closed wrapped input stream");
            break;
        }
    }
    WAIT_FOR_FREE();
    copy_hw_dev->close_input_stream(dev, stream_in);
    UNLOCK_FREE();

    pthread_mutex_unlock(&in_streams_mutex);
}

static int wrapper_open_input_stream(struct audio_hw_device *dev,
                                     audio_io_handle_t handle,
                                     audio_devices_t devices,
                                     struct audio_config *config,
                                     struct audio_stream_in **stream_in)
{
    int ret;

    pthread_mutex_lock(&in_streams_mutex);

    WAIT_FOR_FREE();
    ret = copy_hw_dev->open_input_stream(dev, handle, devices,
                                         config, stream_in);
    UNLOCK_FREE();

    if (ret == 0) {
        struct wrapper_in_stream *new_in_streams;

        new_in_streams = realloc(in_streams,
                              sizeof(struct wrapper_in_stream) * (n_in_streams + 1));
        if (!new_in_streams) {
            ALOGE("Can't allocate memory for wrapped stream, not touching original!");
            pthread_mutex_unlock(&in_streams_mutex);
            return ret;
        }
        in_streams = new_in_streams;
        memset(&in_streams[n_in_streams], 0, sizeof(struct wrapper_in_stream));

        in_streams[n_in_streams].stream_in = *stream_in;
        in_streams[n_in_streams].copy_stream_in = malloc(sizeof(struct audio_stream_in));
        if (!in_streams[n_in_streams].copy_stream_in) {
            ALOGE("Can't allocate memory for copy_stream_in!");
            pthread_mutex_unlock(&in_streams_mutex);
            return ret;
        }
        memcpy(in_streams[n_in_streams].copy_stream_in, *stream_in,
               sizeof(struct audio_stream_in));

        (*stream_in)->get_capture_position = in_get_capture_position;

        ALOGI("Wrapped an input stream: rate %d, channel_mask: %x, format: %d",
              config->sample_rate, config->channel_mask, config->format);

        n_in_streams++;
    }
    pthread_mutex_unlock(&in_streams_mutex);

    return ret;
}
/* Generic HAL */

static int wrapper_close(hw_device_t *device)
{
    int ret;

    pthread_mutex_lock(&in_streams_mutex);

    WAIT_FOR_FREE();

    ret = copy_hw_dev->common.close(device);

    dlclose(dso_handle);
    dso_handle = NULL;
    free(copy_hw_dev);
    copy_hw_dev = NULL;

    if (in_streams) {
        free(in_streams);
        in_streams = NULL;
        n_in_streams = 0;
    }

    UNLOCK_FREE();
    pthread_mutex_unlock(&in_streams_mutex);

    return ret;
}

static int wrapper_open(const hw_module_t* module,
                             const char* name,
                             hw_device_t** device)
{
    struct hw_module_t *hmi;
    struct audio_hw_device *adev;
    int ret;

    ALOGI("Initializing wrapper for Yamahas's YMU831 audio-HAL");
    if (copy_hw_dev) {
        ALOGE("Audio HAL already opened!");
        return -ENODEV;
    }

    dso_handle = dlopen("/system/lib/hw/audio.primary.universal5420.so", RTLD_NOW);
    if (dso_handle == NULL) {
        char const *err_str = dlerror();
        ALOGE("wrapper_open: %s", err_str ? err_str : "unknown");
        return -EINVAL;
    }

    const char *sym = HAL_MODULE_INFO_SYM_AS_STR;
    hmi = (struct hw_module_t *)dlsym(dso_handle, sym);
    if (hmi == NULL) {
        ALOGE("wrapper_open: couldn't find symbol %s", sym);
        dlclose(dso_handle);
        dso_handle = NULL;
        return -EINVAL;
    }

    hmi->dso = dso_handle;

    ret = audio_hw_device_open(hmi, &adev);
    ALOGE_IF(ret, "%s couldn't open audio module in %s. (%s)", __func__,
                 AUDIO_HARDWARE_MODULE_ID, strerror(-ret));
    if (ret) {
        dlclose(dso_handle);
        dso_handle = NULL;
        return ret;
    }

    *device = (hw_device_t*)adev;

    copy_hw_dev = malloc(sizeof(struct audio_hw_device));
    if (!copy_hw_dev) {
        ALOGE("Can't allocate memory for copy_hw_dev, continuing unwrapped...");
        return 0;
    }

    memcpy(copy_hw_dev, *device, sizeof(struct audio_hw_device));

    /* HAL */
    adev->common.close = wrapper_close;

    /* Input */
    adev->open_input_stream = wrapper_open_input_stream;
    adev->close_input_stream = wrapper_close_input_stream;

    return 0;
}

static struct hw_module_methods_t wrapper_module_methods = {
    .open = wrapper_open,
};

struct audio_module HAL_MODULE_INFO_SYM = {
    .common = {
        .tag = HARDWARE_MODULE_TAG,
        .version_major = 1,
        .version_minor = 0,
        .id = AUDIO_HARDWARE_MODULE_ID,
        .name = "Yamaha YMU831 AUDIO-HAL wrapper",
        .author = "The LineageOS Project (Martin Bouchet)",
        .methods = &wrapper_module_methods,
    },
};