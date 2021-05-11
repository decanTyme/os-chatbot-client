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
 * {@code address} and {@code port}. When not specified, the program will ask
 * for input while running, otherwise it will default to {@code 127.0.0.1:6013}.
 * Runs indefinitely until the {@code exit} keyword is received from the server.
 * 
 * <p>
 * Example console execute: <blockquote>
 * 
 * <pre>
 * java -jar chatbot-client.jar --address 127.0.0.1 -p 5500
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
 * @version 2.6.4-alpha
 */
public class Client {

    /**
     * Default address.
     */
    final static String DEF_LOCALHOST = "127.0.0.1";

    /**
     * Default port.
     */
    final static String DEF_PORT = "6013";

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
	stdStream.print("\033[H\033[2J");
	stdStream.flush();
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

	clearScreen();

	/** Output current client instance URI and port when successfully connected **/
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
     * Provides a delay in milliseconds. Useful for making sure that the user sees
     * an output before it is cleared.
     * 
     * @param ms desired delay in milliseconds
     */
    static void delay(int ms) {
	try {
	    Thread.sleep(ms);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
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
     * Used to get input from the user.
     * 
     * @param inputType The type of parameter to be acquired from the user
     * @return The acquired input from the user
     * @throws IOException
     */
    static String getInput(String inputType) {
	BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
	String acquiredParam = null;

	switch (inputType) {
	case "address":
	    stdStream.println("Address of the server: (leave blank for default)");
	    try {
		acquiredParam = consoleReader.readLine();
		if (acquiredParam.isBlank()) {
		    acquiredParam = DEF_LOCALHOST;
		} else if (!isValidAddress(acquiredParam)) {
		    stdStream.println("Invalid address, using defaults." + "\n");
		    acquiredParam = DEF_LOCALHOST;
		}
	    } catch (IOException e) {
		stdStream.println("There was an error in receiving input data.");
		e.printStackTrace();
	    }
	    break;
	case "port":
	    stdStream.println("Port of the server: (leave blank for default)");
	    try {
		acquiredParam = consoleReader.readLine();
		if (acquiredParam.isBlank()) {
		    acquiredParam = DEF_PORT;
		}
	    } catch (IOException e) {
		stdStream.println("There was an error in receiving input data.");
		e.printStackTrace();
	    }
	    break;
	}

	return acquiredParam;
    }

    /**
     * Checks for address validity.
     * 
     * @param address the address to be checked
     * @return {@code true} if address is valid
     */
    static boolean isValidAddress(String address) {
	boolean flag = false;
	int count = 0;
	for (int i = 0; i < address.length(); i++) {
	    if (address.charAt(i) == '.') {
		count++;
	    }
	}
	if (count == 3)
	    flag = true;
	return flag;
    }

    /**
     * Main entry point of the program. Clears the command line interface first,
     * then checks if arguments are present before continuing.
     * 
     * @param args (optional) address and port
     */
    public static void main(String[] args) {
	clearScreen();

	/** Defaults **/
	int port = Integer.parseInt(DEF_PORT);
	String address = DEF_LOCALHOST;

	/** Check for arguments **/
	if (args.length != 0) {
	    for (int i = 0; i < args.length; i++) {
		switch (args[i]) {
		case "--address":
		case "-a":
		    if (isValidAddress(args[i + 1])) {
			address = args[i + 1];
		    } else {
			stdStream.println("Invalid address, using defaults.");
			delay(1000);
		    }
		    i++;
		    break;
		case "--port":
		case "-p":
		    try {
			port = Integer.parseInt(args[i + 1]);
		    } catch (NumberFormatException e) {
			stdStream.println("Invalid port, using defaults.");
			delay(1000);
		    }
		    i++;
		    break;
		default:
		    stdStream.println("Unknown command: " + args[i]);
		    i++;
		}
	    }
	} else {

	    /* No arguments, ask for user input */
	    address = getInput("address");
	    try {
		port = Integer.parseInt(getInput("port"));
	    } catch (NumberFormatException e) {
		stdStream.println("Invalid port, using defaults." + "\n");
	    }
	}

	try {
	    connect(address, port);
	} catch (IOException e) {
	    stdStream.println(PREF_ERR + "@" + address + ":" + port + " | " + e.getMessage().toUpperCase() + ".");
	}
	stdStream.close();
    }
}
