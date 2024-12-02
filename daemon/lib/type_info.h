#ifndef TYPE_INFO_H
#define TYPE_INFO_H

typedef enum {
    Primitive,
    Compound,
    Union,
    Array
} _type_category;

// Maybe there could be not type_name, rather _type_info
typedef struct {
    const char* field_name;
    const char* type_name;
    unsigned int offset;
    // Todo offset
} _compound_type_field;

// Maybe there could be not type_name, rather _type_info
typedef struct {
    const char* type_name;
} _union_type_field;

typedef struct {
    const char* type_name;
    _type_category category;

    _compound_type_field* compound_fields;
    _union_type_field* union_fields;
} _type_info;

#endif