//
// Created by dns on 2019-07-07.
//

#include <jni.h>
#include <string.h>
#include <string>
#include <stdio.h>
#include <iostream>

#define SECUREKEY "y0u3c4ntf1ndth1skeyc0zits0s3cur3"

#define OBFSKEY "@h&#sh$l@n&@de&bf#sc$@e@his&kpls"
//th0ush4ltn0tde0bfusc4tethis0kpls


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



extern "C"
JNIEXPORT jstring JNICALL
Java_com_dns_buggycrypto_CryptoClass_stringObfuscatedKeyFromJNI(JNIEnv *env, jobject /* this */) {

    std::string obfskey_str = OBFSKEY;

    std::replace( obfskey_str.begin(), obfskey_str.end(), '@', 't');
    std::replace( obfskey_str.begin(), obfskey_str.end(), '&', '0');
    std::replace( obfskey_str.begin(), obfskey_str.end(), 'D', '$');
    std::replace( obfskey_str.begin(), obfskey_str.end(), '#', 'u');
    std::replace( obfskey_str.begin(), obfskey_str.end(), '$', '4');

    return env->NewStringUTF(obfskey_str.c_str());
}