//
// Created by Administrator on 2019/5/19 0019.
//

#ifndef IDCARDRECOGNIZE_UTILS_H
#define IDCARDRECOGNIZE_UTILS_H


#include <jni.h>
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C" {

    void bitmap2Mat(JNIEnv *env, jobject bitmap, Mat *mat, bool needPremultiplyAlpha = 0);

    void mat2Bitmap(JNIEnv *env, jobject bitmap, Mat mat, bool needPremultiplyAlpha = 0);

    jobject createBitmap(JNIEnv *env, Mat srcData, jobject config);

}


#endif //IDCARDRECOGNIZE_UTILS_H
