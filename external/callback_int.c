#include "../include/callback.h"

void no_free_int(int *buff) { };
void has_free_int(int *buff) { free(buff); };
void callback_int(int *buff, fp_int fp) { fp(buff); };