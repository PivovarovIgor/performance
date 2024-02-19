#include <jni.h>
#include <string>
#include <iostream>
extern "C" JNIEXPORT jstring

JNICALL
Java_ru_brauer_nativeexample_SimpleViewModel_stringFromJNI(
        JNIEnv *env,
        jobject /* this */,
        jstring name) {
    jboolean isCopy;
    const char *convertValue = env->GetStringUTFChars(name, &isCopy);
    std::string nameConverted = std::string(convertValue);
    std::string hello = "VVV Hello from C++" + nameConverted;
    std::cout << hello;
    return env->NewStringUTF(hello.c_str());
}