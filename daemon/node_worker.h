#ifndef NODE_INTERFACE
#define NODE_INTERFACE

#include "logger.h"
#include "environment.h"
#include "lib/packet.h"

void* node_worker_thread_fun(void* vargp);

#endif