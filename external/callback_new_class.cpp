#include "../include/callback.h"

extern "C" {
    void no_free_new_class(TwoIntsClass *buff) { };
    void has_free_new_class(TwoIntsClass *buff) { delete buff; };
    void callback_new_class(TwoIntsClass *buff, fp_class fp) { fp(buff); };
}