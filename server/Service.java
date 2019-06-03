package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Service implements Runnable {
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;

	public Service(Socket pSocket) {
		this.socket = pSocket;
		try {// flux d'entrée
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// flux de sortie
			this.pw = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				String request = br.readLine();
				System.out.println("Service : message reçu : " + request);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
