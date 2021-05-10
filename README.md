OS Activity 3: Process Implementation, Client-Side Codebase
=========================

Simple implementation of client-server communication for the client-side interface. When run in the command line, it accepts two arguments, `address` and `port`. When not specified, it will default to `127.0.0.1:6013`. Runs indefinitely until the `exit` keyword is received from the server.

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

## Definition of Terms
`address`: The IP address of the server
`port`: The open port of the server 

## The Process Rundown
When the program is first run, a handshake is made to confirm the connection to server in the form of the current time and date. If the server is not running or the address and port is not found, the program will refuse to connect.

The displayed time and date of the handshake is from the server itself and *not* from the client. It will also display the `address` and `port` of the server it is currently connected to. The sent user message will then be echoed back to the user, and will also be displayed with a timestamp (this time it is from the client).

A successfully connected program output will more or less look like so:

```
[INFO] (Client connected to 127.0.0.1:6013)

Current time and date is Mon May 10 13:25:09 PST 2021


Message:
Hello World!

> Mon May 10 13:26:42 PST 2021 | Reply from server: Hello World!
```

The connection and, subsequently, the program will be halted if the user enters the word 'exit' without the quotes.

## Checklist
- [x] Semi-finalize code
- [ ] Finalize JavaDocs and README
- [ ] Finalize all