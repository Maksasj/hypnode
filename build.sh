gcc -c -Wall -Werror -fpic std/native/printf.c
gcc -shared -o printf.so printf.o