#include "../include/callback.h"

extern "C" {
    void no_free_array_long(long *buff) { };
    void has_free_array_long(long *buff) { delete[] buff; };
    void callback_array_long(long *buff, fp_long fp) { fp(buff); };
}