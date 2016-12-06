# Inherit device configuration
$(call inherit-product, device/samsung/ha3g/full_ha3g.mk)

# Inherit some common AICP stuff.
$(call inherit-product, vendor/aicp/config/common.mk)
$(call inherit-product, vendor/aicp/config/telephony.mk)


## Device identifier. This must come after all inclusions
PRODUCT_NAME := aicp_ha3g
PRODUCT_DEVICE := ha3g

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_MODEL=SM-N900 \
    PRODUCT_NAME=ha3g \
    PRODUCT_DEVICE=ha3g \
    TARGET_DEVICE=ha3g 