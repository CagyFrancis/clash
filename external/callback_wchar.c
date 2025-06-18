#include "../include/callback.h"

void no_free_wchar(wchar_t *buff) { };
void has_free_wchar(wchar_t *buff) { free(buff); };
void callback_wchar(wchar_t *buff, fp_wchar fp) { fp(buff); };