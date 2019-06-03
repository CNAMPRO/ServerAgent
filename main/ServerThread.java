
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
			 
			sendObjectCEnvironement(m_CServer2.mEnv,outputTram);
			
			char charCur[] = new char[1];
			while (m_in.read(charCur, 0, 1) != -1) {

				if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r') // "\u0000 " == null
					message += charCur[0];
				else if (!message.equalsIgnoreCase("")) {
					if (charCur[0] == '\u0000')

						m_CServer2.sendAll(message, "" + charCur[0]);
					else
						m_CServer2.sendAll(message, "");
					message = "";
				}
			}
		} catch (Exception e) {
		} finally // deconnexion du client
		{
			try {
				// on indique la deconnexion du client
				System.out.println("Le client no " + m_numClient + " s'est deconnecte");
				m_CServer2.delClient(m_numClient); // on supprime le client de la liste
				m_s.close(); // fermeture du socke
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	private CEnvironement readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		// in.defaultReadObject();
		CEnvironement env = (CEnvironement) in.readObject();
		return env;
	}

	public void sendObject(CBase object, OutputStream outputStream) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		System.out.println("Sending messages to the ServerSocket");
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		System.out.println("Closing socket and terminating program.");
	}
	
	public void sendObjectCEnvironement(CEnvironement object, OutputStream outputStream) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		System.out.println("Sending messages to the ServerSocket");
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		System.out.println("Closing socket and terminating program.");
	}
}