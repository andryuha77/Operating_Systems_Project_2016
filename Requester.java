package user;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message,choice,ip;
 	Scanner user_input, user_input2;
	Requester()
	{
		user_input = new Scanner(System.in);
		user_input2 = new Scanner(System.in);
	}
	void run()
	{
			//1. creating a socket to connect to the server
			//local IP 127.0.0.1
			// Change server IP 35.167.134.104 to connect to running server.
			System.out.print("\nTo what IP would you like to connect?: \n"
					+ "Press(1) 127.0.0.1 \n"
					+ "Press(2) 35.167.134.104 \n ");
			choice = user_input2.next( );
			switch (choice) {
			case "1":
				ip = "127.0.0.1";	
				break;
			case "2":									
				ip = "35.167.134.104";	
				break;																		
			default:
				System.out.println("Wrong choose!");
			}
	
		try{	
			requestSocket = new Socket(ip, 2004);
			System.out.println("Connected to host in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server
			do{
				try{
					message = (String)in.readObject();
					System.out.println(message);
					message = user_input.nextLine();
					sendMessage(message);
					
					if(message.compareTo("q")==0)
					{
						sendMessage("Thank You!");			
					}				
				}
				catch(ClassNotFoundException classNot){
					System.err.println("data received in unknown format");
				}
			}while(!message.equals("Thank You!"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
}