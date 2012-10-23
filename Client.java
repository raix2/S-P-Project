import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

import javax.net.ssl.*;


public class Client {
	static public void main(String argv[]) {
		try {
		    // Create the client socket
		    int port = Integer.parseInt(argv[0]);
		    String hostname = "localhost";
		    System.setProperty("javax.net.ssl.trustStore", "Client/cTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "keystorePW");
			// Get server certificate
		  	FileInputStream is = new FileInputStream(System.getProperty("javax.net.ssl.trustStore"));

		    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		    keystore.load(is, "keystorePW".toCharArray());

		    // Get certificate
		    java.security.cert.Certificate cert = keystore.getCertificate("server");
		    
		    SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();
		    SSLSocket socket = (SSLSocket)factory.createSocket(hostname, port);
		    System.out.println("Handshake...");
		    // Connect to the server
		    socket.startHandshake();
		    System.out.println("Retrieve certs");
		    // Retrieve the server's certificate chain
		    java.security.cert.Certificate[] serverCerts =
		        socket.getSession().getPeerCertificates();
		    
		    // Let's do some stuff yo
		    System.out.println("Look we have the server's certificates!");
		    // Verify
		    serverCerts[0].verify(cert.getPublicKey());
		    System.out.println("And it's verified!");
		    // Close the socket
		    socket.close();
		} catch (SSLPeerUnverifiedException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
