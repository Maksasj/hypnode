#include <stdlib.h>
#include <stdio.h>
#include <dlfcn.h>

typedef void (*entrypoint)(const char* message);

int main() {
    const char* libplug_file_name = "./printf.so";
    void *libplug = dlopen(libplug_file_name, RTLD_NOW);

    if(libplug == NULL) {
        fprintf(stderr, "ERROR: could not load %s: %s", libplug_file_name, dlerror());
        return 1;
    }

    entrypoint fun = dlsym(libplug, "entrypoint");

    if(fun == NULL) {
        fprintf(stderr, "ERROR: could not find entrypoint symbol in %s: %s", libplug_file_name, dlerror());
        return 1;
    }

    (*fun)("Hello from plugin");

    return 0;
}