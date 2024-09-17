import subprocess 
import shutil
import os
import sys

# make scanner
os.system("jflex ./compiler/jflex/scanner.jflex")

os.system("mv ./compiler/jflex/Yylex.java ./compiler")

# make parser
os.system("java -cp ./compiler/lib/java-cup-11.jar java_cup.Main -destdir compiler ./compiler/cup/parser.cup")

os.system("javac -cp ./compiler/lib/java-cup-11.jar ./compiler/*.java")
os.system("java -cp ./compiler:./compiler/lib/java-cup-11.jar Main ./examples/fib.hn")
