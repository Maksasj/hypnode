#include <stdio.h>
#include <stdlib.h>

// Node type and callback declaration
struct _node_printf_struct {
    // Ports

    // Callback
    void (*_callback)(void* self);
};

void _node_printf_callback(void* _self) {
    struct _node_printf_struct* self = _self;
}

// Node life-cycle functions
void* _node_printf_init() {
    struct _node_printf_struct* node = malloc(sizeof(struct _node_printf_struct));

    node->_callback = _node_printf_callback;

    return node;
}

void _node_printf_dispose(void* _node) {
    free(_node);
}

void _node_printf_trigger(void* _node) {
    struct _node_printf_struct* node = _node;
}

// Node specific meta information
// ...

// Module meta information
// Exported nodes, etc...
struct _meta_export_symbol {
    char* _name;
};

struct _meta_export_symbol* _meta_export_symbols() {
    return NULL;    
}