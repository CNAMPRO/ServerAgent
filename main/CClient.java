package main;

import java.net.*;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import gui.CMainPanel;
import system.CBase;
import system.CEnvironement;
import java.io.*;

public class CClient {
	// init
	private Socket socket = null;
	private OutputStream output = null;
	private InputStream input = null;
	private ObjectOutputStream outputTram = null;
	private ObjectInputStream inputTram = null;
	public CEnvironement mEnv;
	private CMainPanel panel;
	
	private static final int TIMER_DELAY = 0;
    private static final int TIMER_PERIOD = 10;

	public CClient(String address, int port, CMainPanel panel) throws IOException, ClassNotFoundException {
		try {
			panel = panel;
			socket = new Socket(address, port);
			System.out.println("Connected");
			System.out.println(socket);

			// get the output stream from the socket
			output = socket.getOutputStream();
			input = socket.getInputStream();
			// create an object output stream from the output stream so we can send an
			// object through it
			outputTram = new ObjectOutputStream(output);
			inputTram = new ObjectInputStream(input);

		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}
		
		getEnvironement();
		
		//createBase();
		panel.launch();
	}

	public void close() throws IOException {
		output.close();
		input.close();
		outputTram.close();
		inputTram.close();
		socket.close();
		System.exit(0);
	}

	private CEnvironement readObject() throws IOException, ClassNotFoundException {
		//in.defaultReadObject();
		CEnvironement env = (CEnvironement) inputTram.readObject();
		return env;
	}

	public void sendObject(CBase object) throws IOException {
		System.out.println("Sending messages to the ServerSocket");
		outputTram.writeObject(object);
		outputTram.flush();
		System.out.println("Closing socket and terminating program.");
	}

	public void createBase() throws IOException {
		Random r = new Random();
		double x = 0 + r.nextInt(768 - 0);
		double y = 0 + r.nextInt(768 - 0);

		CBase mBase = new CBase(x, y, 10, java.awt.Color.GREEN, 10);
		sendObject(mBase);
		
		
	}
	
	public void getEnvironement() throws ClassNotFoundException, IOException {
		
		 mEnv = readObject();
		 System.out.println(mEnv.mBaseList.size());
		 CEnvironement.ImportEnvironement(mEnv);
	}
}
