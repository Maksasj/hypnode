#include <stdio.h>
#include <stdlib.h>

#include "queue.h"

int main() {
    Queue queue;

    create_queue(&queue, 1);

    for(int i = 0; i < 2; ++i) {
        int* item = (int*) malloc(sizeof(int));
        *item = i;
        queue_push(&queue, item);
    }

    printf("%d %d\n", queue_capacity(&queue), 2);

    for(int i = 1; i >= 0; --i) {
        int* item = (int*) queue_pop(&queue);

        printf("%d %d\n", *item, i);
    }

    printf("%d %d\n", queue_capacity(&queue), 2);

    free_queue_content(&queue);
    free_queue(&queue);
}