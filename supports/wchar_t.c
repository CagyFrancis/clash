#include <stdlib.h>

void with_free(wchar_t *buff) { free(buff); }
void no_free(wchar_t *buff) { /*free(buff)*/ }
wchar_t *alloc_good() { return (wchar_t *)malloc(sizeof(wchar_t)); }
wchar_t *alloc_bad() { return (wchar_t *)malloc(sizeof(wchar_t)); }

int main()
{
    wchar_t *buff1 = alloc_good();
    wchar_t *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}