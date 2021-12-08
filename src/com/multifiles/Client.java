/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multifiles;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {

    public static String fileS = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\multifiles\\file.txt";
    public static String keyS = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\multifiles\\key.txt";
    public static String signS = "C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\com\\multifiles\\sign.txt";

    public static void main(String[] args) throws Exception {     
        Socket socket = new Socket("localhost", 5555);
        ArrayList<File> files = new ArrayList<File>();
        File file = new File(fileS);
        File key = new File(keyS);
        File sign = new File(signS);
        files.add(file);
        files.add(key);
        files.add(sign);
        
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
}