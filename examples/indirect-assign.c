#include <stdlib.h>

void with_free(char *buff) { free(buff); }
void no_free(char *buff) { /*free(buff)*/ }
void assign(char **dst, char *src) { *dst = src; }
char *alloc_good() { return (char *)malloc(sizeof(char)); }
char *alloc_bad() { return (char *)malloc(sizeof(char)); }

int main()
{
    char *buff1 = alloc_good();
    char *buff2 = alloc_bad();
    char *r1, *r2;
    assign(&r1, buff1);
    assign(&r2, buff2);
    with_free(r1);
    no_free(r2);
    return 0;
}