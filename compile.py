import subprocess 
import shutil
import os
import sys

# make scanner
os.system("jflex ./hn2c/jflex/scanner.jflex")

os.system("mv ./hn2c/jflex/Yylex.java ./hn2c")

# make parser
os.system("java -cp ./hn2c/lib/java-cup-11.jar java_cup.Main -destdir hn2c ./hn2c/cup/parser.cup")

os.system("javac -cp ./hn2c/lib/java-cup-11.jar ./hn2c/*.java")
os.system("java -cp ./hn2c:./hn2c/lib/java-cup-11.jar Main ./examples/fib.hn")
