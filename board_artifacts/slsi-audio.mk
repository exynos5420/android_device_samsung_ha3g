# Audio
PRODUCT_PACKAGES += \
    libtinyxml \
    libtinyalsa

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/audio/mixer_paths.xml:system/etc/mixer_paths.xml \
    $(LOCAL_PATH)/configs/audio/ysound.xml:system/etc/ysound.xml

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/com.nxp.mifare.xml:system/etc/permissions/com.nxp.mifare.xml
