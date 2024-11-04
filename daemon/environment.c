#include "environment.h"

void init_environment(Environment* env) {
    DAEMON_LOG(INFO, "Initializing environment");

    create_queue(&env->packet_queue, 1000);

    memset(env->node_definitions, 0, MAX_NODE_DEFINITIONS*sizeof(_node_definition*));
    env->node_definition_count = 0;

    memset(env->node_instances, 0, MAX_NODES*sizeof(_node_instance_struct*));
    env->node_instance_count = 0;

    DAEMON_LOG(INFO, "Initialized environment");
}

void dispose_environment(Environment* env) {
    free(env);
}

void load_module(Environment* env, const char* file_name) {
    DAEMON_LOG(INFO, "Loading module %s", file_name);

    void *module = dlopen(file_name, RTLD_NOW);

    if(module == NULL)
        DAEMON_LOG(ERROR, "Could not load %s: %s", file_name, dlerror());

    // Exported node symbols
    unsigned long* _node_export_symbols_count = dlsym(module, "_node_export_symbols_count");

    if(_node_export_symbols_count == NULL)
        DAEMON_LOG(ERROR, "HypnodeModule is invalid, could not find _node_export_symbols_count symbol in %s: %s", file_name, dlerror());

    if(_node_export_symbols_count == 0)
        DAEMON_LOG(WARNING, "HypnodeModule %s does not export any symbols");

    _node_export_symbol* _node_export_symbols = dlsym(module, "_node_export_symbols");

    if(_node_export_symbols == NULL)
        DAEMON_LOG(ERROR, "HypnodeModule is invalid, could not find _node_export_symbols symbol in %s: %s", file_name, dlerror());

    // Import node symbols
    unsigned long* _node_import_symbols_count = dlsym(module, "_node_import_symbols_count");

    if(_node_import_symbols_count == NULL)
        DAEMON_LOG(ERROR, "HypnodeModule is invalid, could not find _node_import_symbols_count symbol in %s: %s", file_name, dlerror());

    if(*_node_import_symbols_count != 0)
        DAEMON_LOG(INFO, "HypnodeModule %s imports %d node symbols", file_name, *_node_import_symbols_count);

    struct {
        char* symbol_name;
        char* implementation_symbol
    }* _node_import_symbols = dlsym(module, "_node_import_symbols");

    if(_node_import_symbols == NULL)
        DAEMON_LOG(ERROR, "HypnodeModule is invalid, could not find _node_import_symbols symbol in %s: %s", file_name, dlerror());

    for(int i = 0; i < *_node_import_symbols_count; ++i) {
        const char* symbol_name = _node_import_symbols[i].symbol_name;
        const char* implementation_symbol = _node_import_symbols[i].implementation_symbol;

        DAEMON_LOG(INFO, "Importing %s node symbol into %s module", symbol_name, file_name);

        _node_implementation* _node_import_symbols = dlsym(module, implementation_symbol);
        if(_node_import_symbols == NULL)
            DAEMON_LOG(ERROR, "HypnodeModule is invalid, could not find %s symbol in %s: %s", implementation_symbol, file_name, dlerror());

        *_node_import_symbols = get_definition_by_name(env, symbol_name).implementation;

        DAEMON_LOG(INFO, "Successfully imported node %s", symbol_name);
    }

    // After resolving exports and imports we can instantiate node
    for(int i = 0; i < *_node_export_symbols_count; ++i) {
        _node_export_symbol export_node = _node_export_symbols[0];

        // Node definition
        _node_definition definition = load_node_definition(env, file_name, module, export_node);

        // Instantiate node

        // Todo, not every node needs to be instantiated 
        instantiate_node(env, definition, export_node);
    }
}

_node_definition load_node_definition(Environment* env, const char* file_name, void* module, _node_export_symbol export_node) {
    DAEMON_LOG(INFO, "Loading node %s definition", export_node._name);

    _node_init init = dlsym(module, export_node._init);
    if(init == NULL)
        DAEMON_LOG(ERROR, "could not find symbol in %s: %s", file_name, dlerror());

    _node_dispose dispose = dlsym(module, export_node._dispose);
    if(dispose == NULL)
        DAEMON_LOG(ERROR, "could not find symbol in %s: %s", file_name, dlerror());

    _node_trigger trigger = dlsym(module, export_node._trigger);
    if(trigger == NULL)
        DAEMON_LOG(ERROR, "could not find symbol in %s: %s", file_name, dlerror());
    
    _node_implementation implementation = dlsym(module, export_node._implementation);
    if(trigger == NULL)
        DAEMON_LOG(ERROR, "could not find symbol in %s: %s", file_name, dlerror());

    DAEMON_LOG(INFO, "Successfully loaded node %s definition", export_node._name);

    _node_definition definition = (_node_definition) {
        .name = export_node._name, // Todo, do we need to do copy there, probably yes   module could be unloaded

        .init = init,
        .dispose = dispose,
        .trigger = trigger,
        .implementation = implementation
    };

    env->node_definitions[env->node_definition_count] = definition;
    ++env->node_definition_count; // Todo

    return definition;
}

void instantiate_node(Environment* env, _node_definition definition, _node_export_symbol export_node) {
    DAEMON_LOG(INFO, "Instating node %s", export_node._name);

    // Instantiate node
    _node_instance_struct* instance = (_node_instance_struct*) malloc(sizeof(_node_instance_struct));

    instance->node = definition.init();
    instance->definition = definition; 
    instance->meta = export_node;

    env->node_instances[env->node_instance_count] = instance;
    ++env->node_instance_count; // Todo

    DAEMON_LOG(INFO, "Successfully instantiate node %s", export_node._name);
}

_node_definition get_definition_by_name(Environment* env, const char* name) {
    for(int i = 0; i < env->node_definition_count; ++i) {
        _node_definition definition = env->node_definitions[i];

        if(strcmp(name, definition.name) == 0)
            return definition;
    }

    DAEMON_LOG(ERROR, "Failed to find node definition by name %s", name);
    exit(1);
}
