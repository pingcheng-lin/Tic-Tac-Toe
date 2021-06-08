import java.net.*;
import java.util.Vector;
import java.io.*;  
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
class Server{
    static boolean keyToRemoveMap = false; //the paper is useless so allow to remove
    static Vector<String> players = new Vector<String>(); //store all players name
    static Map<String, Integer> playerScoreWin =  new HashMap<>(); //map of player and num of win
    static Map<String, Integer> playerScoreLose =  new HashMap<>(); //map of player and num of lose
    static Map<String, String> paper = new HashMap<>(); //map player to playground
    static Map<String, String> playerMap = new HashMap<>(); //map player to opponent

    public static void main(String args[])throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234); //create a server socket
        
        System.out.println("Waiting first player...");
        while(true) {
            Socket connection = serverSocket.accept(); //establish connection and waits for the client
            System.out.println("A Connection Success");
            WaitingRoom wr = new WaitingRoom(connection); //socket to socket connection
            Thread th = new Thread(wr);
            th.start();
        }
    }
}

class WaitingRoom implements Runnable {
    private Socket connection; //player's own connection
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
            String forDiff = "000000000"; //to identify paper where its square is modified, (store new version)

            //read player name
            System.out.println("Wait player name...");
            String name = input.readUTF();
            System.out.println("Player name: " + name + " login.");

            //see if duplicate if not store it 
            if(!Server.players.contains(name)) {
                Server.players.add(name);
                Server.playerScoreWin.put(name, 0);
                Server.playerScoreLose.put(name, 0);
            }

            //loop until result appear
            while(true) {
                //read command and use whitespace to split
                String command = input.readUTF();
                String[] subcommand = command.split("\\s");

                //give client player list
                if(subcommand[0].equals("update")) {
                    System.out.println(name + " update list");
                    StringBuilder update = new StringBuilder();
                    update.append("update Random");
                    for(String i: Server.players) {
                        if(!i.equals(name))
                            update.append(" " + i + "(" + Server.playerScoreWin.get(i).toString() + "/" + Server.playerScoreLose.get(i).toString() + ")");
                    }
                    output.writeUTF(update.toString());
                    output.flush();
                    System.out.println(update.toString());
                }

                //map player opponent
                else if(subcommand[0].equals("play")) {
                    String[] temp = subcommand[1].split("\\(");
                    opponent = temp[0];

                    //if get random, give it random opponent
                    if(opponent.equals("Random")) { 
                        Random random = new Random();
                        int index = random.nextInt(Server.players.size());
                        while(Server.players.get(index).equals(name))
                            index = random.nextInt(Server.players.size());
                        opponent = Server.players.get(index);
                    }

                    //determine who is check and who is cross
                    writeType = "check"; 
                    if(Server.playerMap.containsKey(opponent))
                        writeType = "cross";
                    Server.playerMap.put(name, opponent);
                    while(!Server.playerMap.containsKey(opponent) || !Server.playerMap.get(opponent).equals(name)); //wait until another client put itself in playerMap
                    output.writeUTF(writeType);
                    output.flush();

                    //create paper to play
                    if(!Server.paper.containsKey(opponent) && !Server.paper.containsKey(name)) {
                        System.out.println("Create place to play.");
                        Server.paper.put(name, "000000000");
                    }
                }

                else if(subcommand[0].equals("first")) { //for cross player to wait check player, identify by see whether paper modified          
                    while(forDiff.equals("000000000")) {
                        if(Server.paper.containsKey(name))
                            forDiff = Server.paper.get(name);
                        else
                            forDiff = Server.paper.get(opponent);
                    }

                    for(int i = 0; i < 9; i++)
                        if(forDiff.charAt(i) != '0')
                            output.writeInt(i);
                }

                else if(subcommand[0].equals("click")) { //someone click
                    //get paper(nine square)
                    String place = new String();
                    if(Server.paper.containsKey(name))
                        place = Server.paper.get(name);
                    else
                        place = Server.paper.get(opponent);

                    //update paper 
                    int x = input.readInt();
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

                    //update diff(new version of paper)
                    forDiff = buildPaper.toString();
                    if(Server.paper.containsKey(name))
                        Server.paper.put(name, forDiff);
                    else
                        Server.paper.put(opponent, forDiff);
                    char check = '0';

                    //check win or draw or notYet
                    check = checkWinner(forDiff);
                    if(((check == '1') && writeType.equals("check")) || ((check == '2') && writeType.equals("cross"))) {
                        output.writeUTF("win");
                        System.out.println(name + " win");
                        Server.playerScoreWin.put(name, Server.playerScoreWin.get(name)+1);
                        break;
                    }
                    else if(check == '3') {
                        output.writeUTF("draw");
                        System.out.println(name + " draw");
                        Server.playerScoreWin.put(name, Server.playerScoreWin.get(name)+1);
                        Server.playerScoreLose.put(name, Server.playerScoreLose.get(name)+1);
                        break;
                    }
                    else
                        output.writeUTF("notYet");
                    System.out.println(name + " notYet");

                    //wait enemy and get paper by place
                    System.out.println(name + " wait " + opponent);
                    while(true) {
                        if(Server.paper.containsKey(name))
                            place = Server.paper.get(name);
                        else
                            place = Server.paper.get(opponent);
                        if(!forDiff.equals(place))
                            break;
                    }

                    //see last time and this time diff (forDiff here is old version)
                    for(int i = 0; i < 9; i++) {
                        if(place.charAt(i) != forDiff.charAt(i)) {
                            output.writeInt(i);
                            output.flush();
                            break;
                        }
                    }

                    //update forDiff     
                    forDiff = place;

                    //check lose or draw or notYet
                    check = checkWinner(forDiff);
                    if(((check == '2') && writeType.equals("check")) || ((check == '1') && writeType.equals("cross"))) {
                        output.writeUTF("lose");
                        System.out.println(name + " lose");
                        Server.playerScoreLose.put(name, Server.playerScoreLose.get(name)+1);
                        break;
                    }
                    else if(check == '3') {
                        output.writeUTF("draw");
                        System.out.println(name + " draw");
                        Server.playerScoreWin.put(name, Server.playerScoreWin.get(name)+1);
                        Server.playerScoreLose.put(name, Server.playerScoreLose.get(name)+1);
                        break;
                    }
                    else {
                        output.writeUTF("notYet");
                    }
                }
            }
            Server.playerMap.remove(name);
            Server.playerMap.remove(opponent);
            if(!Server.keyToRemoveMap)
                Server.keyToRemoveMap = true;
            else {
                if(Server.paper.containsKey(name))
                    Server.paper.remove(name);
                else
                    Server.paper.remove(opponent);
                Server.keyToRemoveMap = false;
            }
                
            output.close();
            input.close();
        } catch(Exception e) {}
    }

    public char checkWinner(String array) {
        //check all possibility of win and a possibility of draw
        if((array.charAt(0) != '0') && (array.charAt(0) == array.charAt(1)) && (array.charAt(1) ==  array.charAt(2)))
            return array.charAt(0);
        else if((array.charAt(3) != '0') && (array.charAt(3) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(5)))
            return array.charAt(3);
        else if((array.charAt(6) != '0') && (array.charAt(6) == array.charAt(7)) && (array.charAt(7) ==  array.charAt(8)))
            return array.charAt(6);
        else if((array.charAt(0) != '0') && (array.charAt(0) == array.charAt(3)) && (array.charAt(3) ==  array.charAt(6)))
            return array.charAt(0);
        else if((array.charAt(1) != '0') && (array.charAt(1) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(7)))
            return array.charAt(1);
        else if((array.charAt(2) != '0') && (array.charAt(2) == array.charAt(5)) && (array.charAt(5) ==  array.charAt(8)))
            return array.charAt(2);
        else if((array.charAt(0) != '0') && (array.charAt(0) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(8)))
            return array.charAt(0);
        else if((array.charAt(2) != '0') && (array.charAt(2) == array.charAt(4)) && (array.charAt(4) ==  array.charAt(6)))
            return array.charAt(2);
        else {
            boolean allNotZero = true;
            for(char i: array.toCharArray())
                if(i == '0')
                    allNotZero = false;
            if(allNotZero)
                return '3';
        }
        return '0';
    }
}
