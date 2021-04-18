# Java Client for Horse MQ Server


To do

* SSL Engine implementation for secure connections
* Now, it supports send and receive as HorseMessage. There is no object based push, send, publish methods. We need to register models with consumer interfaces.
* 
* HorseClient does not support reconnecting after a period of time and it does not any event such as connected, disconnected, message received etc.
* There is builder for HorseClient. Consumer registration and setting fluent api options are not possible without any builder.
* Direct Operator methods (send direct message, send response etc)
* Router operator methods (create/delete router, get routers, get router bindings, add/remove binding, publish to router)
* Queue Operator, consuming from pull state queue methods.
