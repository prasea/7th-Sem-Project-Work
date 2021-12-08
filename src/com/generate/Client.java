/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generate;

import static com.multifiles.Client.fileS;
import static com.multifiles.Client.keyS;
import static com.multifiles.Client.signS;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;

public class Client {

    public static String sendFile = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\generate\\send.txt";
    public static String signFile = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\generate\\signature.txt";
    public static String pubkeyFile = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\generate\\publickey.txt";

    public static void main(String args[]) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 5555);
        
        
        ArrayList<File> files = new ArrayList<File>();
        File file = new File(sendFile);
        File key = new File(signFile);
        File sign = new File(pubkeyFile);
        
        if(!key.exists() && !sign.exists())
        {
            generateDigitalSingature();        
            files.add(file);
            files.add(key);
            files.add(sign);        
            send(files, socket);
        } else{
            //Sending just the text tile
            files.add(file);
            send(files,socket);
        }
        

    }
    public static void send(ArrayList<File> files, Socket socket)
    {
                //Client sends the file
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println(files.size());
//write the number of files to the server
            dos.writeInt(files.size());
            dos.flush();

            //write file names 
            for (int i = 0; i < files.size(); i++) {
                dos.writeUTF(files.get(i).getName());
                dos.flush();
            }

            //buffer for file reading, to declare inside or outside loop?
            int n = 0;
            byte[] buf = new byte[4092];
            //outer loop, executes one for each file
            for (int i = 0; i < files.size(); i++) {
                dos.writeLong(files.get(i).length());
                System.out.println(files.get(i).getName());
                //create new fileinputstream for each file
                FileInputStream fis = new FileInputStream(files.get(i));

                //write file to dos
                while ((n = fis.read(buf)) != -1) {
                    dos.write(buf, 0, n);
                    dos.flush();

                }
                //should i close the dataoutputstream here and make a new one each time?
            }
            //or is this good?
            dos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void generateDigitalSingature() {
        try {
            /* Generate a key pair */
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();

            /* Create a Signature object and initialize it with the private key */
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(priv);
            /* Update and sign the data */
            FileInputStream fis = new FileInputStream(sendFile);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                dsa.update(buffer, 0, len);
            }
            bufin.close();
            /* Now that all the data to be signed has been read in, generate a signature for it */
            byte[] realSig = dsa.sign();
            /* Save the signature in a file */
            FileOutputStream sigfos = new FileOutputStream(signFile);
            sigfos.write(realSig);
            sigfos.close();
            /* Save the public key in a file */
            byte[] key = pub.getEncoded();
            FileOutputStream keyfos = new FileOutputStream(pubkeyFile);
            keyfos.write(key);
            keyfos.close();
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}
