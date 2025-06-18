#include "../include/callback.h"

extern "C" {
    void no_free_new_char(char *buff) { };
    void has_free_new_char(char *buff) { delete buff; };
    void callback_new_char(char *buff, fp_char fp) { fp(buff); };
}