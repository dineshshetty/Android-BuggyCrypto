//
// Created by dns on 2019-07-07.
//

#include <jni.h>
#include <string.h>
#include <string>
#include <stdio.h>
#include <iostream>

#include <android/log.h>
#include <unistd.h>
#include <stdlib.h>

#define SECUREKEY "y0u3c4ntf1ndth1skeyc0zits0s3cur3"

#define OBFSKEY "@h&#sh$l@n&@de&bf#sc$@e@his&kpls"
//th0ush4ltn0tde0bfusc4tethis0kpls

const char b64chars[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

size_t b64_encoded_size(size_t inlen)
{
    size_t ret;

    ret = inlen;
    if (inlen % 3 != 0)
        ret += 3 - (inlen % 3);
    ret /= 3;
    ret *= 4;

    return ret;
}
char *b64_encode(const unsigned char *in, size_t len)
{
    char   *out;
    size_t  elen;
    size_t  i;
    size_t  j;
    size_t  v;

    if (in == NULL || len == 0)
        return NULL;

    elen = b64_encoded_size(len);
    out  = static_cast<char *>(malloc(elen + 1));
    out[elen] = '\0';

    for (i=0, j=0; i<len; i+=3, j+=4) {
        v = in[i];
        v = i+1 < len ? v << 8 | in[i+1] : v << 8;
        v = i+2 < len ? v << 8 | in[i+2] : v << 8;

        out[j]   = b64chars[(v >> 18) & 0x3F];
        out[j+1] = b64chars[(v >> 12) & 0x3F];
        if (i+1 < len) {
            out[j+2] = b64chars[(v >> 6) & 0x3F];
        } else {
            out[j+2] = '=';
        }
        if (i+2 < len) {
            out[j+3] = b64chars[v & 0x3F];
        } else {
            out[j+3] = '=';
        }
    }

    return out;
}
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


extern "C"
JNIEXPORT jstring JNICALL
Java_com_dns_buggycrypto_CryptoClass_tempFunc(JNIEnv *env, jobject /* this */, jstring usermessage, jint messagesize) {

//    std::replace( obfskey_str.begin(), obfskey_str.end(), '@', 't');
//    std::replace( obfskey_str.begin(), obfskey_str.end(), '&', '0');
//    std::replace( obfskey_str.begin(), obfskey_str.end(), 'D', '$');
//    std::replace( obfskey_str.begin(), obfskey_str.end(), '#', 'u');
//    std::replace( obfskey_str.begin(), obfskey_str.end(), '$', '4');

    jboolean isCopy;
    char* ch = (char*) env->GetStringUTFChars(usermessage, &isCopy);


    char       *enc;
    char       *out;
    size_t      out_len;
    enc = b64_encode((const unsigned char *)ch, strlen(ch));
    return env->NewStringUTF(enc);
}
