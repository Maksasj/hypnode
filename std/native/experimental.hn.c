#include <stdlib.h>
#include <stdio.h>

#include "native.h"
#include "logger.h"

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
void _node_log_callback(void* _self);
void _node_log_trigger(void* _node);

// Module meta information
// Exported nodes, etc...
static _meta_export_node _export_symbols[] = {
    (_meta_export_node) {
        ._name = "std_experimental_log",

        ._init = "_node_log_init",
        ._dispose = "_node_log_dispose",
        ._trigger = "_node_log_trigger" 
    },
};

_meta_export_node* _meta_export_nodes();
//_meta_export_node* _meta_export_types();

#define INCLUDE_IMPLEMENTATION
#ifdef INCLUDE_IMPLEMENTATION

// Node life-cycle functions
void* _node_log_init() {
    struct _node_log_struct* node = malloc(sizeof(struct _node_log_struct));

    node->_callback = _node_log_callback;

    return node;
}

void _node_log_dispose(void* _node) {
    free(_node);
}

void _node_log_callback(void* _self) {
    struct _node_log_struct* self = _self;

    const char* message = self->message.value;

    DAEMON_LOG(EXPERIMENTAL, "%s", message);
}

void _node_log_trigger(void* _node) {
    struct _node_log_struct* node = _node;

    // for now we do not do any checks
    node->_callback(_node);
}

_meta_export_node* _meta_export_nodes() {
    return _export_symbols;    
}

#endif