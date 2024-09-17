#ifndef TCP_INTERFACE
#define TCP_INTERFACE

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "logger.h"
#include "environment.h"

void* tcp_interface_thread_fun(void* vargp);

#endif