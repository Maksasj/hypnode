#include <stdio.h>
#include <stdlib.h>

#include "native.h"

// Node type and callback declaration
struct _node_main_struct {
    // Ports

    // Child nodes

    // Callback
    void (*_callback)(void* self);
};

// Node life-cycle functions
void* _node_main_init();
void _node_main_dispose(void* _node);
void _node_main_callback(void* _self);
void _node_main_trigger(void* _node);

// Module meta information
// Exported nodes, etc...
static _meta_export_node _export_symbols[] = {
    (_meta_export_node) {
        ._name = "entrypoint",
        
        ._init = "_node_main_init",
        ._dispose = "_node_main_dispose",
        ._trigger = "_node_main_trigger" 
    }
};

_meta_export_node* _meta_export_nodes();
//_meta_export_node* _meta_export_types();

#define INCLUDE_IMPLEMENTATION
#ifdef INCLUDE_IMPLEMENTATION

// Node life-cycle functions
void* _node_main_init() {
    struct _node_main_struct* node = malloc(sizeof(struct _node_main_struct));

    node->_callback = _node_main_callback;
    
    // Create child nodes

    return node;
}

void _node_main_dispose(void* _node) {
    free(_node);
}

void _node_main_callback(void* _self) {
    struct _node_main_struct* self = _self;

    // Create node mylog
    // send packet

    /*
    let l: mylog;
    l.message <- "Hello world !\n";
    */
}

void _node_main_trigger(void* _node) {
    struct _node_main_struct* node = _node;

    // for now we do not do any checks
    node->_callback(_node);
}

_meta_export_node* _meta_export_nodes() {
    return _export_symbols;    
}

#endif