#include <stdlib.h>

extern "C" {
    void with_free(wchar_t *buff) { delete[] buff; }
    void no_free(wchar_t *buff) { /*free(buff)*/ }
    wchar_t *alloc_good() { return new wchar_t[10]; }
    wchar_t *alloc_bad() { return new wchar_t[10]; }
}

int main()
{
    wchar_t *buff1 = alloc_good();
    wchar_t *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}