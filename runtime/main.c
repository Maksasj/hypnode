#include <stdlib.h>
#include <stdio.h>
#include <dlfcn.h>

#include "hypnode.h"

int main(int argc, char* argv[]) {
    if(argc < 2) {
        fprintf(stderr, "Usage: hne [FILE]");
        return 1;
    }

    const char* file_name = argv[1];
    void *module = dlopen(file_name, RTLD_NOW);

    if(module == NULL) {
        fprintf(stderr, "ERROR: could not load %s: %s", file_name, dlerror());
        return 1;
    }

    _meta_export_nodes _meta_export_nodes = dlsym(module, "_meta_export_nodes");

    if(_meta_export_nodes == NULL) {
        fprintf(stderr, "ERROR: could not find entrypoint symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    _meta_export_node* export_node = (*_meta_export_nodes)();

    if(export_node == NULL) {
        fprintf(stderr, "ERROR: module does not export any nodes");
        return 1;
    }

    _node_init init = dlsym(module, export_node->_init);
    if(init == NULL) {
        fprintf(stderr, "ERROR: could not find symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    _node_dispose dispose = dlsym(module, export_node->_dispose);
    if(dispose == NULL) {
        fprintf(stderr, "ERROR: could not find symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    _node_trigger trigger = dlsym(module, export_node->_trigger);
    if(trigger == NULL) {
        fprintf(stderr, "ERROR: could not find symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    void* node = init();

    trigger(node);

    dispose(node);

    return 0;
}