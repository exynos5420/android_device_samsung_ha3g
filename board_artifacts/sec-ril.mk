# Radio
PRODUCT_PACKAGES += \
    libril \
    librilutils \
    rild \
    libxml2 \
    libprotobuf-cpp-full \
    modemloader

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml

PRODUCT_PROPERTY_OVERRIDES += \
    ro.carrier=unknown \
    rild.libpath=/system/lib/libsec-ril.so \
    rild.libargs=-d /dev/ttyS0 \
    ro.telephony.ril_class=SlteRIL \
    ro.ril.telephony.mqanelements=5 \
    ro.telephony.default_network=0 \
    telephony.lteOnCdmaDevice=0