# javacup scanner

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

- `src/main/resources/hypnode.cup`: JFlex parser generator definition.

- `src/test/resources/parser_tests/**`: This directory contains 2 directories: accepted and rejected. These directories contain input files that are accepted by parser (has no syntax errors) or rejected (has some syntax errors)
