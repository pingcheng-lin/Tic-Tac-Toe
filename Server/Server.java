import java.net.*;
import java.util.Vector;
import java.io.*;  
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.HashMap;

class Server{

    static Vector<String> players = new Vector<String>();
    static Map<String, String> paper = new HashMap<>();//map plater to playground
    static Map<String, String> playerMap = new HashMap<>();
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
            String writeType = new String();
            String opponent = new String();
            System.out.println("Wait player name...");
            String name = input.readUTF();
            System.out.println("Player name: " + name + "login");
            Server.players.add(name);
            String forDiff = "000000000";
            while(true) {

                String command = input.readUTF();
                String[] subcommand = command.split("\\s");

                if(subcommand[0].equals("update")) {
                    System.out.println(name + " update list");
                    StringBuilder update = new StringBuilder();
                    update.append("update");
                    //Server.players.forEach((n) -> update.append(" " + n));
                    for(String i: Server.players) {
                        if(!i.equals(name))
                            update.append(" " + i);
                    }
                    output.writeUTF(update.toString());
                    output.flush();
                    System.out.println(update.toString());
                }

                else if(subcommand[0].equals("play")) {
                    opponent = subcommand[1];
                    writeType = "check";
                    if(Server.playerMap.containsKey(opponent))
                        writeType = "cross";
                    Server.playerMap.put(name, opponent);
                    while(!Server.playerMap.containsKey(opponent) || !Server.playerMap.get(opponent).equals(name));
                    output.writeUTF(writeType);
                    output.flush();
                    if(!Server.paper.containsKey(opponent) && !Server.paper.containsKey(name)) {
                        System.out.println("Create place to play.");
                        Server.paper.put(name, "000000000");
                    }
                }
                else if(subcommand[0].equals("click")) {
                    //get place(nine square)
                    String place = new String();
                    if(Server.paper.containsKey(name))
                        place = Server.paper.get(name);
                    else
                        place = Server.paper.get(opponent);
                    int x = input.readInt();
                    //update place
                    StringBuilder buildPaper = new StringBuilder();
                    for(int i = 0; i < 9; i++) {
                        if(x == i)
                            if(writeType.equals("check"))
                                buildPaper.append("1");
                            else
                                buildPaper.append("2");
                        else
                            buildPaper.append(place.charAt(i));
                    }

                    //see last time and this time diff
                    place = buildPaper.toString();
                    for(int i = 0; i < 9; i++) {
                        if(place.charAt(i) != forDiff.charAt(i)) {
                            System.out.println("111");
                            output.writeInt(place.charAt(i) - '0');
                            output.flush();
                            break;
                        }
                    }
                    //update diff
                    forDiff = buildPaper.toString();
                    if(Server.paper.containsKey(name))
                        Server.paper.put(name, forDiff);
                    else
                        Server.paper.put(opponent, forDiff);
                    char check = '0';
                    checkWinner(forDiff, check);
                    if(((check == '1') && writeType.equals("check")) || ((check == '2') && writeType.equals("cross")))
                        output.writeUTF("win");
                    else if(((check == '2') && writeType.equals("check")) || ((check == '1') && writeType.equals("cross")))
                        output.writeUTF("lose");
                    else
                        output.writeUTF("notYet");
                }
                else if(subcommand[0].equals("first")) {
                    while(forDiff.equals("000000000"));
                    //????
                    for(int i = 0; i < forDiff.length(); i++)
                        if(forDiff.charAt(i) != '0')
                            output.writeInt(i);
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

    public void checkWinner(String array, char check) {
        if((array.charAt(0) != '0') && (array.charAt(0) == array.charAt(1)) && (array.charAt(1) ==  array.charAt(2)))
            check = array.charAt(0);
        else if((array.charAt(0) != '0') && (array.charAt(3) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(5)))
            check = array.charAt(0);
        else if((array.charAt(0) != '0') && (array.charAt(6) == array.charAt(7)) && (array.charAt(7) ==  array.charAt(8)))
            check = array.charAt(0);
        else if((array.charAt(0) != '0') && (array.charAt(0) == array.charAt(3)) && (array.charAt(3) ==  array.charAt(6)))
            check = array.charAt(0);
        else if((array.charAt(0) != '0') && (array.charAt(1) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(7)))
            check = array.charAt(0);
        else if((array.charAt(0) != '0') && (array.charAt(2) == array.charAt(5)) && (array.charAt(5) ==  array.charAt(8)))
            check = array.charAt(0);
        else if((array.charAt(0) != '0') && (array.charAt(0) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(8)))
            check = array.charAt(0);
        else if((array.charAt(0) != '0') && (array.charAt(2) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(6)))
            check = array.charAt(0);
    }
}
