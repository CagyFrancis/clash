#include "../include/callback.h"

void no_free_long(long *buff) { };
void has_free_long(long *buff) { free(buff); };
void callback_long(long *buff, fp_long fp) { fp(buff); };