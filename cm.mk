# Inherit device configuration
$(call inherit-product, device/samsung/ha3g/full_ha3g.mk)

# Inherit some common LineageOS stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

## Device identifier. This must come after all inclusions
PRODUCT_NAME := lineage_ha3g
PRODUCT_DEVICE := ha3g

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_MODEL=SM-N900 \
    PRODUCT_NAME=ha3g \
    PRODUCT_DEVICE=ha3g \
    TARGET_DEVICE=ha3g \
