import java.net.*;
import java.io.*;

public class MServer {
	public static void main(String argv[]) throws Exception {
		if(argv.length != 1) {
			System.out.println("Usage: Server [port]");
			System.exit(0);
		}
		int port = Integer.parseInt(argv[0]);
		String fname;
		int noFiles;
		System.out.println("Server setting up port "+port+", press 'ENTER' to close connection...");
		ServerSocket servSock = new ServerSocket(3322);
		long flen;
		OutputStream out;
		File f;
		byte[] buffer = new byte[1024];
		int len;
		boolean end = false;
		while(true) {
			Socket sock = servSock.accept(); // Listening socket 
			System.out.println("Receiving something!");
			// Receive name from client
			InputStream fileIn = sock.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(fileIn));
			while((fname = in.readLine()) != null && !fname.equals("STARTFILEREADING")) {}
			System.out.println("Line break: "+fname);
			fname = in.readLine();
			flen = Long.parseLong(in.readLine());
			sock = servSock.accept(); 
			fileIn = sock.getInputStream();
			f = new File("to/"+fname); // Open file stream with specified name
			System.out.println("Created file stream for file "+fname+" with "+flen+" bytes");
			out = new FileOutputStream(f);
			while(!end && flen > 1024) {
				if((len = fileIn.read(buffer)) != -1) {
					out.write(buffer, 0, len);
					flen -= len;
				}
				else
					end = true;
			}
			System.out.println("Left to read: "+flen+" bytes");
			if(end || (len = fileIn.read(buffer, 0, (int) flen)) == -1)
				System.out.println("File end reached unexpectedly");
			else {
				out.write(buffer, 0, len);
				System.out.println(len+" bytes written");
			}
			out.close();
			System.out.println("File written!");
			System.out.println("END");
		}
	}
}
