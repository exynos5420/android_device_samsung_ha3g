# Radio
BOARD_PROVIDES_LIBRIL := true
# hardware/samsung/ril
BOARD_MODEM_TYPE := xmm6360
# we need define it (because audio.primary.universal5420.so requires it)
BOARD_GLOBAL_CFLAGS += -DSEC_PRODUCT_FEATURE_RIL_CALL_DUALMODE_CDMAGSM
# RIL.java overwrite
BOARD_RIL_CLASS := ../../../device/samsung/ha3g/ril