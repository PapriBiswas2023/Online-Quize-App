package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener {
    JButton userRegister, userLogin, adminLogin;
    JTextField dummyField;
    JLabel heading;
    int headingX = 600;

    public Home() {
        setTitle("Quiz Time! - Home");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // App Icon
        setIconImage(new ImageIcon("icons/Home.jpeg").getImage());

        // Gradient background design
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(58), 0, getHeight(), new Color(58, 96, 115));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(null);
        setContentPane(background);

        // Logo Image
        JLabel imageLabel = new JLabel(new ImageIcon(
                new ImageIcon("icons/Home.jpeg").getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH)));
        imageLabel.setBounds(200, 180, 400, 400); 
        background.add(imageLabel);

        // Heading Animation
        heading = new JLabel("Welcome to Quiz Time!");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 46));
        heading.setForeground(Color.WHITE);
        heading.setBounds(headingX, 80, 800, 60);
        background.add(heading);
        animateHeading();

        // Button Panel Style
        JPanel buttonPanel = new JPanel(null);
<<<<<<< HEAD
        buttonPanel.setBackground(new Color(255, 255, 255, 40));
        buttonPanel.setOpaque(false);
=======
        buttonPanel.setBackground(new Color(255, 255, 255, 20));
>>>>>>> 8d87d84798bca6852d8d47bc6c4a933809f467de
        buttonPanel.setBounds(600, 180, 500, 400);
        buttonPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        background.add(buttonPanel);

        JLabel menuTitle = new JLabel("Main Menu");
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        menuTitle.setForeground(new Color(255, 255, 255));
        menuTitle.setBounds(170, 30, 200, 40);
        buttonPanel.add(menuTitle);

        // User Register
        userRegister = createStyledButton("User Register", new Color(0, 123, 255));
        userRegister.setBounds(150, 100, 200, 45);
        buttonPanel.add(userRegister);

        // User Login
        userLogin = createStyledButton("User Login", new Color(40, 167, 69));
        userLogin.setBounds(150, 170, 200, 45);
        buttonPanel.add(userLogin);

        // Admin Login
        adminLogin = createStyledButton("Admin Login", new Color(255, 193, 7));
        adminLogin.setBounds(150, 240, 200, 45);
        buttonPanel.add(adminLogin);

        // Add listeners
        userRegister.addActionListener(this);
        userLogin.addActionListener(this);
        adminLogin.addActionListener(this);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
                heading.setBounds(headingX, 80, 800, 60);
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        if (e.getSource() == userRegister) {
            new UserRegister();
        } else if (e.getSource() == userLogin) {
            new UserLogin();
        } else if (e.getSource() == adminLogin) {
            new AdminLogin();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
