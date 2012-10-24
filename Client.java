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

import java.util.*;


public class Client {
	static private java.security.cert.Certificate cert;
	static private OutputStream out;
	static private DataOutputStream outData;
	static private SSLSocket cSock;
	static private String hostname;
	static private int port;
	static private InputStream in;
	
	static public void newSocket() throws UnknownHostException, IOException, InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();
		cSock = (SSLSocket)factory.createSocket(hostname, port);
		cSock.startHandshake();
		java.security.cert.Certificate[] serverCerts =
	    	cSock.getSession().getPeerCertificates();
	    
	    // Let's do some stuff yo
	   // Verify
	    serverCerts[0].verify(cert.getPublicKey());
	}
	
	static public void closeAll() throws IOException {
		cSock.close();
		out.close();
		outData.close();
	}
	
	static public void initOut() throws IOException {
		out = cSock.getOutputStream();
		outData = new DataOutputStream(out);
	}
	
	static public void upload(String[] argv) throws IOException, InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
	    String fname;
	    InputStream in;
	    File f;
	    long flen;
	    // Send files
	    for(int i = 0; i<argv.length; i++) {
		    newSocket();
		    initOut();
		    fname = argv[i];
		    in = new FileInputStream(fname);
		    f = new File(fname);
		    flen = f.length(); // Get file size
		    fname = f.getName(); // Get file name
		    outData.writeBytes("\r\nSTARTFILEREADING\r\n"+fname+"\r\n"+flen+"\r\n");
		    System.out.println("Sent name "+fname);
		    closeAll();
		    newSocket(); // Send file
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
	    newSocket();
	    System.out.println("Ending socket");
	    initOut();
	    outData.writeBytes("\n\rENDFILEUPLOAD\n\r");
	    closeAll();
	}
	
	static public void main(String argv[]) {
		try {
			if(argv.length < 1) {
				System.out.println("Usage: Client [port]");
				System.exit(0);
			}
		    // Keyboard input
		    Scanner sc = new Scanner(System.in);
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
		    newSocket();
		    in = cSock.getInputStream();
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String s;
		    if(!(s = br.readLine()).startsWith("Welcome")) {
		    	System.err.println("Error: Server welcome not received.");
		    	System.exit(0);
		    }
		    //cSock.close();
		    //in.close();
		    System.out.println(s+'\n');
		    Integer op;
		    boolean end = false;
		    while(!end) {
			    System.out.println("Would you like to do anything else?");
			    System.out.println("1 - ADD/UPDATE\t2 - DELETE\t3 - FETCH\n4 - LIST\t5 - VERIFY\t6 - EXIT");
			    s = sc.nextLine();
			    if(s.isEmpty()) { // Stop an exception if user just presses enter key
		    		s = "0";
		    	}
		    	op = Integer.parseInt(s);
			    switch(op) {
			    	case 1:
			    		System.out.println("Please enter the files you would like to upload (Absolute or relative paths) separated by a comma\neg. file.txt,file2.gif");
			    		String[] files = sc.nextLine().split(",");
			    		System.out.println("Recreating socket...");
			    		newSocket();
			    		initOut();
			    		outData.writeBytes("\r\nCLOUDCOMMAND\r\nUPLOAD\r\n");
			    		System.out.println("Sent command.");
			    		upload(files);
			    		closeAll();
			    	break;
			    	case 6:
			    		newSocket();
			    		initOut();
			    		outData.writeBytes("\r\nCLOUDCOMMAND\r\nLOGOUT\r\n");
			    		System.out.println("Bye bye!");
			    		end = true;
			    	break;
			    	default:
			    		System.out.println("Command not recognised.");
			    	break;	
			    }
		    }
		    is.close();
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
