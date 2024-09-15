#include <stdio.h>
#include <stdlib.h>

#include "queue.h"

int main() {
    Queue queue;

    create_queue(&queue, 1024);

    for(int i = 0; i < 100; ++i) {
        int* item = (int*) malloc(sizeof(int));
        *item = i;
        queue_push(&queue, item);
    }

    printf("%d %d\n", 100, queue_size(&queue));

    for(int i = 0; i < 20; ++i) {
        void* item = queue_pop(&queue);

        free(item);
    }

    printf("%d %d\n", 80, queue_size(&queue));
    printf("%d %d\n", 0, queue_empty(&queue));

    for(int i = 0; i < 20; ++i) {
        void* item = queue_pop(&queue);

        free(item);
    }

    printf("%d %d\n", 60, queue_size(&queue));
    printf("%d %d\n", 0, queue_empty(&queue));

    for(int i = 0; i < 60; ++i) {
        queue_pop(&queue);
    }

    printf("%d %d\n", 0, queue_size(&queue));
    printf("%d %d\n", 1, queue_empty(&queue));

    free_queue_content(&queue);
    free_queue(&queue);
}
