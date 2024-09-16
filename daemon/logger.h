#ifndef LOGGER_H
#define LOGGER_H

#include <stdio.h>
#include <stdarg.h>

typedef enum LogLevel {
    INFO,
    WARNING,
    ERROR
} LogLevel;

char* stringify_log_level(LogLevel logLevel);

void daemon_log(LogLevel logLevel, const char *format, ...);

#define DAEMON_LOG(logLevel, ...) daemon_log(logLevel, __VA_ARGS__)

#endif