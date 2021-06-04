import java.net.*;
import java.util.Vector;
import java.io.*;  
import java.lang.String;
import java.lang.StringBuilder;
class Server{

    static Vector<String> players = new Vector<String>();

    public static void main(String args[])throws Exception {
        
        ServerSocket serverSocket = new ServerSocket(1234); //create a server socket
        System.out.println("Waiting first player...");

        while(true) {
            Socket connection = serverSocket.accept(); //establish connection and waits for the client
            System.out.println("A Connection Success");
            WaitingRoom wr = new WaitingRoom(connection);
            Thread th = new Thread(wr);
            th.start();
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

            System.out.println("Wait player name...");
            String name = input.readUTF();
            System.out.println("Player name: " + name + "login");
            Server.players.add(name);

            while(true) {
                String command = input.readUTF();
                String[] subcommand = command.split("\\s");
                if(subcommand[0].equals("update")) {
                    System.out.println(name + " update list");
                    StringBuilder update = new StringBuilder();
                    update.append("update");
                    Server.players.forEach((n) -> update.append(" " + n));
                    output.writeUTF(update.toString());
                    output.flush();
                    System.out.println(update.toString());
                }
                else if(subcommand[0].equals("leave")) {
                    input.close();
                    output.close();
                    connection.close();
                }
            } 
        } catch(Exception e) {
            System.out.println(e);
        }
    }

}

