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

DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay

# Device uses high-density artwork where available
PRODUCT_AAPT_CONFIG := normal

# Boot animation
TARGET_SCREEN_HEIGHT := 1920
TARGET_SCREEN_WIDTH := 1080

# Display
PRODUCT_PACKAGES += \
    SamsungServiceMode

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/handheld_core_hardware.xml:system/etc/permissions/handheld_core_hardware.xml \
    frameworks/native/data/etc/android.software.sip.voip.xml:system/etc/permissions/android.software.sip.voip.xml \
    frameworks/native/data/etc/android.software.sip.xml:system/etc/permissions/android.software.sip.xml 

# call dalvik heap and hwui config
$(call inherit-product, frameworks/native/build/phone-xxhdpi-3072-dalvik-heap.mk)

$(call inherit-product-if-exists, frameworks/native/build/phone-xxhdpi-3072-hwui-memory.mk)

# call the proprietary setup
$(call inherit-product-if-exists, vendor/samsung/ha3g/ha3g-vendor.mk)

# Inherit board specific artifacts
include $(LOCAL_PATH)/board_artifacts/*.mk

# Import the common tree changes
include device/samsung/exynos5420-common/exynos5420.mk
