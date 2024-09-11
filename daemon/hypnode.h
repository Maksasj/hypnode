#ifndef HYPNODE_H
#define HYPNODE_H

typedef struct {
    char* _init;
    char* _dispose;
    char* _trigger;
} _meta_export_node;

typedef _meta_export_node* (*_meta_export_nodes)();

typedef void* (*_node_init)();
typedef void (*_node_dispose)(void* _node);
typedef void (*_node_trigger)(void* _node);

#endif