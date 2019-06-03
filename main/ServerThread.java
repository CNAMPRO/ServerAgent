
package main;

import java.net.*;
import system.CBase;
import system.CEnvironement;
import java.io.*;

class ServerThread implements Runnable {
	private Thread m_t;
	private Socket m_s;
	private PrintWriter m_out; // flux de sortie
	private BufferedReader m_in; // flux d'entree
	private CServer m_CServer2; // classe principale
	private int m_numClient = 0; // numero de client que le thread gere
	
	
	private OutputStream output = null;
	private InputStream input = null;
	private ObjectOutputStream outputTram = null;
	private ObjectInputStream inputTram = null;
	

	ServerThread(Socket s, CServer blablaServ) throws IOException, ClassNotFoundException {
		m_CServer2 = blablaServ;
		m_s = s;
		System.out.println(s);
		output = m_s.getOutputStream();
		input = m_s.getInputStream();
		outputTram = new ObjectOutputStream(output);
		inputTram = new ObjectInputStream(input);
		try {
			m_out = new PrintWriter(m_s.getOutputStream());
			m_in = new BufferedReader(new InputStreamReader(m_s.getInputStream()));
			m_numClient = blablaServ.addClient(m_out);
		} catch (IOException e) {
		}
		sendObjectCEnvironement(m_CServer2.mEnv);
		CBase base = (CBase) inputTram.readObject();
		System.out.println(base);
		if(base != null) {
			System.out.println("ajout base");
			m_CServer2.mEnv.mBaseList.add(base);
			System.out.println(m_CServer2.mEnv.mBaseList.size());
		}
		m_t = new Thread(this);
		m_t.start();
	}
	public void run() {
		String message = "";

		System.out.println("Un nouveau client s'est connecte, no " + m_numClient);
		try {
			
			while(true) {
				m_CServer2.mEnv.update();
				outputTram.writeObject(m_CServer2.mEnv);
				outputTram.flush();
				
			}
			
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private CEnvironement readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		// in.defaultReadObject();
		CEnvironement env = (CEnvironement) in.readObject();
		return env;
	}
	
	private CBase readBase() throws IOException, ClassNotFoundException {
		CBase base = (CBase) inputTram.readObject();
		return base;
	}

	/**
	 * 
	 * @param object
	 * @param outputStream
	 * @throws IOException
	 */
	public void sendObject(CBase object, OutputStream outputStream) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		System.out.println("Sending messages to the ServerSocket");
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		System.out.println("Closing socket and terminating program.");
	}
	/**
	 * 
	 * @param object
	 * @throws IOException
	 */
	private void readObject() throws IOException, ClassNotFoundException {
		if(inputTram.available() > 0) {
			System.out.print("ajout base");
			CBase base = (CBase) inputTram.readObject();
			m_CServer2.mEnv.mBaseList.add(base);
			System.out.println(m_CServer2.mEnv.mBaseList.size());
		}
	}

	
	public void sendObjectCEnvironement(CEnvironement object) throws IOException {
		
		outputTram.writeObject(object);
		outputTram.flush();
	}

}