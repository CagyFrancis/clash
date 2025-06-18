#include <stdlib.h>

extern "C" {
    void with_free(long *buff) { delete buff; }
    void no_free(long *buff) { /*free(buff)*/ }
    long *alloc_good() { return new long; }
    long *alloc_bad() { return new long; }
}

int main()
{
    long *buff1 = alloc_good();
    long *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}