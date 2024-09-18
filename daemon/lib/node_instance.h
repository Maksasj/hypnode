#ifndef NODE_INSTANCE_H
#define NODE_INSTANCE_H

typedef void* (*_node_init)();
typedef void (*_node_dispose)(void* _node);
typedef void (*_node_trigger)(void* _node);

typedef void (*_node_implementation)(void* _node);

// exported node symbols
typedef struct {
    const char* _name;

    const char* _init;
    const char* _dispose;
    const char* _trigger;

    const char* _implementation;
} _node_export_symbol;

// node definitions, NOT instance
typedef struct {
    // Todo not sure is this really needed there, since we have this in _node_export_symbol
    // but _node_export_symbol is a meta struct, so there is not really a point storing it in _node_instance_struct
    // , probably only for node_info type or smth.
    const char* name;

    _node_init init;
    _node_dispose dispose;
    _node_trigger trigger;
    _node_implementation implementation;
} _node_definition;

// active node instance
typedef struct {
    void* node;

    _node_definition definition;
    _node_export_symbol meta;
} _node_instance_struct;

#ifdef DAEMON_BUILD
    typedef _node_export_symbol* (*_node_export_symbols)();
#endif

#endif