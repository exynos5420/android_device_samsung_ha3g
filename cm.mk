## Specify phone tech before including full_phone
$(call inherit-product, vendor/cm/config/gsm.mk)

# Release name
PRODUCT_RELEASE_NAME := ha3g

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Inherit device configuration
$(call inherit-product, device/samsung/ha3g/device_ha3g.mk)

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := ha3g
PRODUCT_NAME := cm_ha3g
PRODUCT_BRAND := samsung
PRODUCT_MODEL := ha3g
PRODUCT_MANUFACTURER := samsung
