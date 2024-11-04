#include <stdio.h>
#include <stdlib.h>

#include "native.h"

// Node type and callback declaration
struct _node_printf_struct {
    // Ports
    char* format;

    // Callback
    void (*_implementation)(void* self);
};

// Node life-cycle functions
void* _node_printf_init();
void _node_printf_dispose(void* _node);
void _node_printf_trigger(void* _node);

void _node_printf_implementation(void* _self);

// Node life-cycle functions
void* _node_printf_init() {
    struct _node_printf_struct* node = malloc(sizeof(struct _node_printf_struct));

    node->format = "Hello world !\n";
    node->_implementation = _node_printf_implementation;

    return node;
}

void _node_printf_dispose(void* _node) {
    free(_node);
}

void _node_printf_trigger(void* _node) {
    struct _node_printf_struct* node = _node;

    // for now we do not do any checks
    node->_implementation(_node);
}

void _node_printf_implementation(void* _self) {
    struct _node_printf_struct* self = _self;

    printf("%s", self->format);
}

// HypnodeModule meta information
/* ================ meta ================ */

unsigned long _node_import_symbols_count = 0;
struct {
    char* symbol_name;
    char* implementation_symbol
} _node_import_symbols[] = {

};
unsigned long _node_export_symbols_count = 1;
_node_export_symbol _node_export_symbols[] = {
    (_node_export_symbol) {
        ._name = "std_printf",

        ._init = "_node_printf_init",
        ._dispose = "_node_printf_dispose",
        ._trigger = "_node_printf_trigger",

        ._implementation = "_node_printf_implementation"
    }
};

/* ====================================== */