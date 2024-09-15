#include <stdio.h>
#include <stdlib.h>

#include "hypnode.h"

// Node type and callback declaration
struct _node_printf_struct {
    // Ports
    char* format;
    
    // Child nodes

    // Callback
    void (*_callback)(void* self);
};

// Node life-cycle functions
void* _node_printf_init();
void _node_printf_dispose(void* _node);
void _node_printf_callback(void* _self);
void _node_printf_trigger(void* _node);

// Module meta information
// Exported nodes, etc...
static _meta_export_node _export_symbols[] = {
    (_meta_export_node) {
        ._name = "std_experimental_printf",

        ._init = "_node_printf_init",
        ._dispose = "_node_printf_dispose",
        ._trigger = "_node_printf_trigger" 
    }
};

_meta_export_node* _meta_export_nodes();
//_meta_export_node* _meta_export_types();

#define INCLUDE_IMPLEMENTATION
#ifdef INCLUDE_IMPLEMENTATION

// Node life-cycle functions
void* _node_printf_init() {
    struct _node_printf_struct* node = malloc(sizeof(struct _node_printf_struct));

    node->format = "Hello world !\n";
    node->_callback = _node_printf_callback;

    return node;
}

void _node_printf_dispose(void* _node) {
    free(_node);
}

void _node_printf_callback(void* _self) {
    struct _node_printf_struct* self = _self;

    printf("%s", self->format);
}

void _node_printf_trigger(void* _node) {
    struct _node_printf_struct* node = _node;

    // for now we do not do any checks
    node->_callback(_node);
}

_meta_export_node* _meta_export_nodes() {
    return _export_symbols;    
}

#endif