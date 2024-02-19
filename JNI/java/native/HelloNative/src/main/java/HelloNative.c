#include "HelloNative.h"
#include <stdio.h>

JNIEXPORT void JNICALL Java_HelloNative_greeting
  (JNIEnv *, jclass) {
  printf("Hello native\n");
  }