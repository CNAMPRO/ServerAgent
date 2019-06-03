package main;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import system.CBase;
import system.CEnvironement; 
import java.util.ArrayList;

public class CServer
{ 

	private int m_nbClient =0 ; //nb Client Connecter
	static CEnvironement mEnv;
	private static final int BASE_COUNT = 1;
    private static final int AGENTS_COUNT = 30;
    private static final int NOURRITURE_COUNT = 2;
	private ArrayList<PrintWriter> m_TabClient;
	public static final Integer _PORT = 40000;
	
	public CServer() {
		this.m_TabClient = new ArrayList<PrintWriter>();
		
	}
	public static void main(String args[]) 
	{ 

		CServer server = new CServer();
		
		try
		{
			
			new CommandesServ(server);
			ServerSocket ss = null;
			try
			{
				ss = new ServerSocket(_PORT);
				mEnv = CEnvironement.getInstance();
		        mEnv.init(0, 0, 768, 768, NOURRITURE_COUNT);
		       
				printBienvenue(_PORT);
				
			} catch (UnknownHostException e) 
			{
			    e.printStackTrace();
			} catch (IOException e) 
			{
			   e.printStackTrace();
			}
			
			while(true)
			{
				new ServerThread(ss.accept(),server);
			}
		}
		catch(Exception e) {System.out.println(e);}
	}
	
	static private void printBienvenue(Integer port)
	{
		System.out.println("Ouai Bienvenue");
	}
	
	synchronized public void sendAll(String message,String sLast)
	{
	
		for (PrintWriter printWriter : m_TabClient) {
			printWriter.print(message + sLast);
			printWriter.flush();
		}

	}
	
	
	  synchronized public void delClient(int i)
	  {
		  
		  m_nbClient--; // del client
		 
	    if (m_TabClient.get(i) != null) 
	    {
	    	m_TabClient.remove(i);
	    }
	  }

	  synchronized public int addClient(PrintWriter out)
	  {
		  m_nbClient++; 
		  m_TabClient.add(out); 
	    return m_TabClient.size()-1; // on retourne le numéro du client ajouté (size-1)
	  }

	  synchronized public int getNbClients()
	  {
	    return m_nbClient; // retourne le nombre de clients connectés
	  }	
} 
