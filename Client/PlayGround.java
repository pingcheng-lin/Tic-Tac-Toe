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
    ImageIcon check = new ImageIcon("./img/check.png");;
    ImageIcon cross = new ImageIcon("./img/cross.png");;
    ImageIcon square = new ImageIcon("./img/square.png");;
    ImageIcon image = new ImageIcon("./img/tic-tac-toe.png");
    String type;
    boolean win = true;
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
            frame.add(label[i]);

        //frame.setResizable(false);
        frame.setTitle("Playing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(350, 350);
        frame.setLayout(new GridLayout(3, 3, 5, 5));

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(255, 255, 204));

        String priority = input.readUTF();
        type = input.readUTF();
        if(priority.equals("second"))
            for(int i = 0; i < 9; i++)
                label[i].setEnabled(false);
        while(true) {
            int beChanged = Integer.parseInt(input.readUTF());
        }
    } 

    public void mouseClicked(MouseEvent e) {
        try {
            for(int i = 0; i < 9; i++)
                if(e.getSource() == label[i]) {
                    label[i].setIcon(check);
                    output.writeUTF(Integer.toString(i));
                    output.flush();
                }
            String which = input.readUTF();
            if(type.equals("check"))
                label[Integer.parseInt(which)].setIcon(check);
            else
                label[Integer.parseInt(which)].setIcon(cross);
            for(int i = 0; i < 9; i++)
                label[i].setEnabled(false);
        } catch(Exception err) {}
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