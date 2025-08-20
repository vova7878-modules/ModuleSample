#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_v7878_zygisk_sample_EntryPoint_hello(JNIEnv *env, jclass) {
    return env->NewStringUTF("Hello from jni!");
}

extern "C" JNIEXPORT void JNICALL
Java_com_v7878_zygisk_sample_EntryPoint_init(JNIEnv *, jclass) {
    // nop
}
