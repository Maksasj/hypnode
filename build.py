import subprocess 

def build_runtime():
    print("Building runtime")

    subprocess.call(["gcc", "./runtime/main.c"]) 

def build_std_native():
    print("Building native std modules")

    subprocess.call(["gcc", "-c", "-Wall", "-fpic", "./std/native/printf.c"]) 
    subprocess.call(["gcc", "-shared", "-o", "printf.so", "printf.o"]) 

def run_runtime():
    print("Running runtime")

    subprocess.call("./a.out") 

build_runtime()
build_std_native()

run_runtime()