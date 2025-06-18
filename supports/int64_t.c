#include <stdint.h>
#include <stdlib.h>

void with_free(int64_t *buff) { free(buff); }
void no_free(int64_t *buff) { /*free(buff)*/ }
int64_t *alloc_good() { return (int64_t *)malloc(sizeof(int64_t)); }
int64_t *alloc_bad() { return (int64_t *)malloc(sizeof(int64_t)); }

int main()
{
    int64_t *buff1 = alloc_good();
    int64_t *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}