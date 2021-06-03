import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class MyFrame extends JFrame implements ActionListener, MouseListener {
    JTextField textField;
    JButton submitButton;
    JComboBox<String> comboBox;
    JButton updateButton;
    JLabel label[] = new JLabel[9];
    ImageIcon check = new ImageIcon("check.png");;
    ImageIcon cross = new ImageIcon("cross.png");;
    ImageIcon square = new ImageIcon("square.png");;
    ImageIcon image = new ImageIcon("tic-tac-toe.png");
    
    MyFrame() throws Exception{

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(250,40));
        textField.setFont(new Font("Arial", Font.BOLD, 35));
        textField.setForeground(new Color(0x404040));
        textField.setBackground(new Color(204, 255, 204));
        textField.setCaretColor(Color.GRAY);
        textField.setText("Default");

        submitButton = new JButton("Submit my name");
        submitButton.addActionListener(this);


        updateButton = new JButton("Update");
        updateButton.addActionListener(this);


        String[] animals = {"", "Random", "dog", "cat", "bird"};
        try {
            comboBox = new JComboBox<>(animals);
            comboBox.addActionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //comboBox.addItem("123");
        //comboBox.removeItem("123");

        myResize(check, "check.png");
        myResize(cross, "cross.png");
        myResize(square, "square.png");
        for(int i = 0; i < 9; i++) {
            label[i] = new JLabel();
            label[i].setSize(100,100);
            label[i].addMouseListener(this);
            label[i].setIcon(square);
        }

        this.add(textField);
        this.add(submitButton);
        this.add(updateButton);
        this.add(comboBox);
        for(int i = 0; i < 9; i++)
            this.add(label[i]);
        //this.setResizable(false);
        this.setTitle("Tic-Tac-Toe");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLayout(new FlowLayout());
        //this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);//make frame visible
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(255, 255, 204));
    } 


    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == submitButton) {
            System.out.println(textField.getText());
            submitButton.setEnabled(false);
            textField.setEditable(false);
        }
        if(e.getSource() == comboBox) {
            System.out.println(comboBox.getSelectedItem());
        }
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("213");
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