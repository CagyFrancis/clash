#include <stdlib.h>

typedef struct twoIntStruct
{
    int intOne;
    int intTwo;
} twoIntStruct;

void with_free(twoIntStruct *buff) { free(buff); }
void no_free(twoIntStruct *buff) { /*free(buff)*/ }
twoIntStruct *alloc_good() { return (twoIntStruct *)malloc(sizeof(twoIntStruct)); }
twoIntStruct *alloc_bad() { return (twoIntStruct *)malloc(sizeof(twoIntStruct)); }

int main()
{
    twoIntStruct *buff1 = alloc_good();
    twoIntStruct *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}