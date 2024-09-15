import subprocess 
import shutil
import os

def build_everything():
    subprocess.call(["python3", "build.py", "--no-run"]) 

def clean_tests():
    print("Cleaning old tests")

    for file in os.listdir("./target/tests"):
        if file.endswith(".test.res.txt"):
            os.remove("./target/tests/" + file)

def run_tests():
    print("Running tests")

    for file in os.listdir("./target/tests"):
        if file.endswith(".test"):
            print("    Running " + file)

            f = open("./target/tests/" + file + ".res.txt", "w")
            subprocess.call(["./target/tests/" + file], stdout=f) 
            f.close()


build_everything()
clean_tests()

run_tests()
