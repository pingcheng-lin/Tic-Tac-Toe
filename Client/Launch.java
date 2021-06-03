import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class Launch extends Client implements ActionListener {
    JFrame frame = new JFrame();
    JTextField textField;
    JButton submitButton;
    JButton updateButton;
    JComboBox<String> comboBox;
    ImageIcon image = new ImageIcon("./img/tic-tac-toe.png");
    
    Launch() throws Exception{

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(250,40));
        textField.setFont(new Font("Arial", Font.BOLD, 35));
        textField.setForeground(new Color(0x404040));
        textField.setBackground(new Color(204, 255, 204));
        textField.setCaretColor(Color.GRAY);
        textField.setText("Default");

        submitButton = new JButton("Submit my name");
        submitButton.addActionListener(this);

        updateButton = new JButton("Update List");
        updateButton.addActionListener(this);

        String[] animals = {"", "Random", "dog", "cat", "bird"};
        try {
            comboBox = new JComboBox<>(animals);
            comboBox.addActionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        comboBox.setEnabled(false);
        updateButton.setEnabled(false);
        //comboBox.addItem("123");
        //comboBox.removeItem("123");

        frame.add(textField);
        frame.add(submitButton);
        frame.add(updateButton);
        frame.add(comboBox);
        frame.setTitle("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(500,500);
        frame.setLayout(new FlowLayout());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);//make frame visible
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(255, 255, 204));
    } 


    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getSource() == submitButton) {
                System.out.println(textField.getText());
                output.writeUTF(textField.getText());
                output.flush();
                submitButton.setEnabled(false);
                textField.setEditable(false);
                comboBox.setEnabled(true);
                updateButton.setEnabled(true);
            }
            else if(e.getSource() == updateButton) {
                System.out.println("update");
                output.writeUTF("update");
                output.flush();
            }
            else if(e.getSource() == comboBox) {
                comboBox.setEnabled(false);
                updateButton.setEnabled(false);
                PlayGround ground = new PlayGround();
            }
        } catch(Exception err) {}
    }
}