package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Vector;

import system.CBase;
import system.CEnvironement; 

public class CServer
{ 

	private int m_nbClient =0 ; //nb Client Connecter
	private Vector m_TabClient = new Vector(); //TabClient
	static CEnvironement mEnv;
	private static final int BASE_COUNT = 1;
    private static final int AGENTS_COUNT = 30;
    private static final int NOURRITURE_COUNT = 2;
	
	public static void main(String args[]) 
	{ 
		CServer server = new CServer();
		
		try
		{
			Integer port = 40000;
			
			new CommandesServ(server);
			ServerSocket ss = null;
			try
			{
				ss = new ServerSocket(port);
				mEnv = CEnvironement.getInstance();
		        mEnv.init(BASE_COUNT, AGENTS_COUNT, 768, 768, NOURRITURE_COUNT);
		       
				printBienvenue(port);
				
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
		PrintWriter out;
		for(int i = 0; i<m_TabClient.size();i++)
		{
			out = (PrintWriter) m_TabClient.elementAt(i);
			if(out != null)
			{
				out.print(message + sLast);
				out.flush();
			}
		}
	}
	
	
	  synchronized public void delClient(int i)
	  {
		  m_nbClient--; // del client
	    if (m_TabClient.elementAt(i) != null) 
	    {
	    	m_TabClient.removeElementAt(i);
	    }
	  }

	  synchronized public int addClient(PrintWriter out)
	  {
		  m_nbClient++; 
		  m_TabClient.addElement(out); 
	    return m_TabClient.size()-1; // on retourne le numéro du client ajouté (size-1)
	  }

	  synchronized public int getNbClients()
	  {
	    return m_nbClient; // retourne le nombre de clients connectés
	  }	
} 
