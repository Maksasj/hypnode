# JFlex scanner

This project is built using maven. So, in order to run the tests you should have `mvn` and `java` installed on your computer.

## Build and run tests

### Install maven dependencies

```sh
mvn install
```

### Run tests

```sh
mvn test
```

## Main files

- `src/main/resources/hypnode.flex`: JFlex scanner generator reads this file and generates the `hypnode` language scanner based on it.

- `src/test/resources/**`: This directory contains `hypnode` language example programs that are being scanned by generated scanner. In `input.hn` you can see the source code and in `output.json` the expected array of lexemes.

- `src/test/java/gen/TestScanner.java`: org.junit.jupiter tests entrypoint.
