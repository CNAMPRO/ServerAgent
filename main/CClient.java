package main;
// A Java program for a Client 
import java.net.*; 
import java.io.*; 

public class CClient 
{ 
	// init 
	private Socket socket		 = null; 
	private OutputStream input = null; 
	private ObjectOutputStream outputTram	 = null; 

	public CClient(String address, int port) 
	{ 
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("Connected"); 

			input = socket.getOutputStream();
			outputTram = new ObjectOutputStream(input);
		} 
		catch(UnknownHostException u) 
		{ 
			System.out.println(u); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
		
	} 
	
	public void close() throws IOException {
		input.close(); 
		outputTram.close(); 
		socket.close(); 
		System.exit(0);
	}

	//public static void main(String args[]) 
	//{ 
		//ClientDuDu client = new ClientDuDu("localhost", 40000); 
	//} 
} 
