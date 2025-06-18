#include <stdlib.h>

void with_free(long *buff) { free(buff); }
void no_free(long *buff) { /*free(buff)*/ }
long *alloc_good() { return (long *)malloc(sizeof(long)); }
long *alloc_bad() { return (long *)malloc(sizeof(long)); }

int main()
{
    long *buff1 = alloc_good();
    long *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}