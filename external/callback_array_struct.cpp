#include "../include/callback.h"

extern "C" {
    void no_free_array_struct(twoIntsStruct *buff) { };
    void has_free_array_struct(twoIntsStruct *buff) { delete[] buff; };
    void callback_array_struct(twoIntsStruct *buff, fp_struct fp) { fp(buff); };
}