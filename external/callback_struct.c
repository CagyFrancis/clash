#include "../include/callback.h"

void no_free_struct(twoIntsStruct *buff) { };
void has_free_struct(twoIntsStruct *buff) { free(buff); };
void callback_struct(twoIntsStruct *buff, fp_struct fp) { fp(buff); };