package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Rules extends JFrame implements ActionListener {

    String name;
    String topic;
    JButton start, back;
    JLabel heading;
    int headingX = 600;

    public Rules(String name, String topic) {
        this.name = name;
        this.topic = topic;

        setTitle("Quiz Time! - Rules");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Exit quiz rules?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // App Icon
        setIconImage(new ImageIcon("icons/Home.jpeg").getImage());

        // Gradient Background
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(58, 123, 213), 0, getHeight(), new Color(58, 96, 115));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(null);
        setContentPane(background);

        heading = new JLabel(name + " Read The Instruction Carefully");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        heading.setForeground(Color.WHITE);
        heading.setBounds(headingX, 0, 1000, 60);
        background.add(heading);
        animateHeading();

        // Topic Label
        JLabel topicLabel = new JLabel("Quiz Topic: " + topic);
        topicLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topicLabel.setForeground(Color.YELLOW);
        topicLabel.setBounds(100, 140, 800, 40);
        background.add(topicLabel);

        // Rules Box
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(null);
        rulesPanel.setBounds(100, 200, 1200, 400);
        rulesPanel.setBackground(new Color(255, 255, 255, 230));
        rulesPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        background.add(rulesPanel);

        JLabel rules = new JLabel();
        rules.setBounds(30, 20, 1140, 360);
        rules.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        rules.setText(
                        "<html>" +
                                "1. Read each question carefully before selecting your answer.<br><br>" +
                                "2. Use 50-50 life-line for helping but only one time.<br><br>" +
                                "3. Each question has only one correct answer.<br><br>" +
                                "4. Once you select an answer and move on, you cannot go back.<br><br>" +
                                "5. There is a time limit for the quiz. Manage your time wisely.<br><br>" +
                                "6. Do not close the quiz window or refresh the page during the quiz.<br><br>" +
                                "7. Click 'Submit' after completing the quiz to save your responses.<br>" +
                        "</html>"
        );
        rulesPanel.add(rules);

        start = createStyledButton("Start Quiz", new Color(40, 167, 69));
        start.setBounds(800, 630, 200, 50);
        background.add(start);

        back = createStyledButton("Back", new Color(220, 53, 69));
        back.setBounds(550, 630, 200, 50);
        background.add(back);

        start.addActionListener(this);
        back.addActionListener(this);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void animateHeading() {
        Timer timer = new Timer(20, e -> {
            if (headingX > 400) {
                headingX -= 2;
                heading.setBounds(headingX, 60, 1000, 60);
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == start) {
            setVisible(false);
            new QuizScreen(name, topic); 
        } else {
            setVisible(false);
            new UserDashboard(name); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Rules("User", "Java Basics"));
    }
}
