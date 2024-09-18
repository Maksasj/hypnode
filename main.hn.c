#include <stdio.h>
#include <stdlib.h>

#include "native.h"

// @include 'primitive.hn'
/* =========== types declaration ======== */
// Declared string type
static _type_info _string_type_info = (_type_info) {
    .type_name = "string",
    .category = Primitive,
    .compound_fields = NULL,
    .union_fields = NULL
};

// Declared i32 type
static _type_info _i32_type_info = (_type_info) {
    .type_name = "i32",
    .category = Primitive,
    .compound_fields = NULL,
    .union_fields = NULL
};
/* ====================================== */

// @include 'experimental.hn'
/* ======== log node declaration ======== */
// Node type and callback declaration
struct _node_log_struct {
    // Ports
    _port_struct message;
    
    // Child nodes

    // Callback
    void (*_callback)(void* self);
};

// Node life-cycle functions
void* _node_log_init();
void _node_log_dispose(void* _node);
// void _node_log_callback(void* _self); <- callback is provided by environment
void _node_log_trigger(void* _node);
/* ====================================== */

// Node type and callback declaration
struct _node_main_struct {
    // Ports
    _port_struct argc;
    _port_struct argv;

    // Child nodes
    struct _node_log_struct* mylog;

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
    
    // Initialize ports
    node->argc = (_port_struct) {
        .port_name = "argc",
        .value = NULL, // <- Initial value
        .value_type_info = _i32_type_info
    };

    node->argv = (_port_struct) {
        .port_name = "argv",
        .value = NULL, // <- Initial value
        .value_type_info = _string_type_info
    };

    // Initialize child nodes
    node->mylog = (struct _node_log_struct*) _node_log_init();

    return node;
}

void _node_main_dispose(void* _self) {
    struct _node_main_struct* self = _self;

    // dispose child nodes
    _node_log_dispose(self->mylog);

    // Todo dispose ports + values

    free(self);
}

void _node_main_callback(void* _self) {
    struct _node_main_struct* self = _self;
    
    /* propogate subgraph */
    /* call child node triggers */
}

void _node_main_trigger(void* _node) {
    struct _node_main_struct* node = _node;

    // Check argc and argv ports

    // for now we do not do any checks
    node->_callback(_node);
}

_meta_export_node* _meta_export_nodes() {
    return _export_symbols;    
}

#endif