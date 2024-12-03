#ifndef PRIMITIVE_TYPES_H
#define PRIMITIVE_TYPES_H

#include "type_info.h"

// Native types
typedef char* string;
static _type_info _string_type_info = (_type_info) {
    .type_name = "string",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef signed int i32;
static _type_info _i32_type_info = (_type_info) {
    .type_name = "i32",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef void* any;
static _type_info _any_type_info = (_type_info) {
    .type_name = "any",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

#endif