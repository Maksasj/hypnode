#include "logger.h"

const char* unix_log_level_color(LogLevel logLevel) {
    switch (logLevel) {
        case INFO:       { return KGRN; }
        case WARNING:    { return KYEL; }
        case ERROR:      { return KRED; }
    }

    return NULL;
}

const char* stringify_log_level(LogLevel logLevel) {
    switch (logLevel) {
        case INFO:       { return "INF"; }
        case WARNING:    { return "WAR"; }
        case ERROR:      { return "ERR"; }
    }

    return NULL;
}

void daemon_log(LogLevel logLevel, const char *format, ...) {
    va_list args;
    va_start(args, format);

    time_t t ;
    struct tm *tmp ;
    char MY_TIME[50];
    time( &t );
     
    tmp = localtime( &t );
     
    strftime(MY_TIME, sizeof(MY_TIME), "%H:%M:%S", tmp);

    printf("\x1B[90m[%s %s%s\x1B[90m] ", MY_TIME, unix_log_level_color(logLevel), stringify_log_level(logLevel));
    
    printf("\033[0m");
    vprintf(format, args);

    printf("\n");

    va_end(args);
}
