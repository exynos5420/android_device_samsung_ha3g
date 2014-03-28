## Specify phone tech before including full_phone
$(call inherit-product, vendor/cm/config/gsm.mk)

# Release name
PRODUCT_RELEASE_NAME := ha3g

# Enhanced NFC
$(call inherit-product, vendor/cm/config/nfc_enhanced.mk)

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Inherit device configuration
$(call inherit-product, device/samsung/ha3g/full_ha3g.mk)

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := ha3g
PRODUCT_NAME := cm_ha3g
PRODUCT_BRAND := samsung
PRODUCT_MODEL := SM-N900
PRODUCT_MANUFACTURER := samsung
