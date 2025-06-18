#include "../include/callback.h"

void no_free_fopen(FILE *buff) { };
void has_free_fopen(FILE *buff) { fclose(buff); };
void callback_fopen(FILE *buff, fp_file fp) { fp(buff); };