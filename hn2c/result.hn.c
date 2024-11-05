#include <stdlib.h>
#include "native.h"

struct { 
    i32 a;
    i32 b;
};

meta type information

struct _node_printf_struct {
    // Ports
    // Callback
    // void (*_implementation)(void* self);
};

// Node life-cycle functions
void* _node_printf_init();
void _node_printf_dispose(void* _node);
void _node_printf_trigger(void* _node);
void _node_printf_implementation(void* _self) 

struct _node_printf_struct {
    // Ports
    // Callback
    // void (*_implementation)(void* self);
};

// Node life-cycle functions
void* _node_printf_init();
void _node_printf_dispose(void* _node);
void _node_printf_trigger(void* _node);
void _node_printf_implementation(void* _self) 

/* ================ meta ================ */

unsigned long _node_import_symbols_count = 0;
struct {
    char* symbol_name;
    char* implementation_symbol
} _node_import_symbols[] = {

};

unsigned long _node_export_symbols_count = 1;
_node_export_symbol _node_export_symbols[] = {
    (_node_export_symbol) {
        ._name = "std_experimental_log",

        ._init = "_node_log_init",
        ._dispose = "_node_log_dispose",
        ._trigger = "_node_log_trigger",

        ._implementation = "_node_log_implementation"
    },
};

/* ====================================== */
