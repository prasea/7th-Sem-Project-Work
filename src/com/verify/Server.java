/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.verify;

/**
 *
 * @author Prajanya
 */


import java.io.*;  
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;  
import java.security.spec.*;  
import java.util.ArrayList;
import static com.multifiles.Server.fileSize;
public class Server  
{  
    public static String sendFile = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\verify\\send.txt";
    public static String signFile = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\verify\\signature.txt";
    public static String pubkeyFile = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\verify\\publickey.txt";
    public static void main(String args[]) throws IOException   
    {  
        ServerSocket ss = new ServerSocket(5555);
        Socket socket = ss.accept();
        
        File file = new File(sendFile);
        File key = new File(signFile);
        File sign = new File(pubkeyFile);
        
        receive(socket);

        
        verifyDigitalSignature();                 
    }
    public static void receive(Socket socket)
    {
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
//                System.out.println("File number "+ i +" size is "+fileSize);
                System.out.println("Receiving file: " + files.get(i).getName());
                //create a new fileoutputstream for each new file
                FileOutputStream fos = new FileOutputStream("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\verify\\" + files.get(i).getName());
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
    public static void verifyDigitalSignature() {        
        try {
            /* import & convert encoded public key bytes*/
            FileInputStream keyfis = new FileInputStream(pubkeyFile);
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);
            keyfis.close();

            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            /* input the signature bytes */
            FileInputStream sigfis = new FileInputStream(signFile);
            byte[] sigToVerify = new byte[sigfis.available()];
            sigfis.read(sigToVerify);
            sigfis.close();
            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);
            /* Update and verify the data */

 /* Supply the Signature Object with the Data to be Verified*/
            FileInputStream datafis = new FileInputStream(sendFile);
            BufferedInputStream bufin = new BufferedInputStream(datafis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
            }
            bufin.close();
            boolean verifies = sig.verify(sigToVerify);
            if(verifies == true){
                System.out.println("The document is not tempered");
            } else{
                System.out.println("The document is tempered");
            }
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}  
