## Type

Primitive types

- Signed numerical types: **i8 i16 i32 i64**
- Unsigned numerical types: **u8 u16 u32 u64**
- Floating point: **f32 f64**
- Other: **string bool**

Special types

- any

Compound types

```lua
type MyStruct = {
    a: i32
    b: i32
};
```
-с 
Type operations
```lua
type Type1 = { ... };
type Type2 = { ... };
type Type3 = { ... };

type Union = Type1 | Type2 | Type3;
 
type Type11 = any as Type1;
 
```

Type C interface

```c
 
struct _type_info_struct {
    const char* _type_name;
}
```


```lua
type Type1 = { 
    a: i32
};

type Type2 = { 
    a: string
};

type Type3 = { 
    a: bool
    b: i32 
};

type Union = Type1 | Type2 | Type3;
```

```c
enum _type_category {
    Primitive;
    Compound;
    Union;
}

// Maybe there could be not type_name, rather _type_info
struct _compound_type_field {
    const char* field_name;
    const char* type_name;
    // Todo offset
}

// Maybe there could be not type_name, rather _type_info
struct _union_type_field {
    const char* type_name;
}

struct _type_info {
    const char* type_name;
    _type_type category;

    _compound_type_field* compound_fields;
    _union_type_field* union_fields;
}

struct Type1 { 
    signed int a;
};

_type_info _Type1_type_info = (_type_info) {
    .type_name = "Type1",
    .category = Compound

    .compound_fields = {
        (_compound_type_field) {
            .field_name = "a",
            .type_name = "i32"
        }
    }
    ._union_type_field = NULL
}

struct Type2 { 
    char* a;
};

_type_info _Type2_type_info = (_type_info) {
    .type_name = "Type2",
    .category = Compound,

    .compound_fields = {
        (_compound_type_field) {
            .field_name = "a",
            .type_name = "string"
        }
    },
    .union_fields = NULL
}

struct Type3 = { 
    unsigned char a;
    signed int b;    
};

_type_info _Type3_type_info = (_type_info) {
    .type_name = "Type3",
    .category = Compound,

    .compound_fields = {
        (_compound_type_field) {
            .field_name = "a",
            .type_name = "bool"
        },
        (_compound_type_field) {
            .field_name = "b",
            .type_name = "i32"
        }
    },
    .union_fields = NULL
}

union Union {
    Type1 _Type1; 
    Type2 _Type2;
    Type3 _Type3;
};

_type_info _Type1_type_info = (_type_info) {
    .type_name = "Union",
    .category = Union

    .compound_fields = NULL
    .union_fields = {
        (_union_type_field) {
            .type_name = "Type1"
        },
        (_union_type_field) {
            .type_name = "Type2"
        },
        (_union_type_field) {
            .type_name = "Type3"
        }
    }
}

```

```c

struct Packet {
    void* value;
    _type_info type_info;
};

```

### Node

Required native nodes

Arithmetic Operators
- `+` Addition
- `-` Subtraction
- `*` Multiplication
- `/` Division with Modulus

Comparison Operators
- `==` Equal to
- `>` Greater than
- `<` Less than

Logical Operators
- `&&` And
- `||` Or
- `!` Not

```lua
@native node + (a: i32, b: i32) => (c: i32);
@native node - (a: i32, b: i32) => (c: i32);
@native node * (a: i32, b: i32) => (c: i32);
@native node / (a: i32, b: i32) => (c: i32);

@native node == (a: bool, b: bool) => (c: bool);
@native node > (a: bool, b: bool) => (c: bool);
@native node < (a: bool, b: bool) => (c: bool);

@native node && (a: bool, b: bool) => (c: bool);
@native node || (a: bool, b: bool) => (c: bool);
@native node ! (a: bool) => (c: bool);
```

```@native``` means that node callback is provided by daemon

Node C interface

```c
struct _node_name_struct {
    
    void (*_callback)(void* self);
};

void _node_name_callback(void* _self) {
    struct _node_name_struct* self = _self;

    // Node logic
}

// Node life-cycle functions
void* _node_name_init();
void _node_name_dispose(void* _node);

// Trigger function
void _node_name_trigger(void* _node);
```
### Port

```c

Port C interface

struct _port_struct {
    const char* _type_name;
}
```

### Data packets

Data packet C interface

```c
struct _data_packet {
    // Version
    // Source adress
    // Destination adress
    // Payload length
    // Payload
    // Others
}

```

### Attributes

### Meta
```c
struct _meta_export_node {
    char* _init;
    char* _dispose;
    char* _trigger;
};

static struct _meta_export_node _export_symbols[] = {
    (struct _meta_export_node) {
        ._init = "_node_name_init",
        ._dispose = "_node_name_dispose",
        ._trigger = "_node_name_trigger" 
    }
};

struct _meta_export_node* _meta_export_nodes() {
    return _export_symbols;    
}
```

one file -> one module
basically ...

```lua
# -- arithmetic.hn

# -- implementation of this node is written in C
@native node sum (a: i32, b: i32) => (c: i32);
```

```lua
@include "module_name" # -- includes module source code

@import "node_name" # -- pulls node implementation from daemon environment
@export "node_name" # -- makes node visible to daemon environment

@native # -- node implementation callback should be linked separately 
```

### ```@import```
With @import attribute we can defined node as usuall. But implementation callback should be provided by daemon.

```lua
node anlog (message: string) => () = { ... }

# -- Since this implementation is imported from native C code, we still need to specify node interface, to be able to compile this module
node mylog (message: string) => () = @import "std_experimental_log"

@export "entrypoint"
node main = {
    let l: mylog;
    l.message <- "Hello world !\n";
}
```