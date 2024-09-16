#include "tcp_interface.h"

void* tcp_interface_thread_fun(void* vargp) {
    DAEMON_LOG(INFO, "Started TCP interface thread");

    unsigned int port = 8170;
    int l_socket;
    int c_socket;

    struct sockaddr_in servaddr;
    struct sockaddr_in clientaddr;

    socklen_t clientaddrlen;
 
    int s_len;
    char buffer[65536];

    if ((l_socket = socket(AF_INET, SOCK_STREAM,0))< 0){
        DAEMON_LOG(ERROR, "TCP interface thread: cannot create listening socket");
        exit(1);
    }
    
    memset(&servaddr,0, sizeof(servaddr));
    servaddr.sin_family = AF_INET;

    servaddr.sin_addr.s_addr = htonl(INADDR_ANY); 
    servaddr.sin_port = htons(port);
    
    if (bind(l_socket, (struct sockaddr *)&servaddr,sizeof(servaddr)) < 0) {
        DAEMON_LOG(ERROR, "TCP interface thread: bind listening socket");
        exit(1);
    }

    if (listen(l_socket, 5) < 0){
        DAEMON_LOG(ERROR, "TCP interface thread: error in listen()");
        exit(1);
    }

    DAEMON_LOG(INFO, "TCP interface listens on port: %d", port);

    for(;;) {
        memset(&clientaddr,0, sizeof(clientaddr));
        memset(&buffer,0,sizeof(buffer));

        clientaddrlen = sizeof(struct sockaddr);
        if ((c_socket = accept(l_socket,(struct sockaddr*)&clientaddr,&clientaddrlen)) < 0){
            DAEMON_LOG(ERROR, "TCP interface thread: error occured accepting connection");
            exit(1);
        }

        s_len = recv(c_socket,buffer,sizeof(buffer),0);

        printf("IP: %s Received: %d bytes, Message %s\n", inet_ntoa(clientaddr.sin_addr), s_len, buffer + 4);
        close(c_socket);
    }

    return NULL;
}