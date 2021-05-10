OS Activity 3: Process Implementation, Client-Side Codebase
=========================

Simple implementation of client-server communication for the client-side interface. When run in the command line,<sup>[1](#fn-jarfile)</sup> it accepts two arguments, `address` and `port`. When not specified, it will default to `127.0.0.1` and `6013` respectively. Runs indefinitely until the `exit` keyword is received back from the server.

Example console execute: 

<blockquote>
<pre>
java -jar chatbot-client.jar 127.0.0.1 5500
</pre>
</blockquote>

Or subsequently without arguments: 

<blockquote>
<pre>
java -jar chatbot-client.jar
</pre>
</blockquote>

**Note:** Needs a Java Runtime Environment installed. You may download from the [Java Official Website](https://www.java.com/en/download/).

## Definition of Terms
`address`: The IP address of the server

`port`: The open port of the server 

## Description
This is a simple Java 'client' that runs in the command line that can connect to Java servers. It can only send and receive messages to and from the server.

## Process Rundown
When the program is first run, a handshake is made to confirm the connection to server in the form of the current date and time. If the server is not running or the address and/or port is not found, the program will refuse to run and an error message is displayed.

The displayed time and date of the handshake is from the server itself and *not* from the client. It will also display the `address` and `port` of the server it is currently connected to. The sent user message<sup>[2](#fn-ml)</sup> will then be echoed back to the user and will also be displayed with a timestamp (this time it is from the client). The program will refuse to send a message that is empty.

A successfully connected program output will more or less look like so:

```
[INFO] (Client connected to 127.0.0.1:6013)

Current time and date is Mon May 10 13:25:09 PST 2021


Message:
Hello World!

> Mon May 10 13:26:42 PST 2021 | Reply from server: Hello World!
```

The connection and, subsequently, the program will terminate if the user enters the word 'exit' without the quotes.

## Checklist
- [x] Semi-finalize code
- [ ] Confirm with the instructor if the process is correct
- [ ] Finalize JavaDocs and README
- [ ] Finalize all

### Footnote
1. <a name="fn-jarfile"></a> You need to compile and export the code as a `jar` file first before you can execute it. 
2. <a name="fn-ml"></a> Does not support multi-line messages.
