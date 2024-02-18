#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int argc, char *argv[])
{
    char* command=(char*)malloc(100);
    strcpy(command,"cat ");
    for(int i=1;i<argc;i++)
    {
        strcat(command,argv[i]);
        strcat(command," ");
    }
    strcat(command,"> merge.patch");
    system(command);
    return 0;
}