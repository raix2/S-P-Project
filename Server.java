import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class Server {
	static private ServerSocketFactory ssocketFactory;
	static private ServerSocket servSock;
	static private OutputStream out;
	static private DataOutputStream dataOut;
	static private InputStream in;
	static private BufferedReader br;
	static private Socket sock;
	
	public static boolean upload() throws IOException {
		String folder = "Server/to/";
	    String fname;
	    long flen;
	    File f;
	    byte[] buffer = new byte[1024];
	    int len;
	    boolean endFile = false;
	    boolean run = true;
	    while(run) {
		    sock = servSock.accept(); // Listening socket
		    System.out.println("Receiving something!");
		    in = sock.getInputStream();
		    br = new BufferedReader(new InputStreamReader(in));
		    while((fname = br.readLine()) != null && !fname.equals("STARTFILEREADING") && !fname.equals("ENDFILEUPLOAD")) {}
		    if(fname == null) {
		    	run = false;
		    	System.out.println("Client message error. Upload aborted.");
		    	return false;
		    }
		    else if(fname.equals("ENDFILEUPLOAD")) {
		    	run = false;
		    	System.out.println("End of upload session.");
		    	return true;
		    }
		    else {
			    fname = br.readLine();
			    flen = Long.parseLong(br.readLine());
			    sock = servSock.accept();
			    in = sock.getInputStream();
			    f = new File(folder+fname); // Open file stream with specified name
			    System.out.println("Created file stream for file "+fname+" with "+flen+" bytes");
			    out = new FileOutputStream(f);
			    while(!endFile && flen > 1024) {
				    if((len = in.read(buffer)) != -1) {
				    	out.write(buffer, 0, len);
				    	flen -= len;
				    }
				    else
				    	endFile = true;
			    }
			    System.out.println("Left to read: "+flen+" bytes");
			    if(endFile || (len = in.read(buffer, 0, (int) flen)) == -1)
			    	System.out.println("File end reached unexpectedly");
			    else {
			    	while(flen > 0 && len != -1) {
			    		out.write(buffer, 0, len);
			    		flen -= len;
			    		if(flen > 0)
			    			len = in.read(buffer, 0, (int) flen);
			    	}
			    }
			    out.close();
			    System.out.println("END");
		    }
	    }
	    System.out.println("Transfer ended.");
	    return true;
	}
	
	public static void main(String argv[]) {
		try {
			if(argv.length < 1) {
				System.out.println("Usage: Server [port]");
				System.exit(0);
			}
			int port = Integer.parseInt(argv[0]);
			System.setProperty("javax.net.ssl.keyStore", "Server/keystore.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "keystorePW");

			System.out.println("Server setting up port "+port);
			ssocketFactory = SSLServerSocketFactory.getDefault();
			servSock = ssocketFactory.createServerSocket(port);
			while(true) {
				//Aaaaand we have connection!
				sock = servSock.accept();
				System.out.println("============================\nA user has connected!");
				out = sock.getOutputStream();
				dataOut = new DataOutputStream(out);
				dataOut.writeBytes("Welcome to the Cloud");
				sock.close();
				out.close();
				dataOut.close();
				// Take command from client
				boolean run = true;
				while(run) {
					System.out.println("\nTaking commands...");
					sock = servSock.accept();
					in = sock.getInputStream();
					br = new BufferedReader(new InputStreamReader(in));
					String cmd;
					while((cmd = br.readLine()) != null && !cmd.equals("CLOUDCOMMAND")) {}
					if(cmd == null) {
						run = false;
						System.out.println("ERROR in client message");
					}
					else {
						switch((cmd = br.readLine())) {
							case "UPLOAD":
								System.out.println("\nUpload command.");
								if(!upload())
									run = false;
							break;
							case "LOGOUT":
								System.out.println("\nUser logged out.");
								run = false;
							break;
							default:
								System.out.println("\nCommand not recognised.");
								run = false;
							break;
						}
					}
				}
				System.out.println("User session ended.");
			}
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}
}
