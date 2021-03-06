//
// Created by ycl on 2019/5/19 .
//

#ifndef IDCARDRECOGNIZE_UTILS_H
#define IDCARDRECOGNIZE_UTILS_H


#include <jni.h>
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C" {

    void bitmap2Mat(JNIEnv *env, Mat *mat, jobject bitmap, bool needPremultiplyAlpha  ); // default needPremultiplyAlpha =0

    void mat2Bitmap(JNIEnv *env, Mat &mat, jobject bitmap, bool needPremultiplyAlpha  ); // default needPremultiplyAlpha =0

    jobject createBitmap(JNIEnv *env, Mat &srcData, jobject config);

}


#endif //IDCARDRECOGNIZE_UTILS_H
