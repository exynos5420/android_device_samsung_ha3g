# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/telephony.mk)

# Inherit from ha3g device
$(call inherit-product, device/samsung/ha3g/device.mk)

# Inherit some common CM stuff.
$(call inherit-product, vendor/ld/config/common_full_phone.mk)

PRODUCT_NAME := ld_ha3g
PRODUCT_DEVICE := ha3g

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_MODEL=SM-T705 \
    PRODUCT_NAME=ha3g \
    PRODUCT_DEVICE=ha3g \
    TARGET_DEVICE=ha3g
