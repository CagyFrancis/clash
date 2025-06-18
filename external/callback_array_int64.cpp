#include "../include/callback.h"

extern "C" {
    void no_free_array_int64(int64_t *buff) { };
    void has_free_array_int64(int64_t *buff) { delete[] buff; };
    void callback_array_int64(int64_t *buff, fp_int64 fp) { fp(buff); };
}