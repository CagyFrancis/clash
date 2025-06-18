#include "../include/callback.h"

extern "C" {
    void no_free_array_wchar(wchar_t *buff) { };
    void has_free_array_wchar(wchar_t *buff) { delete[] buff; };
    void callback_array_wchar(wchar_t *buff, fp_wchar fp) { fp(buff); };
}