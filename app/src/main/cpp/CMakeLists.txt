cmake_minimum_required(VERSION 3.4.1)


include_directories(include)


# 编译头文件
file(GLOB my_source_path ${CMAKE_SOURCE_DIR}/*.cpp ${CMAKE_SOURCE_DIR}/*.c)
add_library(OpenCV SHARED ${my_source_path})


# 添加动态库及动态库链接地址
add_library(lib_opencv SHARED IMPORTED)
set_target_properties(lib_opencv
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libopencv_java4.so)


find_library(
        log-lib
        log)

# <android/bitmap.h>
find_library(
        jnigraphics-lib
        jnigraphics)

target_link_libraries(
        OpenCV
        ${log-lib}
        ${jnigraphics-lib}
        lib_opencv)