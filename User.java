
import java.io.*;
import javax.net.ssl.*;

public class User {
	
	public static void main(String[] arstring)
	{
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		 
		SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
		
		try
		{
			SSLSocket c = (SSLSocket) f.createSocket("130.95.253.62", 9999);
			//printSocketInfo(c);
			//c.startHandshake();	
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream())); 
			
			BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
			 
			 String m = null;
			 while ((m=r.readLine())!= null) {
				 	System.out.println(m);
				 	m = in.readLine();
				 	w.write(m,0,m.length());
				 	w.newLine();
		            w.flush();
	            }
			
		}catch(Exception exception) {
	         exception.printStackTrace();
	     }
	}

}
