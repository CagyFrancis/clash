#include "../include/callback.h"

extern "C" {
    void no_free_array_int(int *buff) { };
    void has_free_array_int(int *buff) { delete[] buff; };
    void callback_array_int(int *buff, fp_int fp) { fp(buff); };
}