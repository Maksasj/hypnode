#include "test.h"
#include "queue.h"

int main() {
    Queue queue;

    create_queue(&queue, 1024);

    TEST_EQUAL(queue_size(&queue), 0);
    TEST_EQUAL(queue_capacity(&queue), 1024);

    free_queue(&queue);

    return 0;
}
