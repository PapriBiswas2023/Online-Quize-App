package quiz.application;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Score extends JFrame implements ActionListener {

    JButton playAgainButton, exitButton, backButton;
    String userName;

    Score(String name, String topic, int score, int total) {
        userName = name;

        getContentPane().setBackground(Color.WHITE);
        setTitle("Quiz Time! - Score " + topic);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon("icons/Home.jpeg").getImage());

        // Score image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/score.png"));
        Image i2 = i1.getImage().getScaledInstance(300, 250, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(100, 100, 300, 250);
        add(image);

        // Heading
        JLabel heading = new JLabel("Thank you " + name + " for playing Quiz Time!");
        heading.setBounds(450, 30, 800, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(heading);

        // Score display
        JLabel lblscore = new JLabel("Your score: " + score + " out of " + total);
        lblscore.setBounds(550, 200, 400, 30);
        lblscore.setFont(new Font("Tahoma", Font.PLAIN, 26));
        add(lblscore);

        // Back to Dashboard button
        backButton = new JButton("Back to Dashboard");
        backButton.setBounds(550, 270, 180, 40);
        backButton.setBackground(new Color(34, 139, 34));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setToolTipText("Return to your dashboard");
        backButton.addActionListener(this);
        add(backButton);

        // Exit button
        exitButton = new JButton("Exit");
        exitButton.setBounds(760, 270, 100, 40);
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        exitButton.setToolTipText("Exit the application");
        exitButton.addActionListener(this);
        add(exitButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton) {
            setVisible(false);
            new UserDashboard(userName); 
        } else if (ae.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
