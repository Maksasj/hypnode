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