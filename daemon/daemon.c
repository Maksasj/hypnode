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

#include "hypnode.h"

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
        fprintf(stderr,"ERROR #2: cannot create listening socket.\n");
        exit(1);
    }
    
    memset(&servaddr,0, sizeof(servaddr));
    servaddr.sin_family = AF_INET; // nurodomas protokolas (IP)

    servaddr.sin_addr.s_addr = htonl(INADDR_ANY); 
    servaddr.sin_port = htons(port); // nurodomas portas
    
    if (bind (l_socket, (struct sockaddr *)&servaddr,sizeof(servaddr))<0){
        fprintf(stderr,"ERROR #3: bind listening socket.\n");
        exit(1);
    }

    if (listen(l_socket, 5) <0){
        fprintf(stderr,"ERROR #4: error in listen().\n");
        exit(1);
    }

    for(;;) {
        memset(&clientaddr,0, sizeof(clientaddr));
        memset(&buffer,0,sizeof(buffer));

        clientaddrlen = sizeof(struct sockaddr);
        if ((c_socket = accept(l_socket,
            (struct sockaddr*)&clientaddr,&clientaddrlen))<0){
            fprintf(stderr,"ERROR #5: error occured accepting connection.\n");
            exit(1);
        }

        s_len = recv(c_socket,buffer,sizeof(buffer),0);

        // r_len = send(c_socket,buffer,s_len,0);

        printf("IP: %s Received: %d bytes, Message %s\n", 
            inet_ntoa(clientaddr.sin_addr), 
            s_len,
            buffer);
        
        close(c_socket);
    }

    return NULL;
}

int main(int argc, char* argv[]) {
    if(argc < 2) {
        fprintf(stderr, "Usage: hne [FILE]");
        return 1;
    }

    const char* file_name = argv[1];
    void *module = dlopen(file_name, RTLD_NOW);

    if(module == NULL) {
        fprintf(stderr, "ERROR: could not load %s: %s", file_name, dlerror());
        return 1;
    }

    _meta_export_nodes _meta_export_nodes = dlsym(module, "_meta_export_nodes");

    if(_meta_export_nodes == NULL) {
        fprintf(stderr, "ERROR: could not find entrypoint symbol in %s: %s", file_name, dlerror());
        return 1;
    }

    _meta_export_node* export_node = (*_meta_export_nodes)();

    if(export_node == NULL) {
        fprintf(stderr, "ERROR: module does not export any nodes");
        return 1;
    }

    if(load_node(file_name, module, export_node[0]) == 1) {
        fprintf(stderr, "ERROR: failed to load node from module %s", file_name);
        return 1;
    }


    pthread_t tcp_interface_thread_id;
    pthread_create(&tcp_interface_thread_id, NULL, tcp_interface_thread_fun, NULL);

    while(1) {

    }

    pthread_join(tcp_interface_thread_id, NULL);

    return 0;
}