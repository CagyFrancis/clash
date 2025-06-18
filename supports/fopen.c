#include <stdio.h>
#include <stdlib.h>

void with_free(FILE *buff) { fclose(buff); }
void no_free(FILE *buff) { /*free(buff)*/ }
FILE *alloc_good() { return fopen("file.txt", "w+"); }
FILE *alloc_bad() { return fopen("file.txt", "w+"); }

int main()
{
    FILE *buff1 = alloc_good();
    FILE *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}