package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserRegister extends JFrame implements ActionListener {
    JTextField tfName, tfEmail;
    JPasswordField pfPassword, pfConfirmPassword;
    JCheckBox showPassword;
    JLabel heading;
    JButton register, back;
    int headingX = 600;

    public UserRegister() {
        setTitle("Quiz Time! - User Registration");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("icons/Home.jpeg").getImage());

        // Gradient background
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(58, 123, 213), 0, getHeight(), new Color(58, 96, 115));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        // Logo Image
        JLabel imageLabel = new JLabel(new ImageIcon(
                new ImageIcon("icons/Home.jpeg").getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH)));
        imageLabel.setBounds(150, 200, 400, 400); 
        mainPanel.add(imageLabel);

        
        // Heading Animation
        heading = new JLabel("For Accessing Quiz Please Register");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 46));
        heading.setForeground(Color.WHITE);
        heading.setBounds(300, 100, 800, 80);
        mainPanel.add(heading);

        // Form panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(255, 255, 255, 240));
        formPanel.setBounds(550, 200, 500, 400);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        mainPanel.add(formPanel);

        JLabel heading = new JLabel("User Registration");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(33, 37, 41));
        heading.setBounds(130, 20, 300, 40);
        formPanel.add(heading);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameLabel.setBounds(50, 80, 100, 25);
        formPanel.add(nameLabel);

        tfName = new JTextField();
        tfName.setBounds(180, 80, 250, 30);
        tfName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(tfName);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setBounds(50, 130, 100, 25);
        formPanel.add(emailLabel);

        tfEmail = new JTextField();
        tfEmail.setBounds(180, 130, 250, 30);
        tfEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(tfEmail);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setBounds(50, 180, 100, 25);
        formPanel.add(passwordLabel);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(180, 180, 250, 30);
        pfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(pfPassword);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmLabel.setBounds(50, 230, 140, 25);
        formPanel.add(confirmLabel);

        pfConfirmPassword = new JPasswordField();
        pfConfirmPassword.setBounds(180, 230, 250, 30);
        pfConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(pfConfirmPassword);

        showPassword = new JCheckBox("Show Passwords");
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        showPassword.setBounds(180, 270, 160, 25);
        showPassword.setOpaque(false);
        showPassword.addActionListener(e -> {
            boolean visible = showPassword.isSelected();
            pfPassword.setEchoChar(visible ? (char) 0 : '•');
            pfConfirmPassword.setEchoChar(visible ? (char) 0 : '•');
        });
        formPanel.add(showPassword);

        // Buttons
        register = new JButton("Register");
        register.setBounds(180, 320, 130, 40);
        register.setBackground(new Color(40, 167, 69)); 
        register.setForeground(Color.WHITE);
        register.setFont(new Font("Segoe UI", Font.BOLD, 16));
        register.setFocusPainted(false);
        register.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        register.addActionListener(this);
        formPanel.add(register);

        back = new JButton("Back");
        back.setBounds(320, 320, 110, 40);
        back.setBackground(new Color(220, 53, 69)); 
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setFocusPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(this);
        formPanel.add(back);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            String name = tfName.getText().trim();
            String email = tfEmail.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();
            String confirmPassword = new String(pfConfirmPassword.getPassword()).trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("userdetails.txt", true))) {
                String hashedPassword = hashPassword(password);
                writer.write(email + "," + hashedPassword + "," + name);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "✅ User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new Home();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "❌ Error saving user data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
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
        SwingUtilities.invokeLater(UserRegister::new);
    }
}
