#include "node_worker.h"

typedef struct {

} Packet;

void* node_worker_thread_fun(void* vargp) {
    Environment* env = (Environment*) vargp;
    Queue* packet_queue = &env->packet_queue;

    DAEMON_LOG(INFO, "Started node worker thread");

    while(1) {
        while (!queue_empty(packet_queue)) {
            Packet* packet = (Packet*) queue_pop(packet_queue);

            DAEMON_LOG(INFO, "Processing packet");
        }
    }   

    return NULL;
}
