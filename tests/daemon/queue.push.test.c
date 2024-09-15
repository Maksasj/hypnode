#include "test.h"
#include "queue.h"

int main() {
 Queue queue;

    create_queue(&queue, 1024);

    for(int i = 0; i < 100; ++i) {
        int* item = (int*) malloc(sizeof(int));
        *item = i;
        queue_push(&queue, item);
    }

    unsigned int size = queue_size(&queue);

    TEST_EQUAL(100, size);

    free_queue(&queue);

    return 0;
}
