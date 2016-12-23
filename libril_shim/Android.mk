LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
	libril_shim.cpp

LOCAL_SHARED_LIBRARIES := \
	liblog \
	libutils \
	libril \
	libdl \
	libbinder

LOCAL_C_INCLUDES += $(TARGET_OUT_HEADERS)/libril

LOCAL_MODULE := libshim_ril
LOCAL_MODULE_TAGS := optional
include $(BUILD_SHARED_LIBRARY)