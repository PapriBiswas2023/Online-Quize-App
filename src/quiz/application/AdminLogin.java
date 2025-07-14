package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLogin extends JFrame implements ActionListener {
    JTextField tfEmail;
    JPasswordField pfPassword;
    JButton login, back;
    JCheckBox showPassword; 
    JLabel heading;
    int headingX = 600;

    final String adminEmail = "admin@papri.com";
    final String adminPassword = "admin@123";

    public AdminLogin() {
        setTitle("Quiz Time! - Admin Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        // Set App Icon from home page
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

        // Image 
        ImageIcon icon = new ImageIcon("icons/Home.jpeg");
        Image img = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBounds(150, 200, 400, 400);
        background.add(imageLabel);

        // Heading Animation
        heading = new JLabel("Admin Login Page");
        heading.setFont(new Font("cursive", Font.BOLD, 46));
        heading.setForeground(Color.WHITE);
        heading.setBounds(100, 100, 1200, 80);
        background.add(heading);

        // Login Panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(255, 255, 255, 80));
        formPanel.setOpaque(false);


        formPanel.setBounds(550, 200, 500, 400);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        background.add(formPanel);

        JLabel heading = new JLabel("Admin Login Panel");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(255, 255, 255));
        heading.setBounds(120, 30, 300, 40);
        formPanel.add(heading);

        // Email
        JLabel email = new JLabel("Email:");
        email.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        email.setForeground(new Color(255, 255, 255));
        email.setBounds(50, 100, 100, 25);
        formPanel.add(email);

        tfEmail = new JTextField();
        tfEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tfEmail.setBounds(150, 100, 280, 30);
        formPanel.add(tfEmail);

        // Password
        JLabel password = new JLabel("Password:");
        password.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        password.setForeground(new Color(255, 255, 255));
        password.setBounds(50, 160, 100, 25);
        formPanel.add(password);

        pfPassword = new JPasswordField();
        pfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pfPassword.setBounds(150, 160, 280, 30);
        formPanel.add(pfPassword);

        // Show Password Checkbox
        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(150, 195, 150, 25);
        showPassword.setBackground(new Color(255, 255, 255, 230));
        formPanel.add(showPassword);

        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    pfPassword.setEchoChar((char) 0); 
                } else {
                    pfPassword.setEchoChar('●'); 
                }
            }
        });

        // Login Button
        login = new JButton("Login");
        login.setFont(new Font("Segoe UI", Font.BOLD, 16));
        login.setBounds(150, 230, 130, 40);
        login.setBackground(new Color(40, 167, 69)); // Green
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        login.addActionListener(this);
        formPanel.add(login);

        // Back Button
        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setBounds(300, 230, 130, 40);
        back.setBackground(new Color(220, 53, 69)); 
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(this);
        formPanel.add(back);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            String email = tfEmail.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();

            if (email.equals(adminEmail) && password.equals(adminPassword)) {
                JOptionPane.showMessageDialog(this, "✅ Admin login successful!");
                setVisible(false);
                new AdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid admin credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogin::new);
    }
}
