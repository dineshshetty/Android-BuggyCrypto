//
// Created by dns on 2019-07-07.
//

#include <jni.h>
#include <string.h>
#include <string>

#include <jni.h>
#include <stdio.h>
#include <string>
#include "aes/aes.h"
#include "aes/aes.c"



#define SECUREKEY "s3cr3tS3cur3K3y"





extern "C" JNIEXPORT jstring JNICALL
Java_com_dns_buggycrypto_MainActivity_stringKeyFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = SECUREKEY;
    return env->NewStringUTF(hello.c_str());
}