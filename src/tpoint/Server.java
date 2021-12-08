package tpoint;
import java.io.*;  
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;  
import java.security.spec.*;  
import java.util.ArrayList;
import static com.generate.Client.sendFile;
public class Server  
{  
    public static void main(String args[]) throws IOException   
    {  
        verifyDigitalSignature();
    }
    
    public static void verifyDigitalSignature() {
        try {
            /* import & convert encoded public key bytes*/
            FileInputStream keyfis = new FileInputStream("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\tpoint\\publickey.txt");
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);
            keyfis.close();

            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            /* input the signature bytes */
            FileInputStream sigfis = new FileInputStream("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\tpoint\\signature.txt");
            byte[] sigToVerify = new byte[sigfis.available()];
            sigfis.read(sigToVerify);
            sigfis.close();
            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);
            /* Update and verify the data */

 /* Supply the Signature Object with the Data to be Verified*/
            FileInputStream datafis = new FileInputStream("C:\\Users\\Prajanya\\Documents\\NetBeansProjects\\Project\\src\\tpoint\\send.txt");
            BufferedInputStream bufin = new BufferedInputStream(datafis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
            }
            bufin.close();
            boolean verifies = sig.verify(sigToVerify);
            System.out.println("signature verifies: " + verifies);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}  
