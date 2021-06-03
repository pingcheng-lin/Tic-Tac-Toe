import java.net.*;  
import java.io.*;

public class Client{
    public static void main(String args[]) throws Exception{
        //Socket socketClient = new Socket("localhost",1234); //create a client socket  
        
        MyFrame frame = new MyFrame(); //create a frame
        
        /*DataInputStream input=new DataInputStream(socketClient.getInputStream());  
        DataOutputStream output=new DataOutputStream(socketClient.getOutputStream());  
        BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));  
        
        String str = "", str2 = "";  
        //while(!str.equals("stop")) {  
            System.out.println("Player name: ");
            str = buf.readLine();  
            output.writeUTF(str);  
            output.flush();  
            //str2 = input.readUTF();  
            //System.out.println("Server says: " + str2);  
        //}
        input.close();
        output.close();  
        socketClient.close();  */
    }
}

