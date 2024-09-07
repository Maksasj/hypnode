import subprocess 
import os

def build_runtime():
    print("Building runtime")

    subprocess.call(["gcc", "./runtime/main.c"]) 

def build_std_native():
    print("Building native std modules")

    modules = [ "printf", "arithmetic" ]

    for module in modules:
        subprocess.call(["gcc", "-c", "-Wall", "-fpic", "./std/native/" + module + ".c"]) 
        subprocess.call(["gcc", "-shared", "-o", module + ".so", module + ".o"]) 

def clean_build():
    print("Cleaning build")

    for file in os.listdir("./"):
        if file.endswith(".o"):
            os.remove(file)

def run_runtime():
    print("Running runtime")

    subprocess.call(["./a.out", "./printf.so"]) 

build_runtime()
build_std_native()

clean_build()

run_runtime()