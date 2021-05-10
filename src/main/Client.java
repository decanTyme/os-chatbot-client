package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Simple implementation of client-server communication for the client-side
 * interface. When run in the command line, it accepts two arguments,
 * {@code address} and {@code port}. When not specified, it will default to
 * {@code 127.0.0.1:6013}. Runs indefinitely until the {@code exit} keyword is
 * received from the server.
 * 
 * <p>
 * Example console execute: <blockquote>
 * 
 * <pre>
 * java -jar chatbot-client.jar 127.0.0.1 5500
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Or subsequently without arguments: <blockquote>
 * 
 * <pre>
 * java -jar chatbot-client.jar
 * </pre>
 * 
 * </blockquote>
 * <p>
 * 
 * @author Danry Ague
 * @version 2.3.7
 */
public class Client {

    /**
     * Used to indicate/prefix error logs. Must be concatenated with the actual
     * error message.
     */
    final static String PREF_ERR = "[ERROR] ";

    /**
     * Used to indicate/prefix informational logs. Must be concatenated with the
     * actual information message.
     */
    final static String PREF_INF = "[INFO] ";

    /**
     * Used for developer convenience.
     */
    static PrintStream stdStream = System.out;

    /**
     * Clears the command line interface.
     */
    static void clearScreen() {
	System.out.print("\033[H\033[2J");
	System.out.flush();
    }

    /**
     * Uses the {@link java.net.Socket Socket} class in order to connect to the
     * server.
     * 
     * @param uri  the address of the live server.
     * @param port the port number of the live server.
     * 
     * @throws IOException if an IO error occurs
     */
    static void connect(String uri, int port) throws IOException {

	/** Declarations **/
	Socket sock = new Socket(uri, port);
	BufferedReader received = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	PrintWriter send = new PrintWriter(sock.getOutputStream(), true);
	BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
	String reply;

	/* Output current client instance URI and port when successfully connected */
	stdStream.println(PREF_INF + "(Client connected to " + uri + ":" + port + ")" + "\n");

	while ((reply = received.readLine()) != null) {

	    /* Initial handshake message from server */
	    stdStream.println("Current time and date is " + reply + "\n");

	    while (!reply.equals("exit")) {

		/* Send to server the user input */
		stdStream.println("\n" + "Message: ");
		String message = consoleReader.readLine();

		/** Check if message is empty **/
		if (!message.isBlank()) {
		    send.println(message);
		} else {
		    stdStream.println("Message cannot be empty!" + "\n");
		    continue;
		}

		/* Received from server */
		reply = received.readLine();

		/** Check if user wants to exit **/
		if (reply.equals("exit")) {
		    stdStream.println("\n\n" + "Thank you for using AIChatbot! Exiting...");
		} else {

		    /* Otherwise, relay the server reply to user */
		    stdStream.println("\n" + "> " + generateTimestamp() + " | Reply from server: " + reply);
		}

	    }
	    break;
	}

	/** Properly close the connection **/
	sock.close();
    }

    /**
     * Generates a timestamp to be used.
     * 
     * @return a generated {@code String} timestamp
     */
    static String generateTimestamp() {
	return new java.util.Date().toString();
    }

    /**
     * Main entry point of the program. Clears the command line interface first,
     * then checks if arguments are present before connecting to the server.
     * 
     * @param args (optional) address and port
     */
    public static void main(String[] args) {
	clearScreen();

	int port;
	String address;

	if (args.length != 0) {
	    address = args[0];
	    port = Integer.parseInt(args[1]);
	} else {
	    /** Defaults, if there no console arguments **/
	    address = "127.0.0.1";
	    port = 6013;
	}

	// TODO: Handle args and exceptions
	try {
	    connect(address, port);
	} catch (IOException e) {
	    stdStream.println(PREF_ERR + e.getMessage() + ".");
	}
	stdStream.close();
    }
}
