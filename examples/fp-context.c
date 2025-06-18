#include <stdlib.h>

typedef void (*FunPtr)(char *);
void with_free(char *buff) { free(buff); }
void no_free(char *buff) { /*free(buff)*/ }
FunPtr use(FunPtr fp) { return fp; }
char *alloc_good() { return (char *)malloc(sizeof(char)); }
char *alloc_bad() { return (char *)malloc(sizeof(char)); }

int main() {
    char *buff1 = alloc_good();
    char *buff2 = alloc_bad();
    FunPtr fp1 = use(with_free);
    FunPtr fp2 = use(no_free);
    fp1(buff1);
    fp2(buff2);
    return 0;
}