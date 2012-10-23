import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;


public class Server {
	public static void main(String argv[]) {
		try {
			int port = Integer.parseInt(argv[0]);
			System.setProperty("javax.net.ssl.keyStore", "Server/keystore.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "keystorePW");
			
			System.out.println("Server setting up port "+port+", press 'ENTER' to close connection...");
			ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
		    ServerSocket servSock = ssocketFactory.createServerSocket(port);
		    
			while(true) {
				Socket sock = servSock.accept(); // Listening socket 
				System.out.println("Receiving something!");
				// Receive name from client
				InputStream fileIn = sock.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(fileIn));
				String s = in.readLine();
				System.out.println(s);
			}
		}
		catch(IOException e) {
			System.out.print(e.getMessage());
		}
	}
}
