import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends ServerSocket implements Runnable {
	private int port;
	private List<Service> serviceListe ;

	public Server(int port) throws IOException {
		super(port);
		this.port = port;
		serviceListe = new ArrayList<Service>();
		new Thread(this).start();
	}

	@Override
	public void run() {

		while (true) {
			try {
				System.out.println("Serveur : attente d'un client ..");
				Socket socket = accept(); // Attente d'un client (bloquant) , retour un socket (ip+port)

				System.out.println("Serveur : lancement d'un service ..");
				//serviceListe.add(new Service(socket));
				
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

}
