//
// Created by dns on 2019-07-07.
//

#include <jni.h>
#include <string.h>
#include <string>
#include <stdio.h>

#define SECUREKEY "s1s1s1s1s1s1s1s1s1s1s1s1s1s1s1s1"



extern "C" JNIEXPORT jstring JNICALL
Java_com_dns_buggycrypto_MainActivity_stringKeyFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = SECUREKEY;
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_dns_buggycrypto_CryptoClass_stringKeyFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = SECUREKEY;
    return env->NewStringUTF(hello.c_str());
}