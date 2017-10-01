
import java.io.*;
import java.net.*;



public class Client extends Thread {
    
    // The user socket
    private static Socket userSocket = null;
    // The output stream
    private static PrintStream output_stream = null;
    // The input stream
    private static BufferedReader input_stream = null;
    
    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    
    public static void main(String[] args) {
        
        // The default port.
        int portNumber = 8000;
        // The default host.
        String host = "localhost";

        if (args.length < 2) {
            System.out.println("Usage: java User <host> <portNumber>\n"
                             + "Now using host=" + host + ", portNumber=" + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }
        
	/*
     * Open a socket on a given host and port. Open input and output streams.
     */

        
        try {
			userSocket = new Socket(host, portNumber);
			//Read from Keyboard
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			
			//Read from Server
			input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
			
			//Output to Server
			output_stream = new PrintStream(userSocket.getOutputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	/*
     * If everything has been initialized then create a listening thread to 
	 * read from the server. 
	 * Also send any user’s message to server until user logs out.
     */

	
        Client thread1 = new Client();
        thread1.start(); //Listening thread starts
        
        
        
        while(!closed){
        	try {
				String stringKeyboard = inputLine.readLine().trim();
				output_stream.println(stringKeyboard);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // Close streams and socket
        try {
			inputLine.close();
			input_stream.close();
			output_stream.close();
			userSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
 
    public void run() {
        /*
         * Keep on reading from the socket till we receive “### Bye …” from the
         * server. Once we received that then we want to break and close the connection.
         */
        
        while(true){
    		try {
    			String stringServer = input_stream.readLine().trim(); //Read one line of string from Server
    			
				if(stringServer.startsWith("### Bye")) {
					System.out.println(stringServer);  //print out "Bye" message
					break;
				}
				else{
					System.out.println(stringServer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	  	
	    	closed = true; //Stop the while loop in main thread
	
			
		}
    	

    }