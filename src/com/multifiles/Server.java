
package com.multifiles;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static long fileSize;
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(5555);
        Socket socket = ss.accept();

        //Server receives the file.
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//read the number of files from the client
            int number = dis.readInt();
            ArrayList<File> files = new ArrayList<File>(number);
            System.out.println("Number of Files to be received: " + number);
            //read file names, add files to arraylist
            for (int i = 0; i < number; i++) {
                File file = new File(dis.readUTF());
                files.add(file);             
            }
            int n = 0;
            byte[] buf = new byte[4092];

            //outer loop, executes one for each file
            for (int i = 0; i < files.size(); i++) {                                
                fileSize = dis.readLong();
                System.out.println("File number "+ i +" size is "+fileSize);
                System.out.println("Receiving file: " + files.get(i).getName());
                //create a new fileoutputstream for each new file
                FileOutputStream fos = new FileOutputStream("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\" + files.get(i).getName());
               while (fileSize > 0 && (n = dis.read(buf, 0, (int)Math.min(buf.length, fileSize))) != -1)
                {
                  fos.write(buf,0,n);
                  fileSize -= n;
                }
                
                //read file
//                while ((n = dis.read(buf)) != -1) {
//                    fos.write(buf, 0, n);
//                    fos.flush();
//                }
                fos.close();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
}

//    public static String file = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\multifiles\\file.txt";
//    public static String key = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\multifiles\\key.txt";
//    public static String sign = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\multifiles\\sign.txt";

