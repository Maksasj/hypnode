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
    
    // Callback
    void (*_implementation)(void* self);
};

// Node life-cycle functions
void* _node_log_init();
void _node_log_dispose(void* _node);
void _node_log_trigger(void* _node);

_node_implementation _node_log_implementation; // <- callback is provided by environment

// Node life-cycle functions
void* _node_log_init() {
    struct _node_log_struct* node = malloc(sizeof(struct _node_log_struct));

    node->_implementation = _node_log_implementation;

    // Initialize port
    node->message = (_port_struct) {
        .port_name = "message",
        .value = NULL, // <- Initial value
        .value_type_info = _string_type_info
    };

    return node;
}

void _node_log_dispose(void* _node) {
    free(_node);
}

void _node_log_trigger(void* _node) {
    struct _node_log_struct* node = _node;

    // for now we do not do any checks
    node->_implementation(_node);
}
/* ====================================== */

// Node type and callback declaration
struct _node_main_struct {
    // Ports
    _port_struct argc;
    _port_struct argv;

    // Callback
    void (*_implementation)(void* self);
};

// Node life-cycle functions
void* _node_main_init();
void _node_main_dispose(void* _self);
void _node_main_trigger(void* _node);

void _node_main_implementation(void* _self);

// Node life-cycle functions
void* _node_main_init() {
    struct _node_main_struct* node = malloc(sizeof(struct _node_main_struct));

    node->_implementation = _node_main_implementation;
    
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

    _node_main_trigger(node);

    return node;
}

void _node_main_dispose(void* _self) {
    struct _node_main_struct* self = _self;

    // Todo dispose ports + values

    free(self);
}

void _node_main_trigger(void* _node) {
    struct _node_main_struct* node = _node;

    // Check argc and argv ports

    // for now we do not do any checks
    node->_implementation(_node);
}

void _node_main_implementation(void* _self) {
    struct _node_main_struct* self = _self;
    
    // Initialize child nodes
    struct _node_log_struct* mylog = (struct _node_log_struct*) _node_log_init();

    /* propogate subgraph */
    /* call child node triggers */

    _node_log_dispose(mylog);
}

// HypnodeModule meta information
/* ================ meta ================ */

unsigned long _node_import_symbols_count = 1;
struct {
    char* symbol_name;
    char* implementation_symbol
} _node_import_symbols[] = {
    { "std_experimental_log", "_node_log_implementation" }
};

unsigned long _node_export_symbols_count = 1;
_node_export_symbol _node_export_symbols[] = {
    (_node_export_symbol) {
        ._name = "entrypoint",
        
        ._init = "_node_main_init",
        ._dispose = "_node_main_dispose",
        ._trigger = "_node_main_trigger",

        ._implementation = "_node_main_implementation"
    }
};

/* ====================================== */