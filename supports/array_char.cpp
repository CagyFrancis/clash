#include <stdlib.h>

extern "C" {
    void with_free(char *buff) { delete[] buff; }
    void no_free(char *buff) { /*free(buff)*/ }
    char *alloc_good() { return new char[10]; }
    char *alloc_bad() { return new char[10]; }
}

int main()
{
    char *buff1 = alloc_good();
    char *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}