#
# Copyright (C) 2013 The CyanogenMod Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := device/samsung/ha3g

# Platform
BOARD_VENDOR := samsung
TARGET_BOARD_PLATFORM := exynos5
TARGET_SLSI_VARIANT := cm
TARGET_SOC := exynos5420

# Architecture
TARGET_ARCH := arm
TARGET_ARCH_VARIANT := armv7-a-neon
TARGET_CPU_VARIANT := cortex-a15
TARGET_CPU_ABI := armeabi-v7a
TARGET_CPU_ABI2 := armeabi
TARGET_CPU_SMP := true

# Vold
TARGET_USE_CUSTOM_LUN_FILE_PATH := /sys/class/android_usb/android0/f_mass_storage/lun%d/file

# Radio
BOARD_PROVIDES_LIBRIL := true
# hardware/samsung/ril
BOARD_MODEM_TYPE := xmm6360
# we need define it (because audio.primary.universal5420.so requires it)
COMMON_GLOBAL_CFLAGS += -DSEC_PRODUCT_FEATURE_RIL_CALL_DUALMODE_CDMAGSM
# RIL.java overwrite
BOARD_RIL_CLASS := ../../../device/samsung/ha3g/ril

# Bluetooth
BOARD_HAVE_BLUETOOTH := true
BOARD_HAVE_BLUETOOTH_BCM := true
BOARD_HAVE_SAMSUNG_BLUETOOTH := true
BOARD_BLUEDROID_VENDOR_CONF := $(LOCAL_PATH)/bluetooth/libbt_vndcfg.txt
BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR := $(LOCAL_PATH)/bluetooth

# Boot animation
TARGET_BOOTANIMATION_PRELOAD := true
TARGET_BOOTANIMATION_TEXTURE_CACHE := true
TARGET_BOOTANIMATION_HALF_RES := true

# Bootloader
TARGET_OTA_ASSERT_DEVICE := ha3g
TARGET_BOOTLOADER_BOARD_NAME := universal5420
TARGET_NO_BOOTLOADER := true
TARGET_NO_RADIOIMAGE := true

# Camera
BOARD_CAMERA_SNUMINTS := 24
BOARD_NEEDS_MEMORYHEAPION := true
COMMON_GLOBAL_CFLAGS += -DCAMERA_SNUMINTS=$(BOARD_CAMERA_SNUMINTS)
# COMMON_GLOBAL_CFLAGS += -DDISABLE_HW_ID_MATCH_CHECK
# COMMON_GLOBAL_CFLAGS += -DSAMSUNG_CAMERA_HARDWARE
COMMON_GLOBAL_CFLAGS += -DSAMSUNG_DVFS

# Force the screenshot path to CPU consumer (fix glitches)
COMMON_GLOBAL_CFLAGS += -DFORCE_SCREENSHOT_CPU_PATH

# Kernel
BOARD_KERNEL_BASE := 0x10000000
BOARD_KERNEL_PAGESIZE := 2048
TARGET_KERNEL_CONFIG := cyanogenmod_deathly_ha3g_defconfig
TARGET_KERNEL_SOURCE := kernel/samsung/exynos5420

# Battery
BOARD_CHARGER_SHOW_PERCENTAGE := true
BOARD_CHARGER_ENABLE_SUSPEND := true
BOARD_CHARGING_MODE_BOOTING_LPM := /sys/class/power_supply/battery/batt_lp_charging
BOARD_BATTERY_DEVICE_NAME := battery
RED_LED_PATH := "/sys/class/leds/led_r/brightness"
GREEN_LED_PATH := "/sys/class/leds/led_g/brightness"
BLUE_LED_PATH := "/sys/class/leds/led_b/brightness"
BACKLIGHT_PATH := "/sys/class/backlight/panel/brightness"
CHARGING_ENABLED_PATH := "/sys/class/power_supply/battery/batt_lp_charging"

# FIMG2D
BOARD_USES_SKIA_FIMGAPI := true

# HDMI
BOARD_USES_GSC_VIDEO := true
BOARD_USES_CEC := true

# Graphics
USE_OPENGL_RENDERER := true
NUM_FRAMEBUFFER_SURFACE_BUFFERS := 5

# HWCServices
BOARD_USES_HWC_SERVICES := true

# Include path
TARGET_SPECIFIC_HEADER_PATH := $(LOCAL_PATH)/include

# CMHW
BOARD_HARDWARE_CLASS += device/samsung/ha3g/cmhw
BOARD_HARDWARE_CLASS += hardware/samsung/cmhw

# SurfaceFlinger
BOARD_USES_SYNC_MODE_FOR_MEDIA := true

# NFC
BOARD_HAVE_NFC := true
BOARD_NFC_HAL_SUFFIX := universal5420

# Media
COMMON_GLOBAL_CFLAGS += -DUSE_NATIVE_SEC_NV12TILED # use format from fw/native
COMMON_GLOBAL_CFLAGS += -DWIDEVINE_PLUGIN_PRE_NOTIFY_ERROR

# OpenMAX Video
BOARD_USE_STOREMETADATA := true
BOARD_USE_METADATABUFFERTYPE := true
BOARD_USE_S3D_SUPPORT := true
BOARD_USE_DMA_BUF := true
BOARD_USE_ANB_OUTBUF_SHARE := true
BOARD_USE_GSC_RGB_ENCODER := true
BOARD_USE_IMPROVED_BUFFER := true
BOARD_USE_CSC_HW := false
BOARD_USE_H264_PREPEND_SPS_PPS := false
BOARD_USE_QOS_CTRL := false
BOARD_USE_VP8ENC_SUPPORT := true
BOARD_USE_ENCODER_RGBINPUT_SUPPORT := true
BOARD_USE_DUALDPB_MODE := true

# Samsung Gralloc
TARGET_SAMSUNG_GRALLOC_EXTERNAL_USECASES := true

# Partitions
BOARD_BOOTIMAGE_PARTITION_SIZE := 11534336
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 13631488
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 2398552064
# userdata = 27912056832 = (27912073216 - 16384 <encryption footer>)
BOARD_USERDATAIMAGE_PARTITION_SIZE := 27912056832
BOARD_CACHEIMAGE_PARTITION_SIZE := 309616640
BOARD_FLASH_BLOCK_SIZE := 131072

# PowerHAL
TARGET_POWERHAL_VARIANT := samsung

# Recovery
TARGET_USERIMAGES_USE_EXT4 := true
BOARD_RECOVERY_SWIPE := true
TARGET_RECOVERY_FSTAB := $(LOCAL_PATH)/rootdir/etc/fstab.universal5420

# Scaler
BOARD_USES_SCALER := true

# SELinux
#BOARD_SEPOLICY_DIRS += \
#    device/samsung/ha3g/sepolicy

# Webkit
ENABLE_WEBGL := true

# WFD
BOARD_USES_WFD := true

# Wifi
BOARD_HAVE_SAMSUNG_WIFI          := true
BOARD_WLAN_DEVICE                := bcmdhd
WPA_SUPPLICANT_VERSION           := VER_0_8_X
BOARD_WPA_SUPPLICANT_DRIVER      := NL80211
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_bcmdhd
BOARD_HOSTAPD_DRIVER             := NL80211
BOARD_HOSTAPD_PRIVATE_LIB        := lib_driver_cmd_bcmdhd
WIFI_BAND                        := 802_11_ABG

WIFI_DRIVER_FW_PATH_PARAM        := "/sys/module/dhd/parameters/firmware_path"
WIFI_DRIVER_FW_PATH_STA          := "/system/etc/wifi/bcmdhd_sta.bin"
WIFI_DRIVER_FW_PATH_AP           := "/system/etc/wifi/bcmdhd_apsta.bin"
WIFI_DRIVER_FW_PATH_P2P          := "/system/etc/wifi/bcmdhd_p2p.bin"

# inherit from the proprietary version
-include vendor/samsung/ha3g/BoardConfigVendor.mk
