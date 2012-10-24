import java.io.*;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

import javax.net.ssl.*;


public class Client {
	static private java.security.cert.Certificate cert;
	static private OutputStream out;
	static private DataOutputStream outData;
	static private SSLSocket cSock;
	static private String hostname;
	static private int port;
	static private InputStream in;
	
	static public SSLSocket newSocket() throws UnknownHostException, IOException, InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();
		SSLSocket sock = (SSLSocket)factory.createSocket(hostname, port);
		sock.startHandshake();
		java.security.cert.Certificate[] serverCerts =
	    	sock.getSession().getPeerCertificates();
	    
	    // Let's do some stuff yo
	    System.out.println("Look we have the server's certificates!");
	    // Verify
	    serverCerts[0].verify(cert.getPublicKey());
	    System.out.println("And it's verified!");
		return sock;
	}
	
	static public void closeAll() throws IOException {
		cSock.close();
		out.close();
		outData.close();
	}
	
	static public void upload(String[] argv) throws IOException, InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
	    String fname;
	    InputStream in;
	    File f;
	    long flen;
	    // Send files
	    for(int i = 1; i<argv.length; i++) {
		    System.out.println("Start file loop");
		    cSock = newSocket();
		    System.out.println("Created socket");
		    out = cSock.getOutputStream(); // Get the output stream
		    System.out.println("Got Output Stream");
		    outData = new DataOutputStream(out); // To send bytes as strings
		    System.out.println("Got data output stream");
		    fname = argv[i];
		    System.out.println("Got name"+fname);
		    in = new FileInputStream(fname);
		    System.out.println("File input stream");
		    f = new File(fname);
		    System.out.println("Open file");
		    flen = f.length();
		    System.out.println("Got length");
		    fname = f.getName();
		    System.out.println("Sent name "+fname);
		    outData.writeBytes("\r\nSTARTFILEREADING\r\n"+fname+"\r\n"+flen+"\r\n");
		    System.out.println("Sent name "+fname);
		    closeAll();
		    cSock = newSocket(); // Send file
		    out = cSock.getOutputStream(); // Get the output stream
		    byte[] buf = new byte[1024];
		    int len;
		    int total = 0;
		    while((len = in.read(buf)) > 0) {
			    out.write(buf, 0, len);
			    total += len;
		    }
		    System.out.println("Total "+total+" bytes sent.");
		    closeAll();
		    in.close();
	    }
	    cSock = newSocket();
	    System.out.println("Ending socket");
	    out = cSock.getOutputStream(); // Get the output stream
	    System.out.println("Got Output Stream");
	    outData = new DataOutputStream(out); // To send bytes as strings
	    outData.writeBytes("\n\rENDFILEUPLOAD\n\r");
	    closeAll();
	}
	
	static public void main(String argv[]) {
		try {
			if(argv.length < 2) {
				System.out.println("Usage: Client [port] [filenames]");
				System.exit(0);
			}
		    // Create the client socket
		    port = Integer.parseInt(argv[0]);
		    hostname = "localhost";
		    System.setProperty("javax.net.ssl.trustStore", "Client/cTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "ckeystorePW");
			// Get server certificate from truststore
		  	FileInputStream is = new FileInputStream(System.getProperty("javax.net.ssl.trustStore"));

		    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		    keystore.load(is, "ckeystorePW".toCharArray());

		    // Get certificate
		    cert = keystore.getCertificate("server");
		    
		    // BEGIN ACTUAL CLIENT STUFF
		    // Let's receive a welcome message first
		    // Open first socket
		    cSock = newSocket();
		    in = cSock.getInputStream();
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String s;
		    if(!(s = br.readLine()).startsWith("Welcome")) {
		    	System.err.println("Error: Server welcome not received.");
		    	System.exit(0);
		    }
		    System.out.println(s);
		} catch (SSLPeerUnverifiedException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
