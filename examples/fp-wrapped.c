#include <stdlib.h>

typedef void (*FunPtr)(char *);
void with_free(char *buff) { free(buff); }
void no_free(char *buff) { /*free(buff)*/ }
FunPtr unwrap(FunPtr *fp) { return *fp; }
char *alloc_good() { return (char *)malloc(sizeof(char)); }
char *alloc_bad() { return (char *)malloc(sizeof(char)); }

int main() {
    char *buff1 = alloc_good();
    char *buff2 = alloc_bad();
    FunPtr fp1 = with_free;
    FunPtr fp2 = no_free;
    FunPtr fp3 = unwrap(&fp1);
    FunPtr fp4 = unwrap(&fp2);
    fp3(buff1);
    fp4(buff2);
    return 0;
}