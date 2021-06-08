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
    ButtonGroup group = new ButtonGroup(); 
    ImageIcon image = new ImageIcon("./img/tic-tac-toe.png");
    Launch() throws Exception{
        //text
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(250,40));
        textField.setFont(new Font("Arial", Font.BOLD, 35));
        textField.setForeground(new Color(0x404040));
        textField.setBackground(new Color(204, 255, 204));
        textField.setCaretColor(Color.GRAY);
        textField.setText("Default");

        //submit
        submitButton = new JButton("Submit my name");
        submitButton.addActionListener(this);

        //update
        updateButton = new JButton("Update List");
        updateButton.addActionListener(this);

        //play
        playButton = new JButton("Play");
        playButton.addActionListener(this);

        //disable two buttons for latter use
        playButton.setEnabled(false);
        updateButton.setEnabled(false);

        //build the frame
        frame.add(textField);
        frame.add(submitButton);
        frame.add(updateButton);
        frame.add(playButton);
        frame.setTitle("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,300);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); //make frame visible
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
                //disable used items
                submitButton.setEnabled(false);
                textField.setEditable(false);
                //enable want to use items
                updateButton.setEnabled(true);
                playButton.setEnabled(true);
                //update list
                toUpdateList();
            }
            else if(e.getSource() == updateButton) {
                toUpdateList();
            }
            else if(e.getSource() == playButton) {
                for(JRadioButton i: player) {
                    //see which radio button is selected
                    if(i.isSelected()) {
                        //enable everthing
                        updateButton.setEnabled(false);
                        playButton.setEnabled(false);
                        for(JRadioButton j: player)
                            j.setEnabled(false);

                        //get radio button syntax
                        String tt = i.getText();

                        //remove score, remain original name
                        String[] temp = tt.split("\\(");
                        output.writeUTF("play " + temp[0]);
                        output.flush();

                        //wait opponent and take check or cross
                        Client.check_or_cross = input.readUTF(); 
                        PlayGround start = new PlayGround();
                    }
                }
            }
        } catch(Exception err) {}
    }
    void toUpdateList() throws Exception{
        //tell server want to update enemy list
        output.writeUTF("update");
        output.flush();
        //read update list
        String command = input.readUTF();
        String[] names = command.split("\\s");
        for(int i = 0; i < player.size(); i++) {
            frame.remove(player.get(i));
        }
        player.clear();

        for(int i = 1; i < names.length; i++) {
            player.add(new JRadioButton(names[i]));
            group.add(player.get(i-1));
            frame.add(player.get(i-1));
        }
        frame.revalidate();
    }
}