#include <stdlib.h>

typedef struct twoIntStruct
{
    int intOne;
    int intTwo;
} twoIntStruct;

extern "C" {
    void with_free(twoIntStruct *buff) { delete buff; }
    void no_free(twoIntStruct *buff) { /*free(buff)*/ }
    twoIntStruct *alloc_good() { return new twoIntStruct; }
    twoIntStruct *alloc_bad() { return new twoIntStruct; }
}

int main()
{
    twoIntStruct *buff1 = alloc_good();
    twoIntStruct *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}