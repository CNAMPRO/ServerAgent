
package main;

import java.net.*;
import system.CBase;
import system.CEnvironement;
import java.io.*;

class ServerThread implements Runnable {
	private Thread m_t;
	private Socket m_s;
	private PrintWriter m_out; // flux de sortie
	private BufferedReader m_in; // flux d'entrée
	private CServer m_CServer2; // classe principale
	private int m_numClient = 0; // numéro de client géré par ce thread
	
	
	private OutputStream output = null;
	private InputStream input = null;
	private ObjectOutputStream outputTram = null;
	private ObjectInputStream inputTram = null;
	

	ServerThread(Socket s, CServer blablaServ) throws IOException {
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

		m_t = new Thread(this);
		m_t.start();
	}
	public void run() {
		String message = "";

		System.out.println("Un nouveau client s'est connecte, no " + m_numClient);
		try {
			while(true) {
				sendObjectCEnvironement(m_CServer2.mEnv);
				CBase base = readObject();
				if(base != null) {
					System.out.println();
					m_CServer2.mEnv.mBaseList.add(base);
				}
			}
			
		} catch (Exception e) {
		}
	}

	private CBase readObject() throws IOException, ClassNotFoundException {
		CBase base = null;
		if(inputTram.available() > 0) {
			base = (CBase) inputTram.readObject();
		}
		
		return base;
	}

	
	public void sendObjectCEnvironement(CEnvironement object) throws IOException {
		outputTram.writeObject(object);
		outputTram.flush();
	}

}