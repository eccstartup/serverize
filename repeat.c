#include <stdio.h>
#include <unistd.h>

int main() {
    int i;
    for (i = 0; i < 10; i++) {
        sleep(1);
        printf("i=%d\n", i);
        // fflush(stdout);
    }
    return 0;
}