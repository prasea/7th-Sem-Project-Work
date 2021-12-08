package signature;
import java.net.*;
import java.io.*;

public class fileClient 
{
        public static void main(String[] args) throws IOException
        { 
            Socket socket = null;
            InputStream in = null;
            OutputStream out = null;
            String host = "127.0.0.1";     

            socket = new Socket(host, 4444);
            

            File file = new File("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\signature\\clientSend.txt");

            // Get the size of the file
            long length = file.length();
            if (length > Long.MAX_VALUE) {
                System.out.println("File is too large.");
            }
            in = new FileInputStream(file);
            out = socket.getOutputStream();
            int count;
            byte[] buffer = new byte[8192]; // or 4096, or more
            while ((count = in.read(buffer)) > 0)
            {
              out.write(buffer, 0, count);
            }
        
            out.close();
            in.close();
            socket.close();
                
        }
}

//Socket s = new Socket("localhost", 1999); 
//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\signature\\clientSend.txt")); 
//byte []b = new byte[30]; 
//String k = br.readLine(); 
//DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
//dos.writeUTF(k); 
//System.out.println("Transfer Complete"); 