#include <stdio.h>
#include <stdlib.h>
int main()
{
    system("diff -U 0 file1v1.java file1v1_2/file1v1.java > patch1.patch");
    system("sed -i 1,2d patch1.patch");
    return 0;
}