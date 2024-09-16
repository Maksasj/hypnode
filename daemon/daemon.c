#include <stdlib.h>
#include <stdio.h>
#include <dlfcn.h>
#include <pthread.h>

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define DAEMON_BUILD
#include "lib/hypnode.h"

#include "logger.h"

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

void* tcp_interface_thread_fun(void* vargp) {
    DAEMON_LOG(INFO, "Started tcp interface thread");

    unsigned int port = 8170;
    int l_socket;
    int c_socket;

    struct sockaddr_in servaddr;
    struct sockaddr_in clientaddr;

    socklen_t clientaddrlen;
 
    int s_len;
    // int r_len;
    char buffer[65536];

    if ((l_socket = socket(AF_INET, SOCK_STREAM,0))< 0){
        DAEMON_LOG(ERROR, "tcp interface thread: cannot create listening socket");
        exit(1);
    }
    
    memset(&servaddr,0, sizeof(servaddr));
    servaddr.sin_family = AF_INET; // nurodomas protokolas (IP)

    servaddr.sin_addr.s_addr = htonl(INADDR_ANY); 
    servaddr.sin_port = htons(port); // nurodomas portas
    
    if (bind (l_socket, (struct sockaddr *)&servaddr,sizeof(servaddr))<0){
        DAEMON_LOG(ERROR, "tcp interface thread: bind listening socket");
        exit(1);
    }

    if (listen(l_socket, 5) <0){
        DAEMON_LOG(ERROR, "tcp interface thread: error in listen()");
        exit(1);
    }

    DAEMON_LOG(INFO, "TCP interface listens on port: %d", port);

    for(;;) {
        memset(&clientaddr,0, sizeof(clientaddr));
        memset(&buffer,0,sizeof(buffer));

        clientaddrlen = sizeof(struct sockaddr);
        if ((c_socket = accept(l_socket,(struct sockaddr*)&clientaddr,&clientaddrlen)) < 0){
            DAEMON_LOG(ERROR, "tcp interface thread: error occured accepting connection");
            exit(1);
        }

        s_len = recv(c_socket,buffer,sizeof(buffer),0);

        // r_len = send(c_socket,buffer,s_len,0);

        printf("IP: %s Received: %d bytes, Message %s\n", inet_ntoa(clientaddr.sin_addr), s_len, buffer);
        close(c_socket);
    }

    return NULL;
}

void* node_worker_thread_fun(void* vargp) {
    DAEMON_LOG(INFO, "Started node worker thread");

    while(1) {
        
    }

    return NULL;
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