# Inherit device configuration
$(call inherit-product, device/samsung/ha3g/full_ha3g.mk)

# Enhanced NFC
$(call inherit-product, vendor/cm/config/nfc_enhanced.mk)

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

## Device identifier. This must come after all inclusions
PRODUCT_NAME := cm_ha3g
PRODUCT_DEVICE := ha3g

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_MODEL=SM-N900 \
    PRODUCT_NAME=ha3g \
    PRODUCT_DEVICE=ha3g \
    TARGET_DEVICE=ha3g \
