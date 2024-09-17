#include <stdlib.h>
#include <stdio.h>
#include <dlfcn.h>
#include <pthread.h>

#define DAEMON_BUILD
#include "lib/hypnode.h"

#include "environment.h"

#include "tcp_interface.h"
#include "node_worker.h"

int main(int argc, char* argv[]) {
    DAEMON_LOG(INFO, "Started daemon");

    Environment* env = new_environment();

    pthread_t tcp_interface_thread_id;
    pthread_create(&tcp_interface_thread_id, NULL, tcp_interface_thread_fun, env);

    pthread_t node_worker_thread_id;
    pthread_create(&node_worker_thread_id, NULL, node_worker_thread_fun, env);

    while(1) {
        // Running
    }

    pthread_join(tcp_interface_thread_id, NULL);
    pthread_join(node_worker_thread_id, NULL);

    free_environment(env);

    return 0;
}