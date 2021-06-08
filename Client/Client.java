import java.net.*;  
import java.io.*;

public class Client{
    static DataInputStream input; 
    static DataOutputStream output;
    static String check_or_cross; 
    public static void main(String args[]) throws Exception{

        Socket socketClient = new Socket("localhost",1234); //create a client socket  
        input = new DataInputStream(socketClient.getInputStream());
        output = new DataOutputStream(socketClient.getOutputStream());  

        Launch frame = new Launch(); //create a frame
    }
}

