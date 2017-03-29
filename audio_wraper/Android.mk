include $(CLEAR_VARS)

LOCAL_MODULE := audio.primary.$(TARGET_DEVICE)

LOCAL_MODULE_PATH := $(TARGET_OUT_SHARED_LIBRARIES)/hw
LOCAL_SRC_FILES := audio_wrapper.c

LOCAL_C_INCLUDES += \
    system/media/audio_utils/include \
    system/media/audio_effects/include
LOCAL_SHARED_LIBRARIES := liblog libcutils libaudioutils libdl
LOCAL_MODULE_TAGS := optional

include $(BUILD_SHARED_LIBRARY)