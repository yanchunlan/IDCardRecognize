# IDCardRecognize

安卓中的身份证识别实现

准备：AS 3.3.2 ,gradle 4.10.1 ,OpenCV 4.1.0

## 一.	环境配置

1.	[下载android版本OpenCV](https://sourceforge.net/projects/opencvlibrary/files/4.1.0/opencv-4.1.0-android-sdk.zip/download)

2.	复制 opencv-4.1.0-android-sdk.zip\OpenCV-android-sdk\sdk\native\jni 中的include到cpp<br>
	复制 opencv-4.1.0-android-sdk.zip\OpenCV-android-sdk\sdk\native\libs 中相应的so复制到jnilibs里面
	
3.	配置cmake环境支持OpenCV

## 二.	加入tess-two库，文字识别

1.	加入依赖
```
    implementation 'com.rmtheis:tess-two:7.0.0'
```
2.	assets 导入文字识别训练的库文件

## 三.	处理流程

开始 -><br>
	图像处理：	图片灰度化 -> 图片二值化 -> 轮廓检测 -> 图像膨胀 -> 图片分割 <br>
	文字识别：	OCR文字样本训练 -> 记忆文本移植 -> 文字识别 <br>
结束