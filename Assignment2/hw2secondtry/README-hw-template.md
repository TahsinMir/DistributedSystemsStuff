# Homework 2

* Author: Tahsin Imtiaz
* Class: CS555 [Distributed Systems] Section #001


## Overview

This is the HW2 where we implement the single threaded TimeServer in such a way that it limits the number of connections to be no more than once in an interval of five seconds.

## Building the code

I have added the only changed file which is the TimeServer.java file. The rest is already in the repo. To build the client, run the following command:

javac TimeClient.java

To build the server, run the following command:

javac TimeServer.java

to run the server, run,

java TimeServer

And finally, to run a client, run:

java TimeClient localhost 5005

## Testing

I have tested the program with three terminals where one terminal is used for the server and the other two is used for the clients.
In the case of my program. If two clients request time at the same time, one get the time, the other one has to wait 5 seconds to get the time as per requirement of the hw.
In other words, once a client is connected, the program limits the number of connections and does not accept any other connection for the next 5 seconds.

## Reflection

The output mentioned in the homework statement has only a 2 second delay in the first 2 client executions. This should not happen. The right program should be able to
accpet one connection for all the cases. In my case, if the server gets connection from the first client, it first accepts the connection, runs the timer and then waits.
However, for the next connections, it first waits and then accepts the connection. This solves this minor synchronization problem.

