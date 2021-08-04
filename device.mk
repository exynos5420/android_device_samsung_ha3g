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

LOCAL_PATH := device/samsung/ha3g

DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.telephony.gsm.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.telephony.gsm.xml

# GPS
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/gps/gps.xml:$(TARGET_COPY_OUT_VENDOR)/etc/gps.xml

# Ramdisk
PRODUCT_PACKAGES += \
    init.baseband.rc

   
# Radio
PRODUCT_PACKAGES += \
    modemloader \
    libprotobuf-cpp-full \
    libxml2 \
    rild \
    libril \
    libreference-ril \
    android.hardware.radio@1.0 \
    android.hardware.radio@1.1 \
    android.hardware.radio.deprecated@1.0 \
    android.hardware.radio.deprecated@1.1 \
    android.hardware.radio.config@1.1 \
     android.hardware.radio.config@1.0 
   
    
    
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/init/rild.rc:$(TARGET_COPY_OUT_VENDOR)/etc/init/rild.legacy.rc

# Vendor security patch level
PRODUCT_PROPERTY_OVERRIDES += \
    ro.lineage.build.vendor_security_patch=2017-02-01


  # Macloader
PRODUCT_PACKAGES += \
    macloader


# NFC
$(call inherit-product, device/samsung/ha3g/nfc/bcm2079x/product.mk)
# System properties

-include $(LOCAL_PATH)/system_prop.mk

# Inherit from ha3g-common
$(call inherit-product, device/samsung/ha3g-common/device-common.mk)

# call the proprietary setup
$(call inherit-product, vendor/samsung/ha3g/ha3g-vendor.mk)
