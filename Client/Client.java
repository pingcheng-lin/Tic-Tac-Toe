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

        //BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));  
        
        //String str = "", str2 = "";  
        //while(!str.equals("stop")) {  
            //str = buf.readLine();  
            //output.writeUTF(str);  
            //output.flush();  
            //str2 = input.readUTF();  
            //System.out.println("Server says: " + str2);  
        //}
        //input.close();
        //output.close();  
        //socketClient.close(); 
    }
}

