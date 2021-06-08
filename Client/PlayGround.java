import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.awt.GridLayout;

public class PlayGround extends Client implements MouseListener {
    JFrame frame = new JFrame();
    JLabel label[] = new JLabel[9];
    ImageIcon check = new ImageIcon("./img/check.png");
    ImageIcon cross = new ImageIcon("./img/cross.png");
    ImageIcon square = new ImageIcon("./img/square.png");
    ImageIcon image = new ImageIcon("./img/tic-tac-toe.png");
    ImageIcon winIcon = new ImageIcon("./img/winner.png");
    ImageIcon loseIcon = new ImageIcon("./img/game-over.png");
    ImageIcon drawIcon = new ImageIcon("./img/draw.png");
    ImageIcon myType; //the image when i click
    ImageIcon enemyType; //the image when enemy click
    String winOrNot = "none"; //anyone win, lose, draw
    int myChangeWhich = -1; //which label i choose to click
    int enemyChangeWhich = -1; //which label enemy choose to click
    boolean first = true; //for player who use cross, the player cannot click any label
    boolean first2 = true; //for player who use cross, the player should wait another player check first
    boolean open = false; //wait to let press process done
    PlayGround() throws Exception{

        //set all square
        myResize(check, "./img/check.png");
        myResize(cross, "./img/cross.png");
        myResize(square, "./img/square.png");
        myResize(winIcon, "./img/winner.png");
        myResize(loseIcon, "./img/game-over.png");
        myResize(drawIcon, "./img/draw.png");
        for(int i = 0; i < 9; i++) {
            label[i] = new JLabel();
            label[i].setSize(100,100);
            label[i].addMouseListener(this);
            label[i].setIcon(square);
        }

        for(int i = 0; i < 9; i++)
            frame.add(label[i]);
        frame.addMouseListener(this);
        frame.setTitle("Playing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3));
        frame.setSize(350,350);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(255, 255, 204));
    } 

    public void gogo(){ //to change all label depending on situation
        if(myChangeWhich != -1) {
            System.out.println("my");
            label[myChangeWhich].setIcon(myType);
            myChangeWhich = -1;
        }
        else if(enemyChangeWhich != -1) {
            System.out.println("enemy");
            label[enemyChangeWhich].setIcon(enemyType);
            enemyChangeWhich = -1;
        }
        else if(winOrNot.equals("win")) {
            for(JLabel i: label)
                i.setIcon(winIcon);
        }
        else if(winOrNot.equals("lose")) {
            for(JLabel i: label)
                i.setIcon(loseIcon);
        }
        else if(winOrNot.equals("draw")) {
            for(JLabel i: label)
                i.setIcon(drawIcon);
        }
        frame.revalidate();
    }
    
    public void mouseEntered(MouseEvent e){ //the first thing for all player to do
        try {
            if(first2 && Client.check_or_cross.equals("cross")) { //for cross player to disable all label first
                first2 = false;
                for(JLabel i: label)
                    i.setEnabled(false);
                frame.revalidate();
                System.out.println("cross");
            }
            else if(first && Client.check_or_cross.equals("cross")) { //for cross player to get check player choice
                first = false;
                myType = cross;
                enemyType = check;
                output.writeUTF("first");
                output.flush();
                
                frame.revalidate();
                enemyChangeWhich = input.readInt();
                gogo();
                for(JLabel i: label)
                    i.setEnabled(true);
                frame.revalidate();
            }
            else if(first && e.getSource() != frame) { //for check player
                first = false;
                myType = check;
                enemyType = cross;
                System.out.println("check");
            }
        } catch(Exception err) {}
    }

    public void mousePressed(MouseEvent e){
        try {
            //my turn
            for(JLabel i: label) //avoid disabled label
                if((e.getSource() == i && !i.isEnabled()) || first)
                    return;
            for(JLabel i: label) //avoid label already be chosen before
                if(e.getSource() == i && (i.getIcon().equals(check) || i.getIcon().equals(cross)))
                    return;
            if(winOrNot.equals("win") || winOrNot.equals("lose") || winOrNot.equals("draw")) //terminate when game finish
                return;

            //tell server i click a label
            System.out.println("Click!!");
            open = false;
            output.writeUTF("click");
            output.flush();

            //change label and tell server
            for(int i = 0; i < 9; i++) { 
                if(e.getSource() == label[i]) {
                    myChangeWhich = i;
                    output.writeInt(i);
                    output.flush();
                }
                label[i].setEnabled(false);
            }
            gogo();

            //read win or not
            winOrNot = input.readUTF();
            System.out.println(winOrNot);
            gogo();
            if(winOrNot.equals("win") || winOrNot.equals("lose") || winOrNot.equals("draw"))
                return;
            open = true; //release can start
        } catch(Exception err) {}
    }

    public void mouseReleased(MouseEvent e){
        try{
            int count = 0;
            while(!open) { //wait a moment
                if(count++ == 10000)
                    break;
            }
            open = true;
            System.out.println("Release!!");

            //enemy turn, read enemy choice from server
            enemyChangeWhich = input.readInt();
            gogo();

            //read win or not
            winOrNot = input.readUTF();
            System.out.println(winOrNot);
            gogo();
            if(winOrNot.equals("win") || winOrNot.equals("lose") || winOrNot.equals("draw"))
                return;
            
            //my term all label can be used
            for(JLabel i: label)
                i.setEnabled(true);
        } catch(Exception err) {}
    }
    

    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e) {}
    void myResize(ImageIcon icon, String img) throws IOException{
        //resize and update icon
        BufferedImage temp = null;
        temp = ImageIO.read(new File(img));
        Image resized = temp.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon.setImage(resized);
    }
}