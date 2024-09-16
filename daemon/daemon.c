#include <stdlib.h>
#include <stdio.h>
#include <dlfcn.h>
#include <pthread.h>

#define DAEMON_BUILD
#include "lib/hypnode.h"

#include "tcp_interface.h"
#include "node_worker.h"

int load_node(const char* file_name, void* module, _meta_export_node export_node) {
    _node_init init = dlsym(module, export_node._init);
    if(init == NULL) {
        fprintf(stderr, "ERROR: could not find symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    _node_dispose dispose = dlsym(module, export_node._dispose);
    if(dispose == NULL) {
        fprintf(stderr, "ERROR: could not find symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    _node_trigger trigger = dlsym(module, export_node._trigger);
    if(trigger == NULL) {
        fprintf(stderr, "ERROR: could not find symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    void* node = init();

    trigger(node);

    dispose(node);

    return 0;
}

int main(int argc, char* argv[]) {
    DAEMON_LOG(INFO, "Started daemon");

    pthread_t tcp_interface_thread_id;
    pthread_create(&tcp_interface_thread_id, NULL, tcp_interface_thread_fun, NULL);

    pthread_t node_worker_thread_id;
    pthread_create(&node_worker_thread_id, NULL, node_worker_thread_fun, NULL);

    while(1) {
        // Running
    }

    pthread_join(tcp_interface_thread_id, NULL);
    pthread_join(node_worker_thread_id, NULL);

    return 0;
}