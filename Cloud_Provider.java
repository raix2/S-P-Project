import javax.net.ssl.*;
import java.io.*;
	
public class Cloud_Provider {
	
	public void upload() {
		// Connection stuff to receive from user
		// Receive number of bytes of file
		BigDecimal a1 = new BigDecimal("8250325.12");
	      BigDecimal b1 = new BigDecimal("4321456.31");
	      System.out.println(NumberFormat.getCurrencyInstance().format(c1)); fileSize = 0;
		// Convert to mb
		fileSize /= 1024;
		// Now get money
		// Connect to bank
		
	}
	
	public static void main(String[] arstring) throws IOException
	{
		try{	
			//byte[] newbuffer = null;
		SSLServerSocketFactory sslserverfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		SSLServerSocket sslserversocket = (SSLServerSocket)sslserverfactory.createServerSocket(9999);
		System.out.println("cloud provider is waiting...");
		SSLSocket clientsocket = (SSLSocket)sslserversocket.accept();
		System.out.println("Connection Accepted: "+ clientsocket);
	/*	
		InputStream inputstream = clientsocket.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
         
         String string = null;
         while ((string = bufferedreader.readLine()) != null) {
             System.out.println(string);
             System.out.flush();
         }
	*/	
		File myFile = new File("/home/uniwa/students/students5/20689955/linux/workspace/S&P Project/home");
		byte [] byteCount  = new byte [(int)myFile.length()];
		
		InputStream input = new FileInputStream(myFile);
		BufferedInputStream stream = new BufferedInputStream(input);
		
		int bytesRead=0;
		String fileContents;
		while ((bytesRead = stream.read(byteCount,0,1)) != -1) {
            fileContents = new String(byteCount,0,bytesRead);  
			System.out.print(fileContents);
          }	
		
		
		OutputStream output = clientsocket.getOutputStream();
		System.out.println("Sending...");
		
		output.flush();

	//	input.close();
	//tream.close();
		
	 } catch (Exception exception) {
         exception.printStackTrace();
     }
  
    	 
     
	}
}
