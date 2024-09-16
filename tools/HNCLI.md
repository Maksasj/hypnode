# hncli

```sh
USAGE: ./target/tools/hncli <ip> <port> <module_path>
```

```sh
# Run node via hypnode CLI tool
./target/tools/hncli 127.0.0.1 8170 ./target/std/native/printf.so
```

```txt
Hncli
Usage: hncli <command> [<args>]

These are hncli commands:

start and terminate nodes iside daemon
    run <ip> <port> <module_path>
    kill <ip> <port> <module_id>

view running nodes
    list <ip> <port>
```