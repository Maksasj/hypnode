## Install jflex

```sh
sudo apt install jflex
```

## Build and run parser

```sh
./run.sh
```

```sh
# make scanner
jflex ./jflex/scanner.jflex

mv ./jflex/Yylex.java ./

# make parser
java -cp ./lib/java-cup-11.jar java_cup.Main ./cup/parser.cup

javac -cp ./lib/java-cup-11.jar *.java
java -cp .:./lib/java-cup-11.jar Main fib.hn
```