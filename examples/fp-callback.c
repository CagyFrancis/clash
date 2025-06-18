#include <stdlib.h>

typedef void (*FunPtr)(char *);
void with_free(char *buff) { free(buff); }
void no_free(char *buff) { /*free(buff)*/ }
void callback(char *buff, FunPtr fn) { return fn(buff); }
char *alloc_good() { return (char *)malloc(sizeof(char)); }
char *alloc_bad() { return (char *)malloc(sizeof(char)); }

int main() {
    char *buff1 = alloc_good();
    char *buff2 = alloc_bad();
    callback(buff1, with_free);
    callback(buff2, no_free);
    return 0;
}