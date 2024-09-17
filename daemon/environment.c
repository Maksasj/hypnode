#include "environment.h"

void init_environment(Environment* env) {
    DAEMON_LOG(INFO, "Initializing environment");

    create_queue(&env->packet_queue, 1000);

    memset(env->nodes, 0, MAX_NODES*sizeof(Node*));
    env->node_count = 0;
}

void dispose_environment(Environment* env) {
    free(env);
}

void load_module(Environment* env, const char* file_name) {
    DAEMON_LOG(INFO, "Loading module %s", file_name);

    void *module = dlopen(file_name, RTLD_NOW);

    if(module == NULL)
        DAEMON_LOG(ERROR, "could not load %s: %s", file_name, dlerror());

    _meta_export_nodes _meta_export_nodes = dlsym(module, "_meta_export_nodes");

    if(_meta_export_nodes == NULL)
        DAEMON_LOG(ERROR, "could not find _meta_export_nodes symbol in %s: %s", file_name, dlerror());

    _meta_export_node* export_node = (*_meta_export_nodes)();

    if(export_node == NULL)
        DAEMON_LOG(WARNING, "module does not export any nodes");

    // Todo for each node
    load_node(env, file_name, module, export_node[0]);
    // DAEMON_LOG(ERROR, "failed to load node from module %s", file_name);
}

void load_node(Environment* env, const char* file_name, void* module, _meta_export_node export_node) {
    DAEMON_LOG(INFO, "Loading node %s", export_node._name);

    _node_init init = dlsym(module, export_node._init);
    if(init == NULL)
        DAEMON_LOG(ERROR, "could not find symbol in %s: %s", file_name, dlerror());

    _node_dispose dispose = dlsym(module, export_node._dispose);
    if(dispose == NULL)
        DAEMON_LOG(ERROR, "could not find symbol in %s: %s", file_name, dlerror());

    _node_trigger trigger = dlsym(module, export_node._trigger);
    if(trigger == NULL)
        DAEMON_LOG(ERROR, "could not find symbol in %s: %s", file_name, dlerror());

    Node* node = (Node*) malloc(sizeof(Node));
    node->node = init();
    node->init = init; 
    node->dispose = dispose; 
    node->trigger = trigger; 
    node->meta = export_node;
    env->nodes[env->node_count] = node;

    DAEMON_LOG(INFO, "Successfully loaded node %s", export_node._name);
}
