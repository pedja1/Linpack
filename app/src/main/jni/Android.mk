LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := linpack-jni
#LOCAL_SRC_FILES := linpack-jni.c

LOCAL_SRC_FILES := linpack-standard.c

LOCAL_LDFLAGS := -llog

include $(BUILD_SHARED_LIBRARY)
