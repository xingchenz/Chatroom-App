// Part I - Client

import java.io.*;
import java.net.*;

public class Client{

	private static Socket userSocket = null;

	// Client's input stream from server
	private static BufferedReader input_stream = null;

	// Client's input stream from keyboard
	private static BufferedReader input_line = null;

	// client's output stream
	private static DataOutputStream dos = null;

	private static String skeyboard = null;
	private static String backFromServer = null;

	public static void main(String [] args) {

	   /*
		*  Set up default port number and host name
		*/
		int portNumber = 8000;
		String host = "localhost";
      	
        
        if (args.length < 2) {
            System.out.println("Usage: java User <host> <portNumber>\n"
                             + "Now using host= " + host + ", portNumber = " + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        
        try {
    		// YOUR CODE
        	userSocket = new Socket(host, portNumber);
        	System.out.println("Connection established!" + "\n");
                  
            // Set up input stream by keyboard
            input_line = new BufferedReader(new InputStreamReader(System.in));

            // Set up input stream by Server
            input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));

            // Set up output stream to Server
            dos = new DataOutputStream(userSocket.getOutputStream());
                        
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + host);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to the host "
                                       + host);
            }
        
        /*
         * If everything has been initialized then we want to send message to the
         * socket we have opened a connection to on the port portNumber.
         * When we receive the echo, print it out.
         */
         try {
  
          /*
           * Close the output stream, close the input stream, close the socket.
           */
        	skeyboard = input_line.readLine();
         	dos.writeUTF(skeyboard + "\n"); // 
         	
         	backFromServer = input_stream.readLine().trim();
         	System.out.println("From server: " + backFromServer);
           
         	input_stream.close();
            dos.close();
            userSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
              }
    
    }
        

 }





