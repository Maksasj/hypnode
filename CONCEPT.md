# Concept

## Syntax example
```python
# Basic types ...
@native type f32
@native type char
@native type string

# We support arrays
type StringArray = string[]
type Matrix = f32[][]

# Complex data type declaration
type Student = {
    name: string
    mark: u32
    scores: u32[]
}

# Node declartion
node MyNode () => ()

# Node declaration, but implemetation 
# is provided by native runtime
@native 
node printf (message: string) => (res: ResultT)

# Node declaration, but without implemetation 
@native
node sum (a: f32, b: f32) => (res: f32)

# Native/Export node
@native
@export("Σ")
node Σ(i: f32[]) => (o: f32)

# Hello world example
@export("entrypoint")
node main(argv: string[], argc: f32) => () {
    # Child nodes
    using [ p: printf ]

    p.message : "Hello world !"
}

# Fibonacci sequence
type FibState = {
    a: i32
    b: i32
}

# Fib node accepts previous state and 
# outputs new state + prints result to stdo
node fib(in_state: FibState) => (on_state: FibState) {
    using [ s: sum ]
    s.a : state.a
    s.b : state.b

    on_state.a : in_state.b 
    on_state.b : s.c 

    using [ p: print ] # i/o
    s.message : s.c
}

@export("entrypoint")
node main(argv: string[], argc: i32) => () {
    using [ f: fib ] # operations

    f.state : f.state # Node connected to it self
}

# Type things
@native type i64
@native type i32
@native type i16
@native type i8

@native type u64
@native type u32
@native type u16
@native type u8

@native type char
@native type string

type iany = i64 | i32 | i16 | i8
type uany = u64 | u32 | u16 | u8
type number = iany | uany | f32 | f64

type printable = string | number

@native
node printf (message: string, fmt: printable[]) => ()

type mod = node _ (in: any) => (out: any)

@native 
node to_upper (in: char) => (out: char)
# to_upper node have same interface as mod type
```

### Thought
We need some sort of attribute, that will allow us to mark when node should amit new signal

### Thought
Actually native nodes could be directly linked with C dll

## Attributes
```python
@native 
# notifies compliler that node/type implementation should be provided by runtime

@export("name")
# exposes node by explicit name to runtime
```