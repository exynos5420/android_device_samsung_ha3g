#
# Copyright (C) 2018 The LineageOS Project
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

# Inherit from ha3g-common
include device/samsung/ha3g-common/BoardConfigCommon.mk

LOCAL_PATH := device/samsung/ha3g

# Include path
TARGET_SPECIFIC_HEADER_PATH += $(LOCAL_PATH)/include

# Assert
TARGET_OTA_ASSERT_DEVICE := ha3g,N900 

# HIDL
DEVICE_MANIFEST_FILE += $(LOCAL_PATH)/manifest.xml

# Kernel
TARGET_KERNEL_CONFIG := lineageos_ha3g_defconfig

# Legacy BLOB Support
TARGET_PROCESS_SDK_VERSION_OVERRIDE += \
    /system/vendor/bin/hw/rild=27

# Network Routing
TARGET_NEEDS_NETD_DIRECT_CONNECT_RULE := true

# Battery
RED_LED_PATH := "/sys/class/leds/led_r/brightness" 
GREEN_LED_PATH := "/sys/class/leds/led_g/brightness" 
BLUE_LED_PATH := "/sys/class/leds/led_b/brightness"
BACKLIGHT_PATH := "/sys/class/backlight/panel/brightness"


# Partitions
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 2398552064
BOARD_CACHEIMAGE_FILE_SYSTEM_TYPE := ext4
BOARD_BOOTIMAGE_PARTITION_SIZE := 11534336
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 13631488
# userdata = 27912056832 = (27912073216 - 16384 <encryption  footer>)
BOARD_USERDATAIMAGE_PARTITION_SIZE := 27912056832
BOARD_CACHEIMAGE_PARTITION_SIZE := 309616640
BOARD_FLASH_BLOCK_SIZE := 131072

# Modem
BOARD_PROVIDES_LIBRIL := true
BOARD_MODEM_TYPE := xmm6360

# Inherit from the proprietary versio
-include vendor/samsung/ha3g/BoardConfigVendor.mk

# NFC
BOARD_HAVE_NFC := true 
BOARD_NFC_HAL_SUFFIX := universal5420
 -include device/samsung/ha3g/nfc/bcm2079x/board.mk
