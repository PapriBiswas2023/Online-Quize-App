package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class UserDashboard extends JFrame implements ActionListener {
    JComboBox<String> topicList;
    JButton startQuiz, logout;
    String userName;
    JPanel background;
    JLabel heading;

    public UserDashboard(String userName) {
        this.userName = userName;

        setTitle("Quiz Time! - User Dashboard");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // App icon
        setIconImage(new ImageIcon("icons/Home.jpeg").getImage());

        // Gradient background
        background = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(58, 123, 213), 0, getHeight(),
                        new Color(58, 96, 115));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new BorderLayout());
        setContentPane(background);

        heading = new JLabel("Welcome, " + userName, SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        background.add(heading, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        background.add(centerPanel, BorderLayout.CENTER);

        JPanel profilePanel = buildProfilePanel();
        centerPanel.add(profilePanel, BorderLayout.WEST);

        JPanel quizPanel = buildQuizPanel();
        centerPanel.add(quizPanel, BorderLayout.CENTER);

        JPanel attemptPanel = buildAttemptPanel();
        background.add(attemptPanel, BorderLayout.SOUTH);

        loadTopicsFromFile();
        setVisible(true);
    }

    private JPanel buildProfilePanel() {
        JPanel profilePanel = new JPanel(null);
        profilePanel.setPreferredSize(new Dimension(400, 250));
        profilePanel.setBackground(new Color(255, 255, 255, 100));

        ImageIcon originalIcon = new ImageIcon("icons/avatar.png");
        Image scaled = originalIcon.getImage().getScaledInstance(100, 100,
                Image.SCALE_SMOOTH);
        JLabel avatar = new JLabel(new ImageIcon(scaled));
        avatar.setBounds(20, 20, 100, 100);
        profilePanel.add(avatar);

        Map<String, Object> stats = getUserStats(userName);
        String email = userName.toLowerCase().replace(" ", "") + "@gmail.com";
        int totalQuizzes = (int) stats.get("attempts");
        int averageScore = (int) Math.round((double) stats.get("average"));

        JLabel emailLabel = new JLabel("Email: " + email);
        emailLabel.setBounds(140, 20, 250, 30);
        profilePanel.add(emailLabel);

        JLabel quizCount = new JLabel("Quizzes Attempted: " + totalQuizzes);
        quizCount.setBounds(140, 60, 250, 30);
        profilePanel.add(quizCount);

        JLabel scoreLabel = new JLabel("Average Score: " + averageScore + "%");
        scoreLabel.setBounds(140, 100, 250, 30);
        profilePanel.add(scoreLabel);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(averageScore);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(40, 167, 69));
        progressBar.setBounds(20, 150, 350, 30);
        profilePanel.add(progressBar);

        JLabel summary = new JLabel("<html><b>Summary:</b> You've attempted "
                + totalQuizzes + " quizzes.</html>");
        summary.setBounds(20, 190, 350, 40);
        profilePanel.add(summary);

        return profilePanel;
    }

    private JPanel buildQuizPanel() {
        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        quizPanel.setOpaque(false);
        quizPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(255, 255, 255, 200));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Topic label
        JLabel topicLabel = new JLabel("Select Quiz Topic:");
        topicLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topicLabel.setForeground(Color.DARK_GRAY);
        topicLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dropdown
        topicList = new JComboBox<>();
        topicList.setMaximumSize(new Dimension(300, 30));
        topicList.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add label and dropdown to a form group
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.add(topicLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(topicList);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        startQuiz = createStyledButton("Start Quiz", new Color(40, 167, 69));
        logout = createStyledButton("Logout", new Color(220, 53, 69));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(startQuiz);
        btnPanel.add(logout);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(btnPanel);

        quizPanel.add(cardPanel);

        return quizPanel;
    }

    private JPanel buildAttemptPanel() {
        Map<String, Object> stats = getUserStats(userName);
        @SuppressWarnings("unchecked")
        List<String> entries = (List<String>) stats.get("entries");

        JPanel attemptPanel = new JPanel();
        attemptPanel.setLayout(new BorderLayout());
        attemptPanel.setBackground(new Color(255, 255, 255, 120));
        attemptPanel.setBorder(BorderFactory.createEmptyBorder(-5, 20, 10, 20));

        JLabel lbl = new JLabel("Your Previous Attempts");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 0));
        attemptPanel.add(lbl, BorderLayout.NORTH);

        String[][] data;
        if (entries == null || entries.isEmpty()) {
            data = new String[][] { { "1", "No past attempts found." } };
        } else {
            data = new String[entries.size()][2];
            for (int i = 0; i < entries.size(); i++) {
                data[i][0] = String.valueOf(i + 1);
                data[i][1] = entries.get(i);
            }
        }

        String[] cols = { "#", "Attempt Details" };
        JTable table = new JTable(data, cols);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        table.setEnabled(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 250)); 
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);
        centerPanel.add(scroll, BorderLayout.SOUTH); 

        attemptPanel.add(centerPanel, BorderLayout.CENTER);
        return attemptPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
                button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2),
                    BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
                button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
            }
        });

        button.addActionListener(this);
        return button;
    }

    private void loadTopicsFromFile() {
        Set<String> topics = new LinkedHashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("examquestion.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Topic:")) {
                    topics.add(line.substring(6).trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load topics.");
        }
        for (String t : topics) topicList.addItem(t);
        if (topicList.getItemCount() == 0) {
            topicList.addItem("No topics available");
            startQuiz.setEnabled(false);
        }
    }

    private Map<String, Object> getUserStats(String user) {
        Map<String, Object> stats = new HashMap<>();
        List<String> userResults = new ArrayList<>();
        int totalCorrect = 0, totalQuestions = 0;

        File resultFile = new File("results.txt");
        if (resultFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(resultFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(user + " |")) {
                        userResults.add(line);
                        int scoreIdx = line.indexOf("Score:");
                        if (scoreIdx != -1) {
                            String part = line.substring(scoreIdx + 6, line.indexOf("|", scoreIdx)).trim();
                            String[] p = part.split("/");
                            if (p.length == 2) {
                                try {
                                    totalCorrect += Integer.parseInt(p[0].trim());
                                    totalQuestions += Integer.parseInt(p[1].trim());
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }
            } catch (IOException ignored) {}
        }

        double avg = (totalQuestions > 0) ? ((double) totalCorrect / totalQuestions) * 100 : 0;
        stats.put("attempts", userResults.size());
        stats.put("correct", totalCorrect);
        stats.put("total", totalQuestions);
        stats.put("average", avg);
        stats.put("entries", userResults);
        return stats;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == startQuiz) {
            String topic = (String) topicList.getSelectedItem();
            if (topic != null && !topic.equals("No topics available")) {
                setVisible(false);
                new Rules(userName, topic);
            }
        } else if (ae.getSource() == logout) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboard("Roni Seikh"));
    }
}
