# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)
# Inherit from ha3g device
$(call inherit-product, device/samsung/ha3g/device.mk)

# Set those variables here to overwrite the inherited values.
PRODUCT_NAME := full_ha3gxx
PRODUCT_DEVICE := ha3gxx
PRODUCT_BRAND := samsung
PRODUCT_MANUFACTURER := samsung
PRODUCT_MODEL := ha3g