package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserLogin extends JFrame implements ActionListener {
    JTextField tfEmail;
    JPasswordField pfPassword;
    JButton login, back;
    JCheckBox showPassword;  
    JLabel heading;
    int headingX = 600;

    public UserLogin() {
        setTitle("Quiz Time! - User Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Set App Icon
        setIconImage(new ImageIcon("icons/Home.jpeg").getImage());

        // Gradient background
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
        heading = new JLabel("For Accessing Quiz Please Login");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 46));
        heading.setForeground(Color.WHITE);
        heading.setBounds(300, 100, 800, 80);
        background.add(heading);

        // Login Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(255, 255, 255, 40));
        formPanel.setOpaque(false);
        formPanel.setBounds(550, 200, 500, 400);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        background.add(formPanel);

        JLabel heading = new JLabel("User Login");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(255, 255, 255));
        heading.setBounds(170, 30, 200, 30);
        formPanel.add(heading);

        JLabel email = new JLabel("Email:");
        email.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        email.setForeground(new Color(255, 255, 255));
        email.setBounds(50, 100, 100, 25);
        formPanel.add(email);

        tfEmail = new JTextField();
        tfEmail.setBounds(150, 100, 280, 30);
        formPanel.add(tfEmail);

        JLabel password = new JLabel("Password:");
        password.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        password.setForeground(new Color(255, 255, 255));
        password.setBounds(50, 160, 100, 25);
        formPanel.add(password);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(150, 160, 280, 30);
        formPanel.add(pfPassword);

        // Show Password Checkbox
        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(150, 195, 150, 25);
        showPassword.setBackground(new Color(255, 255, 255, 230));
        formPanel.add(showPassword);

        showPassword.addActionListener(_ -> {
            if (showPassword.isSelected()) {
                pfPassword.setEchoChar((char) 0); 
            } else {
                pfPassword.setEchoChar('●'); 
            }
        });

        // Login Button
        login = new JButton("Login");
        login.setBounds(150, 240, 130, 40);
        login.setBackground(new Color(40, 167, 69)); 
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Segoe UI", Font.BOLD, 16));
        login.setFocusPainted(false);
        login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(login);

        // Back Button
        back = new JButton("Back");
        back.setBounds(300, 240, 130, 40);
        back.setBackground(new Color(220, 53, 69)); 
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setFocusPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(back);

        login.addActionListener(this);
        back.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            String email = tfEmail.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();
            String hashedPassword = hashPassword(password);
            boolean valid = false;
            String name = "";

            try (BufferedReader reader = new BufferedReader(new FileReader("userdetails.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3 && parts[0].equals(email) && parts[1].equals(hashedPassword)) {
                        valid = true;
                        name = parts[2];
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (valid) {
                JOptionPane.showMessageDialog(this, "Welcome, " + name);
                setVisible(false);
                new UserDashboard(name);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }

        } else if (e.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserLogin::new);
    }
}
