# Concept

## Syntax example
```python
@native type u8
@native type u16
@native type u32
@native type u64

@native type i8
@native type i16
@native type i32
@native type i64

@native type f32
@native type f64

@native type byte
@native type string

# We support arrays
type ByteArray = byte[]
type StringArray = string[]
type Matrix = f32[][]

# Complex data type declaration
type Student = {
    name: string
    mark: u32
    scores: u32[]
}

# Custom node declaration
node NiceMark {
    # Node I/O declaration
    input sti: Student
    output sto: Student

    # Rest of the logic

    sti.mark <- 10
    sto <- sti
}

node University {
    input sti: Student
    output sto: Student

    node m: NiceMark
    m.sti <- sti
    sto <- m.sto
}

```