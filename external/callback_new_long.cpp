#include "../include/callback.h"

extern "C" {
    void no_free_new_long(long *buff) { };
    void has_free_new_long(long *buff) { delete buff; };
    void callback_new_long(long *buff, fp_long fp) { fp(buff); };
}