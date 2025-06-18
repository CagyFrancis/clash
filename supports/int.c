#include <stdlib.h>

void with_free(int *buff) { free(buff); }
void no_free(int *buff) { /*free(buff)*/ }
int *alloc_good() { return (int *)malloc(sizeof(int)); }
int *alloc_bad() { return (int *)malloc(sizeof(int)); }

int main()
{
    int *buff1 = alloc_good();
    int *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}