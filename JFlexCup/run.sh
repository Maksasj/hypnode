jflex scanner.jflex
java -cp java-cup-11.jar java_cup.Main parser.cup
javac -cp java-cup-11.jar *.java
java -cp .:java-cup-11.jar Main fib.hn
