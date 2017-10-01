// Part I - Server

import java.io.*;
import java.net.*;

public class Server{

	private static ServerSocket serverSocket = null;
	private static Socket userSocket = null; 
	private static BufferedReader input_stream = null;
	private static DataOutputStream dos = null;
		
	public static void main(String [] args) {

		String content = null;
		int portNumber = 8000;
		if(args.length < 1){
			System.out.println("Usage: java Server \n" + "Using port number: " + portNumber + "\n");

		} else{
			portNumber = Integer.valueOf(args[0]).intValue();
			System.out.println("Usage: java Server \n" + "Using port number: " + portNumber + "\n");
		}

		// Open a server socket on portNumber
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		/*
         * Create a user socket for accepted connection 
         */
		while(true){
			try{
				// Waiting for connection
				userSocket = serverSocket.accept(); 

				System.out.println("Connection Established!");
	
				//Set up server's input stream
				input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));	
				//Set up server's output stream
				dos = new DataOutputStream(userSocket.getOutputStream());
	
				//Read one line of String from server's input stream
				content = input_stream.readLine().trim();  
	
				dos.writeUTF(content + "\n"); //\n is necessary
				System.out.println("From a client: " + content);
				
				/*				
				input_stream.close();
				dos.close();
				serverSocket.close();
				userSocket.close(); 
				*/
				
			} catch (IOException e) {
				System.out.println(e);
			}
		}
			
		
		


	}


}
	
