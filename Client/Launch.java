import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Vector;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class Launch extends Client implements ActionListener {
    JFrame frame = new JFrame();
    JTextField textField;
    JButton submitButton;
    JButton updateButton;
    JButton playButton;
    Vector<JRadioButton> player = new Vector<JRadioButton>();
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

        playButton = new JButton("Play");
        playButton.addActionListener(this);

        playButton.setEnabled(false);
        updateButton.setEnabled(false);
        frame.add(textField);
        frame.add(submitButton);
        frame.add(updateButton);
        frame.add(playButton);
        frame.setTitle("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);//make frame visible
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(255, 255, 204));

    
    } 


    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getSource() == submitButton) {
                //submit name
                System.out.println(textField.getText());
                output.writeUTF(textField.getText());
                output.flush();
                submitButton.setEnabled(false);
                textField.setEditable(false);
                updateButton.setEnabled(true);
                playButton.setEnabled(true);
                //update list
                output.writeUTF("update");
                output.flush();
                String command = input.readUTF();
                String[] names = command.split("\\s");
                System.out.println(command);
                for(int i = 1; i < names.length; i++) {
                    player.add(new JRadioButton(names[i]));
                    frame.add(player.get(i-1));
                }
                frame.revalidate();
            }
            else if(e.getSource() == updateButton) {
                output.writeUTF("update");
                output.flush();
                String command = input.readUTF();
                String[] names = command.split("\\s");
                for(int i = 0; i < player.size(); i++) {
                    frame.remove(player.get(i));
                }
                player.clear();
                System.out.println(player.size());
                
                for(int i = 1; i < names.length; i++) {
                    player.add(new JRadioButton(names[i]));
                    frame.add(player.get(i-1));
                }
                frame.revalidate();
            }
            else if(e.getSource() == playButton) {
                output.writeUTF("play");
                output.flush();

            }
        } catch(Exception err) {}
    }
}