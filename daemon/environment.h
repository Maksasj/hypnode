#ifndef ENVIRONMENT_H
#define ENVIRONMENT_H

#include <stdlib.h>
#include <memory.h>
#include <dlfcn.h>

#define DAEMON_BUILD
#include "lib/node_instance.h"
#include "lib/queue.h"

#include "utils/logger.h"

#define MAX_NODES 100
#define MAX_NODE_DEFINITIONS 100

typedef struct {
    Queue packet_queue; 

    // Loaded node definitions
    unsigned int node_definition_count;
    _node_definition node_definitions[MAX_NODE_DEFINITIONS];

    // Running node instances
    unsigned int node_instance_count;
    _node_instance_struct* node_instances[MAX_NODES];
} Environment;

// typedef void (*_environment_provider)();

void init_environment(Environment* env);
void dispose_environment(Environment* env);

void load_module(Environment* env, const char* file_name);
_node_definition load_node_definition(Environment* env, const char* file_name, void* module, _node_export_symbol export_node);

void instantiate_node(Environment* env, _node_definition definition, _node_export_symbol export_node);
_node_definition get_definition_by_name(Environment* env, const char* name);

#endif