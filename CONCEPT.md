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
node MyNode () => ();

# Node declaration, but implemetation 
# is provided by native runtime
@native 
node printf (message: string) => (res: ResultT);

# Node declaration, but without implemetation 
@native
node sum (a: f32, b: f32) => (res: f32);

# Native/Export node
@native
@export("Σ")
node Σ(i: f32[]) => (o: f32);

# Hello world example
@export("entrypoint")
node main(argv: string[], argc: f32) => () {
    # Child nodes
    using [ p: printf ]

    p.message : "Hello world !"
}

# Fibonacci sequence
@export("entrypoint")
node main(argv: string[], argc: i33) => () {
    using [ print: printf ] # i/o
    using [ s: sum, d: delay ] # operations

    s.a : 0, s.res
    s.b : 1, d.res
    d.i : s.res

    print.message : s.res
}
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