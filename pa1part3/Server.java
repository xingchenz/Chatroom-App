

import java.io.*;
import java.net.*;
import java.util.*;



/*
 * A chat server that delivers public and private messages.
 */
public class Server {
    
    // Create a socket for the server 
    private static ServerSocket serverSocket = null;
    // Create a socket for the user 
    private static Socket userSocket = null;
    // Maximum number of users 
    private static int maxUsersCount = 5;
    // An array of threads for users
    private static ArrayList <userThread> threads = new ArrayList <userThread>(maxUsersCount); 

    public static void main(String args[]) {
        
        // The default port number.
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
         * Create a user socket for each connection and pass it to a new user
         * thread.
         */
		
        while (true) {
        	
        	try {
					userSocket = serverSocket.accept();
					
					if (threads.size()<maxUsersCount){
						userThread t = new userThread(userSocket, threads);
						
						threads.add(t);  //Update arrayList "threads"
						t.start();
						
					}else{
						PrintStream message = new PrintStream (userSocket.getOutputStream());
						message.println("System is too busy at the moment.");
						
					}
						
				}catch (IOException e) {
				e.printStackTrace();
			}
        	

        }
    }
}

/*
 * Threads
 */
class userThread extends Thread {
    
    private String userName = null;
    private BufferedReader input_stream = null;
    private PrintStream output_stream = null;
    
    private Socket userSocket = null;
    private final ArrayList <userThread> threads;   //final
    												
   
    public userThread(Socket userSocket, ArrayList <userThread> threads) {
        this.userSocket = userSocket;
        this.threads = threads;  
        
    }
    
    public void run() {

	/*
	 * Create input and output streams for this client, and start conversation.
	 */

        try {
			input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream())); // Receive from Client
			output_stream = new PrintStream(userSocket.getOutputStream()); //Send to Client
			
			output_stream.println("Enter your username: ");
			
			//Check if the user's name starts with @ and make sure it doesn't
			while(true){
				String stmp = input_stream.readLine().trim();
				if (stmp.startsWith("@")){
					output_stream.println("Please enter an username that doesn't start with @.");
				}else {
					userName = stmp;
					break;
				}
			}
						
			
		
			output_stream.println("Welcome " + userName + " to our chat room.");
			output_stream.println("To leave enter LogOut in a new line.");
			output_stream.println("To talk to a specific user, please type as the following format: @username+space+message.");
			
			
			//Broadcast to other users(threads) when a new user entered
			synchronized (this){
				for(Iterator itr = threads.iterator(); itr.hasNext();){
					userThread tmp = (userThread) itr.next();
					if(tmp != this && tmp != null){
						tmp.output_stream.println("***A new user " + userName + " entered the chat room!!!***");
					}
				}
				
			}
			
			//Chat in process
			while(true){
				String s1; 
				s1 = input_stream.readLine().trim(); //Read user's input
				
				//Broadcast to other users when a user is leaving 
				if(s1.equals("LogOut")){
					output_stream.println("### Bye " + userName + " ###");
					threads.remove(this); //Remove current thread from arrayList "thread"
					synchronized (this){
						for(Iterator itr = threads.iterator(); itr.hasNext();){
							userThread tmp = (userThread) itr.next();
							if(tmp != null){   
								tmp.output_stream.println("***The user " + userName+ " is leaving the chat room!!!***");
							}
							
						}
					}
					break;
				 //Unicast messages to one specific user	
				}else if(s1.startsWith("@")){
					String userName_tmp = null;
					String userMessage_tmp = null;
					
					//Parsing: Find the first whitespace in the string, 
					//         separating the user name and the message.
					for(int i=0; i< s1.length();i++){
						if(s1.charAt(i) == ' '){
							userName_tmp = s1.substring(1, i);
							userMessage_tmp = s1.substring(i+1, s1.length());
							break;
						}
					}
					
					//Determine if the user name currently exits
					synchronized (this){
						boolean flag = false;
						for(Iterator itr = threads.iterator(); itr.hasNext();){
							userThread tmp = (userThread) itr.next();
							if(tmp.userName.equals(userName_tmp)){   
								tmp.output_stream.println("<" + userName + "> " + userMessage_tmp); //Message the user
								output_stream.println("<" + userName + "> " + userMessage_tmp); //Echo message back
								flag = true; //Set flag as true if we found a user name from other threads.
								break;
							}							
							
						}
						//After the iteration, if no user name is found, then flag will still be false.
						if(!flag){ 
							output_stream.println("Username does not exit!");   //Tell the user that the user name is not valid
						}
					}			
					
					
					
				}
				else{
					//Broadcast messages to other users
					synchronized (this){
						for(Iterator itr = threads.iterator(); itr.hasNext();){
							userThread tmp = (userThread) itr.next();
							if(tmp != null){   // Including yourself
								tmp.output_stream.println("<" + userName + "> " + s1);
							}
							
						}
					}
				}
			}
			
			input_stream.close();
			output_stream.close();
			userSocket.close();
			
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
}



