#include "../include/callback.h"

extern "C" {
    void no_free_array_char(char *buff) { };
    void has_free_array_char(char *buff) { delete[] buff; };
    void callback_array_char(char *buff, fp_char fp) { fp(buff); };
}