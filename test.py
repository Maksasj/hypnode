import subprocess 
import shutil
import os
import sys

def build_everything():
    subprocess.call(["python3", "build.py", "--no-run"]) 

def clean_tests():
    print("Cleaning tests")

    for file in os.listdir("./target/tests"):
        if file.endswith(".test.res.txt"):
            os.remove("./target/tests/" + file)

def run_tests():
    print("Running tests")

    total_success = 0
    total_failed = 0

    for file in os.listdir("./target/tests"):
        if file.endswith(".test"):
            print("    Running '" + file + "' test")

            res_file = "./target/tests/" + file + ".res.txt"

            f = open(res_file, "w")
            subprocess.call(["./target/tests/" + file], stdout=f) 
            f.close()

            success_count = 0
            fail_count = 0
            total_count = 0

            with open(res_file) as f:
                lines = f.readlines()

                line_number = 0
                for line in lines:
                    # Split the line into two numbers
                    num1, num2 = map(int, line.split())
                    
                    # Compare the numbers
                    if num1 == num2:
                        success_count += 1
                    else:
                        print(f"        ⚠️ Mismatch on line {line_number}: {num1} is not equal to {num2}")
                        fail_count += 1

                    total_count += 1
                    line_number += 1

            total_success += success_count
            total_failed += fail_count

            if fail_count != 0:
                print(f"        ❌ Test failed {success_count}/{total_count}")
            else:
                print(f"        ✅ Test passed {success_count}/{total_count}")

    return (total_success, total_failed)

build_everything()
clean_tests()

(total_success, total_failed) = run_tests()

if total_failed != 0:
    sys.exit('❌ Tests failed')