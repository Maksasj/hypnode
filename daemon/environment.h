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

typedef struct {
    Queue packet_queue; 

    // Top level nodes
    unsigned int node_count;
    _node_instance_struct* nodes[MAX_NODES];
} Environment;

// typedef void (*_environment_provider)();

void init_environment(Environment* env);
void dispose_environment(Environment* env);

void load_module(Environment* env, const char* file_name);
void load_node(Environment* env, const char* file_name, void* module, _meta_export_node export_node);

#endif