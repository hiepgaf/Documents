# Android.mk for compiling surf using OpenCV
# Created by Hiep Tran Huu
# For Smartphone Visible Light Communication
# Email: hieptran_bka_at_gmail_com
# HaNoi University of Science and Technology
# School of Electronics and Telecommunications


LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := nonfree_prebuilt
LOCAL_SRC_FILES := libnonfree.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := opencv_java_prebuilt
LOCAL_SRC_FILES := libopencv_java.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_C_INCLUDES:= D:\DoAnTotNghiep\Soft\OpenCV-2.4.10-android-sdk\sdk\native\jni\include
LOCAL_MODULE    := nonfree_jni
LOCAL_CFLAGS    := -Werror -O3 -ffast-math
LOCAL_LDLIBS    += -llog -ldl 
LOCAL_SHARED_LIBRARIES := nonfree_prebuilt opencv_java_prebuilt
LOCAL_SRC_FILES := nonfree_jni.cpp
include $(BUILD_SHARED_LIBRARY)