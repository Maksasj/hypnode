import subprocess 
import shutil
import os

def create_folders():
    print("Creating folders")

    folders = [
        "target",
        "target/tools",
        "target/std",
        "target/std/native"
    ]

    for folder in folders:
        if os.path.isdir(folder) == False:
            os.mkdir(folder)

def build_daemon():
    print("Building daemon")

    subprocess.call(["gcc", "./daemon/main.c", "-o", "hne"]) 

    # move daemon executable to target folder
    shutil.move("hne", "target/hne")

def build_std_native():
    print("Building native std modules")

    modules = [ 
        "printf", 
        "arithmetic", 
        "experimental" 
    ]

    for module in modules: 
        print("    Building " + module + " module")
        
        subprocess.call(["gcc", "-c", "-Wall", "-fpic", "./std/native/" + module + ".c"]) 
        subprocess.call(["gcc", "-shared", "-o", module + ".so", module + ".o"]) 

    # Copy all native modules to target folder
    for file in os.listdir("./"):
        if file.endswith(".so"):
            shutil.move(file, 'target/std/native/' + file)

def build_tools():
    print("Building tools")

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

clean_build()

run_daemon()