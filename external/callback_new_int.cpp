#include "../include/callback.h"

extern "C" {
    void no_free_new_int(int *buff) { };
    void has_free_new_int(int *buff) { delete buff; };
    void callback_new_int(int *buff, fp_int fp) { fp(buff); };
}