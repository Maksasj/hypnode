#include <stdlib.h>
#include "native.h"

// Node type and callback declaration
struct _node_sum_struct {
    // Ports
    
    // Callback
    void (*_implementation)(void* self);
};

// Node life-cycle functions
void* _node_sum_init();
void _node_sum_dispose(void* _node);
void _node_sum_trigger(void* _node);

void _node_sum_implementation(void* _self);

// Node life-cycle functions
void* _node_sum_init() {
    struct _node_sum_struct* node = malloc(sizeof(struct _node_sum_struct));

    node->_implementation = _node_sum_implementation;

    return node;
}

void _node_sum_dispose(void* _node) {
    free(_node);
}

void _node_sum_trigger(void* _node) {
    struct _node_sum_struct* node = _node;

    // for now we do not do any checks
    node->_implementation(_node);
}

void _node_sum_implementation(void* _self) {
    // struct _node_sum_struct* self = _self;
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
        ._name = "std_sum",
        
        ._init = "_node_sum_init",
        ._dispose = "_node_sum_dispose",
        ._trigger = "_node_sum_trigger",

        ._implementation = "_node_sum_implementation"
    }
};

/* ====================================== */