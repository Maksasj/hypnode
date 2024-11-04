#include <stdlib.h>
#include <stdio.h>

#include "native.h"
#include "logger.h"

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

void _node_log_implementation(void* _self);

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

void _node_log_implementation(void* _self) {
    struct _node_log_struct* self = _self;

    const char* message = self->message.value;

    DAEMON_LOG(EXPERIMENTAL, "%s", message);
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
        ._name = "std_experimental_log",

        ._init = "_node_log_init",
        ._dispose = "_node_log_dispose",
        ._trigger = "_node_log_trigger",

        ._implementation = "_node_log_implementation"
    },
};

/* ====================================== */