#ifndef LOGGER_H
#define LOGGER_H

#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>
#include <time.h> 

#define KNRM  "\x1B[0m"
#define KRED  "\x1B[31m"
#define KGRN  "\x1B[32m"
#define KYEL  "\x1B[33m"
#define KBLU  "\x1B[34m"
#define KMAG  "\x1B[35m"
#define KCYN  "\x1B[36m"
#define KWHT  "\x1B[37m"
#define KGRA  "\x1B[90m"

typedef enum LogLevel {
    INFO,
    WARNING,
    ERROR,
    EXPERIMENTAL
} LogLevel;

const char* unix_log_level_color(LogLevel logLevel);
const char* stringify_log_level(LogLevel logLevel);

void daemon_log(LogLevel logLevel, const char *format, ...);

#define DAEMON_LOG(logLevel, ...) daemon_log(logLevel, __VA_ARGS__)

#endif