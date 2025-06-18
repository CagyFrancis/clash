#include "../include/callback.h"

extern "C" {
    void no_free_array_class(TwoIntsClass *buff) { };
    void has_free_array_class(TwoIntsClass *buff) { delete[] buff; };
    void callback_array_class(TwoIntsClass *buff, fp_class fp) { fp(buff); };
}