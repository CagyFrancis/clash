#include "../include/callback.h"

extern "C" {
    void no_free_class(TwoIntsClass *buff) { };
    void has_free_class(TwoIntsClass *buff) { };
    void callback_class(TwoIntsClass *buff, fp_class fp) { fp(buff); };
}