/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signature;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Prajanya
 */
public class FileExample {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileOutputStream fout = new FileOutputStream("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\signature\\clientSend.txt", true);
        String msg = "TATA";
        char ch[] = msg.toCharArray();
        byte b[] = msg.getBytes();
        for(int i=0;i<msg.length();i++)
        {
            fout.write(b[i]);
            fout.write(ch[i]);
        }
    }
}
