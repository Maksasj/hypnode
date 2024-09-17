#include "test.h"
#include "queue.h"

int main() {
    Queue queue;

    create_queue(&queue, 10);

    for(int i = 0; i < 1000000; ++i) {
        int* item = (int*) malloc(sizeof(int));
        *item = i;
        queue_push(&queue, item);
    }

    TEST_EQUAL(queue_capacity(&queue), 1310720);

    for(int i = 999999; i >= 0; --i) {
        int* item = (int*) queue_pop(&queue);

        TEST_EQUAL(*item, i);
    }

    TEST_EQUAL(queue_capacity(&queue), 20);

    free_queue_content(&queue);
    free_queue(&queue);
}
