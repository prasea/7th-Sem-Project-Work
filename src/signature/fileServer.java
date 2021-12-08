package  signature;
import java.net.*;
import java.io.*;

public class fileServer {
    public static void main (String []args) throws IOException
    {	
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(4444);

        Socket socket = null;
        socket = serverSocket.accept();

        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\signature\\serverReceive.txt",true);
      

        int count;
        byte[] buffer = new byte[8192]; // or 4096, or more
        while ((count = in.read(buffer)) > 0)
        {
          out.write(buffer, 0, count);
        }
    }
}

//        ServerSocket ss = new ServerSocket(1999); 
//        Socket s = ss.accept(); 
//        DataInputStream dis = new DataInputStream(s.getInputStream()); 
//        String k = dis.readUTF(); 
//        System.out.println("File Transferred"); 
//        FileOutputStream fos = new FileOutputStream("serverReceive.txt"); 
//        byte[] b = k.getBytes(); 
//        fos.write(b); 