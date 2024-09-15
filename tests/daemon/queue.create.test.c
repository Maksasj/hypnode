#include <stdio.h>
#include <stdlib.h>

#include "queue.h"

int main() {
    Queue queue;

    create_queue(&queue, 1024);

    printf("%d %d\n", queue_size(&queue), 0);
    printf("%d %d\n", queue_capacity(&queue), 1024);

    free_queue(&queue);

    return 0;
}
