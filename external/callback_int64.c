#include "../include/callback.h"

void no_free_int64(int64_t *buff) { };
void has_free_int64(int64_t *buff) { free(buff); };
void callback_int64(int64_t *buff, fp_int64 fp) { fp(buff); };