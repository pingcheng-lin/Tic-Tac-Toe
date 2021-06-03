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

public class PlayGround extends JFrame implements MouseListener {
    JLabel label[] = new JLabel[9];
    ImageIcon check = new ImageIcon("./img/check.png");;
    ImageIcon cross = new ImageIcon("./img/cross.png");;
    ImageIcon square = new ImageIcon("./img/square.png");;
    ImageIcon image = new ImageIcon("./img/tic-tac-toe.png");
    
    PlayGround() throws Exception{

        myResize(check, "./img/check.png");
        myResize(cross, "./img/cross.png");
        myResize(square, "./img/square.png");
        for(int i = 0; i < 9; i++) {
            label[i] = new JLabel();
            label[i].setSize(100,100);
            label[i].addMouseListener(this);
            label[i].setIcon(square);
        }

        
        for(int i = 0; i < 9; i++)
            this.add(label[i]);

        //this.setResizable(false);
        this.setTitle("Playing");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setSize(350, 350);
        this.setLayout(new GridLayout(3, 3, 5, 5));

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(255, 255, 204));
    } 

    public void mouseClicked(MouseEvent e) {
        for(int i = 0; i < 9; i++)
            if(e.getSource() == label[i]) {
                label[i].setIcon(check);
            }
    }

    public void mousePressed(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    
    void myResize(ImageIcon icon, String img) throws IOException{
        //resize and update icon
        BufferedImage temp = null;
        temp = ImageIO.read(new File(img));
        Image resized = temp.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon.setImage(resized);
    }
}