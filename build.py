import subprocess 
import shutil
import os
import sys

def create_folders():
    print("Creating folders")

    folders = [
        # General
        "target",
        "target/tools",
        "target/std",
        "target/std/native",

        # Tests
        "target/tests"
    ]

    for folder in folders:
        if os.path.isdir(folder) == False:
            os.mkdir(folder)

def build_daemon():
    print("Building daemon 🤖")

    os.system("gcc daemon/lib/*.c daemon/*.c -o target/hne")  

def build_tests():
    print("Building tests 🧪")

    print("    Deleted old tests")
    for file in os.listdir("./target/tests"):
        if file.endswith(".test"):
            os.remove("./target/tests/" + file)

    print("    Building daemon tests")
    for file in os.listdir("./tests/daemon"):
        if file.endswith(".test.c"):
            length = len(file)
            file = file[0:length - 7]

            os.system("gcc daemon/lib/*.c  tests/daemon/" + file + ".test.c -I daemon/lib -o target/tests/" + file + ".test")  

def build_std_native():
    print("Building native std modules 📜")

    modules = [ 
        "printf", 
        "arithmetic", 
        "experimental" 
    ]

    for module in modules: 
        print("    Building " + module + " module")

        subprocess.call(["gcc", "-c", "-Wall", "-fpic", "./std/native/" + module + ".hn.c"]) 
        subprocess.call(["gcc", "-shared", "-o", module + ".so", module + ".hn.o"]) 

    # Copy all native modules to target folder
    for file in os.listdir("./"):
        if file.endswith(".so"):
            shutil.move(file, 'target/std/native/' + file)

def build_tools():
    print("Building tools 🛠")

    subprocess.call(["go", "build", "./tools/hncli.go"]) 

    # move hncli executable to target folder
    shutil.move("hncli", "target/tools/hncli")

def clean_build():
    print("Cleaning build")

    for file in os.listdir("./"):
        if file.endswith(".o"):
            os.remove(file)

def run_daemon():
    print("Running daemon")

    subprocess.call(["./target/hne", "./target/std/native/printf.so"]) 

create_folders()

build_daemon()
build_std_native()
build_tools()

build_tests()

clean_build()

if len(sys.argv) <= 1:
    run_daemon()