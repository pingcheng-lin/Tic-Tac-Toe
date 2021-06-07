import javax.swing.*;
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
    ImageIcon myType;
    ImageIcon enemyType;
    boolean win = false;
    boolean first = true;
    PlayGround() throws Exception{

        //set all square
        myResize(check, "./img/check.png");
        myResize(cross, "./img/cross.png");
        myResize(square, "./img/square.png");
        myResize(winIcon, "./img/winner.png");
        myResize(loseIcon, "./img/game-over.png");
        for(int i = 0; i < 9; i++) {
            label[i] = new JLabel();
            label[i].setSize(100,100);
            label[i].addMouseListener(this);
            label[i].setIcon(square);
        }

        for(int i = 0; i < 9; i++)
            frame.add(label[i]);
        frame.setTitle("Playing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3));
        //frame.pack();
        frame.setSize(350,350);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(255, 255, 204));

        
    } 

    public void mouseClicked(MouseEvent e) {
        try {
            System.out.println("click");
            //my turn
            for(JLabel i: label)
                if(e.getSource() == i && i.isEnabled())
                    return;
            output.writeUTF("click");
            output.flush();
            for(int i = 0; i < 9; i++) {
                if(e.getSource() == label[i]) {
                    label[i].setIcon(check);
                    frame.revalidate();
                    output.writeInt(i);
                    output.flush();
                }
                label[i].setEnabled(false);
            }

            //win or not
            String winOrNot = input.readUTF();
            if(winOrNot.equals("win")) {
                for(JLabel i: label)
                    i.setIcon(winIcon);
                
                frame.revalidate();
                return;
            }
            else if(winOrNot.equals("lose")) {
                for(JLabel i: label)
                    i.setIcon(loseIcon);
                frame.revalidate();
                return;
            }

            System.out.println("11");
            //enemy turn
            int which = input.readInt();

            System.out.println("33");
            label[which].setIcon(enemyType);
            System.out.println("22");
            //win or not
            /*if(winOrNot.equals("win")) {
                for(JLabel i: label)
                    i.setIcon(winIcon);
                frame.revalidate();
                return;
            }
            else if(winOrNot.equals("lose")) {
                for(JLabel i: label)
                    i.setIcon(loseIcon);
                frame.revalidate();
                return;
            }
            else
                for(JLabel i: label)
                    i.setEnabled(true);*/
        } catch(Exception err) {}
    }


    public void mouseEntered(MouseEvent e){
        try {
            if(first && Client.check_or_cross.equals("cross")) {
                output.writeUTF("first");
                output.flush();
                myType = cross;
                enemyType = check;
                for(JLabel i: label)
                    i.setEnabled(false);
                System.out.println("11");
                int num = input.readInt();

                System.out.println("22");
                label[num].setIcon(enemyType);
                frame.revalidate();
                first = false;
                System.out.println("first");
            }
            else if(first) {
                myType = check;
                enemyType = cross;
                System.out.println("second");
                first = false;
            }
        } catch(Exception err) {}
    }
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    
    void myResize(ImageIcon icon, String img) throws IOException{
        //resize and update icon
        BufferedImage temp = null;
        temp = ImageIO.read(new File(img));
        Image resized = temp.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon.setImage(resized);
    }
}