#include <stdio.h>
#include <stdlib.h>

// Node type and callback declaration
struct _node_printf_struct {
    // Ports
    char* format;
    
    // Child nodes

    // Callback
    void (*_callback)(void* self);
};

void _node_printf_callback(void* _self) {
    struct _node_printf_struct* self = _self;

    printf("%s", self->format);
}

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

void _node_printf_trigger(void* _node) {
    struct _node_printf_struct* node = _node;

    // for now we do not do any checks
    node->_callback(_node);
}

// Node specific meta information
// ...

// Module meta information
// Exported nodes, etc...
struct _meta_export_node {
    const char* _name;

    const char* _init;
    const char* _dispose;
    const char* _trigger;
};

static struct _meta_export_node _export_symbols[] = {
    (struct _meta_export_node) {
        ._name = "std_experimental_printf",

        ._init = "_node_printf_init",
        ._dispose = "_node_printf_dispose",
        ._trigger = "_node_printf_trigger" 
    }
};

struct _meta_export_node* _meta_export_nodes() {
    return _export_symbols;    
}