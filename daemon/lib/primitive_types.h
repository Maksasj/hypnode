#ifndef PRIMITIVE_TYPES_H
#define PRIMITIVE_TYPES_H

#include "type_info.h"

// Native types
typedef unsigned int u8;
static _type_info _u8_type_info = (_type_info) {
    .type_name = "u8",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef unsigned int u16;
static _type_info _u16_type_info = (_type_info) {
    .type_name = "u16",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef unsigned int u32;
static _type_info _u32_type_info = (_type_info) {
    .type_name = "u32",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef unsigned int u64;
static _type_info _u64_type_info = (_type_info) {
    .type_name = "u64",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef signed int i8;
static _type_info _i8_type_info = (_type_info) {
    .type_name = "i8",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef signed int i16;
static _type_info _i16_type_info = (_type_info) {
    .type_name = "i16",
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

typedef signed int i64;
static _type_info _i64_type_info = (_type_info) {
    .type_name = "i64",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef float f32;
static _type_info _f32_type_info = (_type_info) {
    .type_name = "i32",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef double f64;
static _type_info _f64_type_info = (_type_info) {
    .type_name = "f64",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef char* string;
static _type_info _string_type_info = (_type_info) {
    .type_name = "string",
    .category = Primitive,
    .compound_fields = 0,
    .union_fields = 0
};

typedef unsigned char bool;
static _type_info _bool_type_info = (_type_info) {
    .type_name = "bool",
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