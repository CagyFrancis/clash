#include <stdlib.h>

class twoIntClass
{
    public: // Needed to access variables from label files
        int intOne;
        int intTwo;
};

extern "C" {
    void with_free(twoIntClass *buff) { delete buff; }
    void no_free(twoIntClass *buff) { /*free(buff)*/ }
    twoIntClass *alloc_good() { return new twoIntClass; }
    twoIntClass *alloc_bad() { return new twoIntClass; }
}

int main()
{
    twoIntClass *buff1 = alloc_good();
    twoIntClass *buff2 = alloc_bad();
    with_free(buff1);
    no_free(buff2);
    return 0;
}