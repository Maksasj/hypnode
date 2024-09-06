# Compiler and flags
CC = gcc
CFLAGS = -Wall -g

# Target executable
TARGET = hne

# Source files
SRCS = runtime/main.c

# Object files (replace .c with .o in source file names)
OBJS = $(SRCS:.c=.o)

# Default rule to build the target
$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJS)

# Rule to build object files
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Clean rule to remove generated files
clean:
	rm -f $(OBJS) $(TARGET)
