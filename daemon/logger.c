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

void get_time(char* time_str) {
    time_t t ;
    struct tm *tmp ;
    time( &t );
     
    tmp = localtime( &t );
     
    strftime(time_str, 50, "%H:%M:%S", tmp);
}

void daemon_log(LogLevel logLevel, const char *format, ...) {
    va_list args;
    va_start(args, format);

    char time_str[50];
    get_time(time_str);

    printf("\x1B[90m[%s %s%s\x1B[90m] ", time_str, unix_log_level_color(logLevel), stringify_log_level(logLevel));
    
    printf("\033[0m");
    vprintf(format, args);

    printf("\n");

    va_end(args);
}
