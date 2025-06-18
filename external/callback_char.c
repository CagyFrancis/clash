#include "../include/callback.h"

void no_free_char(char *buff) { };
void has_free_char(char *buff) { free(buff); };
void callback_char(char *buff, fp_char fp) { fp(buff); };