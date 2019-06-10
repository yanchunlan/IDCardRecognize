#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include "AndroidLog.h"
#include "utils.h"


#define DEFAULT_WIDTH 640
#define DEFAULT_HEIGHT 400
#define FIX_IDCARD_SIZE Size(DEFAULT_WIDTH,DEFAULT_HEIGHT)

using namespace std;
using namespace cv;

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_idcardrecognize_MainActivity_findIdNumber(JNIEnv *env, jobject instance,
                                                           jobject bitmap, jobject argb8888) {
    // 原图
    Mat src_img;
    Mat dst;
    Mat dst_img;

    //1 先将Bitmap转成Mat
    bitmap2Mat(env, &src_img, bitmap,0);

    //2 归一化
    resize(src_img, src_img, FIX_IDCARD_SIZE);

    //3 灰度化
    cvtColor(src_img, dst, COLOR_RGB2GRAY);

    //4 二值化
    threshold(dst, dst, 100, 255, THRESH_BINARY);

    //5 膨胀
    Mat erodeElement = getStructuringElement(MORPH_RECT, Size(20, 10));
    erode(dst, dst, erodeElement);

    //6 轮廓检测
    vector<vector<Point>> contours;
    vector<Rect> rects;
    findContours(dst, contours, RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0, 0));
    for (int i = 0; i < contours.size(); ++i) {
        //获取到矩形区域
        Rect rect = boundingRect(contours.at(i));
        //绘制
//        rectangle(dst,rect,Scalar(0,0,255));
        //7 逻辑处理，找到号码所在区域
        //身份证号码有固定宽高比>1:8&&<1:16
        if (rect.width > rect.height * 8 && rect.width < rect.height * 16) {
            rects.push_back(rect);
        }
    }
    //9 继续查找坐标最低的矩形区域
    int lowPoint = 0;
    Rect finalRect;
    for (int i = 0; i < rects.size(); i++) {
        Rect rect = rects.at(i);
        Point point = rect.tl();
        if (point.y > lowPoint) {
            lowPoint = point.y;
            finalRect = rect;
        }
    }
    //10 图像切割
    //    rectangle(dst,finalRect,Scalar(0,0,255));
    dst_img = src_img(finalRect);
    //2 将Mat转成Bitmap
    return createBitmap(env, dst_img, argb8888);
}