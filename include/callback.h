#ifndef _CALLBACK_H
#define _CALLBACK_H
#include "std_testcase.h"

typedef void (*fp_char)(char *);
typedef void (*fp_wchar)(wchar_t *);
typedef void (*fp_int)(int *);
typedef void (*fp_int64)(int64_t *);
typedef void (*fp_long)(long *);
typedef void (*fp_struct)(twoIntsStruct *);
typedef void (*fp_file)(FILE *);

#ifdef __cplusplus

typedef void (*fp_class)(TwoIntsClass *);

extern "C" {
    void no_free_class(TwoIntsClass *buff);
    void has_free_class(TwoIntsClass *buff);
    void callback_class(TwoIntsClass *buff, fp_class fp);

    void no_free_new_char(char *buff);
    void no_free_new_wchar(wchar_t *buff);
    void no_free_new_int(int *buff);
    void no_free_new_int64(int64_t *buff);
    void no_free_new_long(long *buff);
    void no_free_new_struct(twoIntsStruct *buff);
    void no_free_new_class(TwoIntsClass *buff);

    void no_free_array_char(char *buff);
    void no_free_array_wchar(wchar_t *buff);
    void no_free_array_int(int *buff);
    void no_free_array_int64(int64_t *buff);
    void no_free_array_long(long *buff);
    void no_free_array_struct(twoIntsStruct *buff);
    void no_free_array_class(TwoIntsClass *buff);

    void has_free_new_char(char *buff);
    void has_free_new_wchar(wchar_t *buff);
    void has_free_new_int(int *buff);
    void has_free_new_int64(int64_t *buff);
    void has_free_new_long(long *buff);
    void has_free_new_struct(twoIntsStruct *buff);
    void has_free_new_class(TwoIntsClass *buff);

    void has_free_array_char(char *buff);
    void has_free_array_wchar(wchar_t *buff);
    void has_free_array_int(int *buff);
    void has_free_array_int64(int64_t *buff);
    void has_free_array_long(long *buff);
    void has_free_array_struct(twoIntsStruct *buff);
    void has_free_array_class(TwoIntsClass *buff);

    void callback_new_char(char *buff, fp_char fp);
    void callback_new_wchar(wchar_t *buff, fp_wchar fp);
    void callback_new_int(int *buff, fp_int fp);
    void callback_new_int64(int64_t *buff, fp_int64 fp);
    void callback_new_long(long *buff, fp_long fp);
    void callback_new_struct(twoIntsStruct *buff, fp_struct fp);
    void callback_new_class(TwoIntsClass *buff, fp_class fp);

    void callback_array_char(char *buff, fp_char fp);
    void callback_array_wchar(wchar_t *buff, fp_wchar fp);
    void callback_array_int(int *buff, fp_int fp);
    void callback_array_int64(int64_t *buff, fp_int64 fp);
    void callback_array_long(long *buff, fp_long fp);
    void callback_array_struct(twoIntsStruct *buff, fp_struct fp);
    void callback_array_class(TwoIntsClass *buff, fp_class fp);
}

#endif

/* no deallocation */
void no_free_char(char *buff);
void no_free_wchar(wchar_t *buff);
void no_free_int(int *buff);
void no_free_int64(int64_t *buff);
void no_free_long(long *buff);
void no_free_struct(twoIntsStruct *buff);

/* has deallocation */
void has_free_char(char *buff);
void has_free_wchar(wchar_t *buff);
void has_free_int(int *buff);
void has_free_int64(int64_t *buff);
void has_free_long(long *buff);
void has_free_struct(twoIntsStruct *buff);

/* callback functions */
void callback_char(char *buff, fp_char fp);
void callback_wchar(wchar_t *buff, fp_wchar fp);
void callback_int(int *buff, fp_int fp);
void callback_int64(int64_t *buff, fp_int64 fp);
void callback_long(long *buff, fp_long fp);
void callback_struct(twoIntsStruct *buff, fp_struct fp);

/* File IO */
void no_free_fopen(FILE *buff);
void has_free_fopen(FILE *buff);
void callback_fopen(FILE *buff, fp_file fp);

#endif