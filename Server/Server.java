import java.net.*;
import java.util.Vector;
import java.io.*;  

class Server{

    static Vector<String> players = new Vector<String>();
    static int [][] paper = new int [3][3];
    public static void main(String args[])throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(1234); //create a server socket
            while(true) {
                System.out.println("Waiting...");
                Socket connection = serverSocket.accept(); //establish connection and waits for the client
                System.out.println("Success Connect");
                WaitingRoom wr = new WaitingRoom(connection);
                Thread th = new Thread(wr);
                th.start();
            }
        } catch(Exception e) {
            System.out.println(e);
        }
        

        //serverSocket.close();
    }


}

class WaitingRoom implements Runnable {
    private Socket connection;
    private DataInputStream input;
    private DataOutputStream output;
    
    public WaitingRoom(Socket connection) {
            this.connection = connection;
    }

    public void run() {
        try {
            input = new DataInputStream(connection.getInputStream());  
            output = new DataOutputStream(connection.getOutputStream());
            
            String str="";
            System.out.println("Wait player name...");
            str=input.readUTF();
            Server.players.addElement(str);
    
            System.out.println("Player name: " + str);
            System.out.flush();
        } catch(Exception e) {
            System.out.println(e);
        }
        

        //str2=br.readLine();  
        //output.writeUTF(str2);  
        //output.flush();  
        try {
            input.close();
            output.close();
            connection.close(); 
        } catch(Exception e) {
            System.out.println(e);
        }
    }

}

