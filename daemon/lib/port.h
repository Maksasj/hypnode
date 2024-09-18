#ifndef PORT_H
#define PORT_H

#include "type_info.h"

typedef struct {
    const char* port_name;

    void* value;
    _type_info value_type_info;
} _port_struct;

#endif