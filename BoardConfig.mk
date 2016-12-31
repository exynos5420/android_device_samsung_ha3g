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

# DT2W
TARGET_DT2W_PATH := /sys/class/input/input1/wake_gesture

# Include path
TARGET_SPECIFIC_HEADER_PATH := $(LOCAL_PATH)/include

# CMHW
BOARD_HARDWARE_CLASS += $(LOCAL_PATH)/cmhw

# SELinux
BOARD_SEPOLICY_DIRS += device/samsung/exynos5420-common/sepolicy
BOARD_SEPOLICY_DIRS += $(LOCAL_PATH)/sepolicy

# Inherit board specific defines
include $(LOCAL_PATH)/board_defines/*.mk

# Inherit from exynos5420-common
include device/samsung/exynos5420-common/BoardConfigCommon.mk