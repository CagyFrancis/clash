#include <stdlib.h>

extern "C" {
    void with_free(int *buff) { delete buff; }
    void no_free(int *buff) { /*free(buff)*/ }
    int *alloc_good() { return new int; }
    int *alloc_bad() { return new int; }
}

int main()
{
    int *buff1 = alloc_good();
    int *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}