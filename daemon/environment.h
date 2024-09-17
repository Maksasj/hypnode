#ifndef ENVIRONMENT_H
#define ENVIRONMENT_H

#include <stdlib.h>
#include <memory.h>
#include <dlfcn.h>

#define DAEMON_BUILD
#include "lib/node.h"
#include "lib/queue.h"

#include "logger.h"

typedef struct {
    void* node;

    _node_init init;
    _node_dispose dispose;
    _node_trigger trigger;

    _meta_export_node meta;
} Node;

#define MAX_NODES 100

typedef struct {
    Queue packet_queue; 

    // Top level nodes
    unsigned int node_count;
    Node* nodes[MAX_NODES];
} Environment;

Environment* new_environment();
void free_environment(Environment* env);

void load_module(Environment* env, const char* file_name);
void load_node(Environment* env, const char* file_name, void* module, _meta_export_node export_node);

#endif