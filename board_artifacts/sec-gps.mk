# GPS
PRODUCT_PACKAGES += \
    libdmitry

# GPS
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/gps/gps.cer:system/etc/gps.cer \
    $(LOCAL_PATH)/configs/gps/gps.conf:system/etc/gps.conf \
    $(LOCAL_PATH)/configs/gps/gps.xml:system/etc/gps.xml
