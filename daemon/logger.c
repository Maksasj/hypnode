#include "logger.h"

char* stringify_log_level(LogLevel logLevel) {
    switch (logLevel) {
        case INFO:       { return "INFO"; }
        case WARNING:    { return "WARNING"; }
        case ERROR:      { return "ERROR"; }
    }

    return NULL;
}

void daemon_log(LogLevel logLevel, const char *format, ...) {
    va_list args;
    va_start(args, format);

    printf("[DAEMON][%s] ", stringify_log_level(logLevel));
    vprintf(format, args);
    printf("\n");

    va_end(args);
}
