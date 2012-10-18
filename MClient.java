import java.net.*;
import java.io.*;

public class MClient {
	public static void main(String argv[]) throws Exception {
		if(argv.length < 2) {
			System.out.println("Usage: Client [port] [filenames]");
			System.exit(0);
		}		
		int port = Integer.parseInt(argv[0]);
		System.out.println("Opening connection...");
		Socket cSock;
		OutputStream out;
		DataOutputStream outData;
		String fname;
		InputStream in;
		File f;
		long flen;
		// Send files
		for(int i = 1; i<argv.length; i++) {
			cSock = new Socket("localhost", port);
			out = cSock.getOutputStream(); // Get the output stream
			outData = new DataOutputStream(out); // To send bytes as strings
			fname = argv[i];
			in = new FileInputStream(fname);
			f = new File(fname);
			flen = f.length();
			fname = f.getName();
			outData.writeBytes("\r\nSTARTFILEREADING\r\n"+fname+"\r\n"+flen+"\r\n");
			System.out.println("Sent name "+fname);
			cSock = new Socket("localhost", port); // Send file
			out = cSock.getOutputStream(); // Get the output stream
			byte[] buf = new byte[1024];
			int len;
			int total = 0;
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
				total += len;
			}
			System.out.println("Total "+total+" bytes sent.");
			in.close();
			out.close();
			cSock.close();
		}
	}
}
