package quiz.application;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class AdminDashboard extends JFrame implements ActionListener {
    JTextField tfTopic, tfQuestion, tfOption1, tfOption2, tfOption3, tfOption4, tfCorrect;
    JTextArea statusArea;
    JPanel sidebar, mainPanel;
    JButton addQuestion, editQuiz, deleteQuiz, viewQuiz, viewUsers, viewResults, saveAndExit;
    JButton btnAdd, btnSave, btnBackAdd, btnBackEdit, btnBackDelete, btnBackView;
    CardLayout cardLayout;

    public AdminDashboard() {
        setTitle("Quiz Time! - Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            setIconImage(new ImageIcon("icons/Home.jpeg").getImage());
        } catch (Exception ignored) {}

        sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(54, 69, 79));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(240, getHeight()));

        panel.add(Box.createVerticalStrut(30));

        JLabel iconLabel = new JLabel(new ImageIcon(new ImageIcon("icons/Home.jpeg")
                .getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH)));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(45));

        panel.add(createSidebarButton("\u2795 Add Question", addQuestion = new JButton(), new Color(46, 204, 113)));
        panel.add(Box.createVerticalStrut(25));
        panel.add(createSidebarButton("\u270F\uFE0F Edit Quiz", editQuiz = new JButton(), new Color(52, 152, 219)));
        panel.add(Box.createVerticalStrut(25));
        panel.add(createSidebarButton("\uD83D\uDDD1 Delete Quiz", deleteQuiz = new JButton(), new Color(231, 76, 60)));
        panel.add(Box.createVerticalStrut(25));
        panel.add(createSidebarButton("\uD83D\uDC41 View Quiz", viewQuiz = new JButton(), new Color(26, 188, 156)));
        panel.add(Box.createVerticalStrut(25));
        panel.add(createSidebarButton("\uD83D\uDC65 View Users", viewUsers = new JButton(), new Color(155, 89, 182)));
        panel.add(Box.createVerticalStrut(25));
        panel.add(createSidebarButton("\uD83D\uDCCA View Results", viewResults = new JButton(), new Color(241, 196, 15)));
        panel.add(Box.createVerticalStrut(25));
        panel.add(createSidebarButton("\uD83D\uDCBE Save & Exit", saveAndExit = new JButton(), new Color(230, 126, 34)));

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createSidebarButton(String text, JButton btn, Color baseColor) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setMaximumSize(new Dimension(200, 45));
        wrapper.setBackground(new Color(44, 62, 80));
        wrapper.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 1));

        btn.setText(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 10));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        Color hoverColor = baseColor.darker();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(baseColor); }
        });

        btn.addActionListener(this);
        wrapper.add(btn, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createMainPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);

        panel.add(createDefaultPanel(), "default");
        panel.add(createAddQuestionPanel(), "addQuestion");
        panel.add(createEditQuizPanel(), "editQuiz");
        panel.add(createDeleteQuizPanel(), "deleteQuiz");
        panel.add(createViewQuizPanel(), "viewQuiz");
        panel.add(createStyledTextPanel(readFile("userdetails.txt"), "\uD83D\uDC65 Registered Users"), "viewUsers");
        panel.add(createStyledTextPanel(readFile("results.txt"), "\uD83D\uDCCA Quiz Results"), "viewResults");

        cardLayout.show(panel, "default");
        return panel;
    }

    private JPanel createStyledTextPanel(String content, String titleText) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(44, 62, 80));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea(content);
        area.setFont(new Font("Consolas", Font.PLAIN, 15));
        area.setForeground(Color.DARK_GRAY);
        area.setEditable(false);
        area.setBackground(new Color(245, 245, 245));
        area.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JButton backBtn = new JButton("\u2B05 Back");
        styleButton(backBtn, new Color(241, 196, 15));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "default"));

        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.add(backBtn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDefaultPanel() {
        // Header image
        JLabel image = new JLabel(new ImageIcon(new ImageIcon("icons/quiz.jpg")
            .getImage().getScaledInstance(1200, 200, Image.SCALE_SMOOTH)));
        image.setHorizontalAlignment(JLabel.CENTER);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome, Admin!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(0, 102, 204));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subheading text
        JLabel subText = new JLabel("You have full access to manage quizzes, users, results, and settings.");
        subText.setFont(new Font("Arial", Font.PLAIN, 22));
        subText.setForeground(Color.DARK_GRAY);
        subText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Additional info
        JLabel infoText = new JLabel("<html><div style='text-align: center;'>"
            + "Use the sidebar to navigate between modules.<br>"
            + "Here you can create new quizzes, view user statistics, manage user accounts, "
            + "and configure the application settings."
            + "</div></html>");
        infoText.setFont(new Font("Arial", Font.PLAIN, 20));
        infoText.setForeground(Color.GRAY);
        infoText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Feature list
        JTextArea features = new JTextArea(
            "Key Features:\n"
            + "• Create, update, and delete quizzes\n"
            + "• View and analyze quiz results\n"
            + "• Manage registered users\n"
            + "• Customize application settings\n"
            + "• Export reports and statistics"
        );
        features.setFont(new Font("Arial", Font.PLAIN, 20));
        features.setForeground(Color.DARK_GRAY);
        features.setEditable(false);
        features.setOpaque(false);
        features.setAlignmentX(Component.CENTER_ALIGNMENT);
        features.setMargin(new Insets(20, 40, 20, 40));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        centerPanel.add(Box.createVerticalGlue()); 
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(subText);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(infoText);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(features);
        centerPanel.add(Box.createVerticalGlue());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(image, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPanelWithTopImage(JComponent innerContent, JButton backButton) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel image = new JLabel(new ImageIcon(new ImageIcon("icons/quiz.jpg")
                .getImage().getScaledInstance(1200, 200, Image.SCALE_SMOOTH)));
        image.setHorizontalAlignment(JLabel.CENTER);
        panel.add(image, BorderLayout.NORTH);
        panel.add(innerContent, BorderLayout.CENTER);
        if (backButton != null) {
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
            footer.add(backButton);
            panel.add(footer, BorderLayout.SOUTH);
        }
        return panel;
    }

    private JPanel createAddQuestionPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("➕ Add New Question");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        formPanel.add(new JLabel("Quiz Topic:"), gbc);
        tfTopic = new JTextField(20);
        addHoverEffect(tfTopic);
        gbc.gridx = 1;
        formPanel.add(tfTopic, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Question:"), gbc);
        tfQuestion = new JTextField(40);
        addHoverEffect(tfQuestion);
        gbc.gridx = 1;
        formPanel.add(tfQuestion, gbc);

        String[] labels = {"Option A:", "Option B:", "Option C:", "Option D:"};
        JTextField[] options = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            formPanel.add(new JLabel(labels[i]), gbc);
            options[i] = new JTextField(30);
            addHoverEffect(options[i]);
            gbc.gridx = 1;
            formPanel.add(options[i], gbc);
        }

        tfOption1 = options[0]; tfOption2 = options[1];
        tfOption3 = options[2]; tfOption4 = options[3];

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Correct Option (A/B/C/D):"), gbc);
        tfCorrect = new JTextField(5);
        addHoverEffect(tfCorrect);
        gbc.gridx = 1;
        formPanel.add(tfCorrect, gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 2;
        statusArea = new JTextArea(6, 50);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        statusArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(new JScrollPane(statusArea), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAdd = new JButton("\u2795 Add");
        btnSave = new JButton("\uD83D\uDCBE Save");
        btnBackAdd = new JButton("\u2B05 Back");

        styleButton(btnAdd, new Color(52, 152, 219));
        styleButton(btnSave, new Color(46, 204, 113));
        styleButton(btnBackAdd, new Color(241, 196, 15));

        btnAdd.addActionListener(e -> {
            String topic = tfTopic.getText().trim();
            String question = tfQuestion.getText().trim();
            String optA = tfOption1.getText().trim();
            String optB = tfOption2.getText().trim();
            String optC = tfOption3.getText().trim();
            String optD = tfOption4.getText().trim();
            String correct = tfCorrect.getText().trim().toUpperCase();

            if (topic.isEmpty() || question.isEmpty() || optA.isEmpty() || optB.isEmpty() ||
                optC.isEmpty() || optD.isEmpty() || correct.isEmpty()) {
                statusArea.append("⚠️ Please fill in all fields.\n");
                return;
            }

            if (!correct.matches("[ABCD]")) {
                statusArea.append("⚠️ Correct option must be A, B, C, or D.\n");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("examquestion.txt", true))) {
                writer.write("Topic:" + topic + "\n");
                writer.write("Q:" + question + "\n");
                writer.write("A:" + optA + "\n");
                writer.write("B:" + optB + "\n");
                writer.write("C:" + optC + "\n");
                writer.write("D:" + optD + "\n");
                writer.write("Answer:" + correct + "\n\n");

                statusArea.append("✅ Question saved successfully.\n");

                tfQuestion.setText(""); tfOption1.setText(""); tfOption2.setText("");
                tfOption3.setText(""); tfOption4.setText(""); tfCorrect.setText("");

                JTextArea area = new JTextArea(readFile("examquestion.txt"));
                area.setFont(new Font("Consolas", Font.PLAIN, 14));
                area.setEditable(false);
                JScrollPane scroll = new JScrollPane(area);
                JPanel content = new JPanel(new BorderLayout());
                content.add(scroll, BorderLayout.CENTER);
                content.add(btnBackView, BorderLayout.SOUTH);
                JPanel viewPanel = createPanelWithTopImage(content, btnBackView);
                mainPanel.add(viewPanel, "viewQuiz");
                cardLayout.show(mainPanel, "viewQuiz");

            } catch (IOException ex) {
                statusArea.append("❌ Error saving question.\n");
            }
        });

        btnSave.addActionListener(e -> {
            statusArea.append("💾 Save clicked - already saved to file.\n");
        });

        btnBackAdd.addActionListener(e -> cardLayout.show(mainPanel, "default"));

        buttonPanel.add(btnAdd); buttonPanel.add(btnSave); buttonPanel.add(btnBackAdd);
        gbc.gridy++;
        formPanel.add(buttonPanel, gbc);

        return createPanelWithTopImage(formPanel, null);
    }

    private JPanel createViewQuizPanel() {
        List<Question> questions = parseQuestions("examquestion.txt");

        JPanel quizListPanel = new JPanel(new GridBagLayout());
        quizListPanel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        for (Question q : questions) {
            gbc.gridy = row++;
            quizListPanel.add(createQuizCard(q), gbc);
        }

        JScrollPane scrollPane = new JScrollPane(quizListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scroll

        btnBackView = new JButton("\u2B05 Back");
        styleButton(btnBackView, new Color(241, 196, 15));
        btnBackView.addActionListener(e -> cardLayout.show(mainPanel, "default"));

        JPanel container = new JPanel(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(btnBackView, BorderLayout.SOUTH);

        return createPanelWithTopImage(container, btnBackView);
    }

    private JPanel createEditQuizPanel() {
        List<Question> questions = parseQuestions("examquestion.txt");

        JPanel listPanel = new JPanel(new GridBagLayout());
        listPanel.setBackground(new Color(245, 245, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        for (Question q : questions) {
            JPanel card = createEditableQuizCard(q);
            gbc.gridy = row++;
            listPanel.add(card, gbc);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JButton btnSaveEdit = new JButton("\uD83D\uDCBE Save Changes");
        styleButton(btnSaveEdit, new Color(46, 204, 113));

        btnSaveEdit.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("examquestion.txt"))) {
                for (Component comp : listPanel.getComponents()) {
                    if (comp instanceof JPanel cardPanel) {
                        String topic = getFieldValue(cardPanel, 0);
                        String question = getFieldValue(cardPanel, 1);
                        String optA = getFieldValue(cardPanel, 2);
                        String optB = getFieldValue(cardPanel, 3);
                        String optC = getFieldValue(cardPanel, 4);
                        String optD = getFieldValue(cardPanel, 5);
                        String correct = getFieldValue(cardPanel, 6).toUpperCase();

                        if (topic.isEmpty() || question.isEmpty() || optA.isEmpty() || optB.isEmpty() ||
                                optC.isEmpty() || optD.isEmpty() || !correct.matches("[ABCD]")) {
                            JOptionPane.showMessageDialog(null, "⚠ Please fill all fields correctly.");
                            return;
                        }

                        writer.write("Topic:" + topic + "\n");
                        writer.write("Q:" + question + "\n");
                        writer.write("A:" + optA + "\n");
                        writer.write("B:" + optB + "\n");
                        writer.write("C:" + optC + "\n");
                        writer.write("D:" + optD + "\n");
                        writer.write("Answer:" + correct + "\n\n");
                    }
                }
                JOptionPane.showMessageDialog(null, "✅ All changes saved successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "❌ Error saving changes: " + ex.getMessage());
            }
        });

        btnBackEdit = new JButton("\u2B05 Back");
        styleButton(btnBackEdit, new Color(241, 196, 15));
        btnBackEdit.addActionListener(e -> cardLayout.show(mainPanel, "default"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.add(btnSaveEdit);
        buttonPanel.add(btnBackEdit);

        JPanel container = new JPanel(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        return createPanelWithTopImage(container, btnBackEdit);
    }

    private JPanel createDeleteQuizPanel() {
        List<Question> questions = parseQuestions("examquestion.txt");
        List<Question> deletedQuestions = new ArrayList<>();

        JPanel questionListPanel = new JPanel(new GridBagLayout());
        questionListPanel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        for (Question q : questions) {
            JPanel card = createDeletableQuizCard(q, questions, deletedQuestions, questionListPanel);
            gbc.gridy = row++;
            questionListPanel.add(card, gbc);
        }

        JScrollPane scrollPane = new JScrollPane(questionListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        btnBackDelete = new JButton("\u2B05 Back");
        styleButton(btnBackDelete, new Color(241, 196, 15));
        btnBackDelete.addActionListener(e -> cardLayout.show(mainPanel, "default"));

        JButton btnSaveDelete = new JButton("\uD83D\uDCBE Save Changes");
        styleButton(btnSaveDelete, new Color(46, 204, 113));

        btnSaveDelete.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("examquestion.txt"))) {
                for (Question q : questions) {
                    writer.write("Topic:" + q.topic + "\n");
                    writer.write("Q:" + q.question + "\n");
                    writer.write("A:" + q.optA + "\n");
                    writer.write("B:" + q.optB + "\n");
                    writer.write("C:" + q.optC + "\n");
                    writer.write("D:" + q.optD + "\n");
                    writer.write("Answer:" + q.correct + "\n\n");
                }
                JOptionPane.showMessageDialog(null, "✅ All changes saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "❌ Error saving changes.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.add(btnSaveDelete);
        buttonPanel.add(btnBackDelete);

        JPanel container = new JPanel(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        return createPanelWithTopImage(container, btnBackDelete);
    }

    private String readFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            content.append("\u26A0 Could not load file.");
        }
        return content.toString();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        Color hover = color.darker();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
            public void mouseExited(MouseEvent e) { button.setBackground(color); }
        });
    }

    private void addHoverEffect(JTextField field) {
    Border original = field.getBorder();
    field.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
            field.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        }
        public void focusLost(FocusEvent e) {
            field.setBorder(original);
        }
    });
}

    private JPanel createQuizCard(Question q) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        card.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JLabel topicLabel = new JLabel("📘 Topic: " + q.topic);
        topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topicLabel.setForeground(new Color(52, 152, 219));

        JLabel questionLabel = new JLabel("<html><b>Q:</b> " + q.question + "</html>");
        JLabel[] optionLabels = {
            new JLabel("A. " + q.optA),
            new JLabel("B. " + q.optB),
            new JLabel("C. " + q.optC),
            new JLabel("D. " + q.optD)
        };
        for (JLabel opt : optionLabels) {
            opt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }

        JLabel answerLabel = new JLabel("✅ Correct: " + q.correct);
        answerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        answerLabel.setForeground(new Color(39, 174, 96));

        card.add(topicLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(questionLabel);
        card.add(Box.createVerticalStrut(5));
        for (JLabel opt : optionLabels) card.add(opt);
        card.add(Box.createVerticalStrut(5));
        card.add(answerLabel);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 245, 255));
                card.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        return card;
    }

    private List<Question> parseQuestions(String file) {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Question q = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Topic:")) {
                    q = new Question();
                    q.topic = line.substring(6).trim();
                } else if (line.startsWith("Q:")) {
                    q.question = line.substring(2).trim();
                } else if (line.startsWith("A:")) {
                    q.optA = line.substring(2).trim();
                } else if (line.startsWith("B:")) {
                    q.optB = line.substring(2).trim();
                } else if (line.startsWith("C:")) {
                    q.optC = line.substring(2).trim();
                } else if (line.startsWith("D:")) {
                    q.optD = line.substring(2).trim();
                } else if (line.startsWith("Answer:")) {
                    q.correct = line.substring(7).trim();
                    questions.add(q); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

    class Question {
        String topic;
        String question;
        String optA, optB, optC, optD;
        String correct;
    }

    private JPanel createDeletableQuizCard(Question q, List<Question> allQuestions, List<Question> deleted, JPanel container) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        card.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JLabel topicLabel = new JLabel("📘 Topic: " + q.topic);
        topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topicLabel.setForeground(new Color(52, 152, 219));

        JLabel questionLabel = new JLabel("<html><b>Q:</b> " + q.question + "</html>");
        JLabel[] optionLabels = {
            new JLabel("A. " + q.optA),
            new JLabel("B. " + q.optB),
            new JLabel("C. " + q.optC),
            new JLabel("D. " + q.optD)
        };
        for (JLabel opt : optionLabels) {
            opt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }

        JLabel answerLabel = new JLabel("✅ Correct: " + q.correct);
        answerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        answerLabel.setForeground(new Color(39, 174, 96));

        JButton btnDelete = new JButton("🗑️ Delete");
        styleButton(btnDelete, new Color(231, 76, 60));
        btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnDelete.addActionListener(e -> {
            allQuestions.remove(q);
            deleted.add(q);
            container.remove(card);
            container.revalidate();
            container.repaint();
        });

        card.add(topicLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(questionLabel);
        card.add(Box.createVerticalStrut(5));
        for (JLabel opt : optionLabels) card.add(opt);
        card.add(Box.createVerticalStrut(5));
        card.add(answerLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(btnDelete);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(255, 250, 250));
                card.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        return card;
    }

    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == addQuestion) cardLayout.show(mainPanel, "addQuestion");
        else if (src == editQuiz) cardLayout.show(mainPanel, "editQuiz");
        else if (src == deleteQuiz) cardLayout.show(mainPanel, "deleteQuiz");
        else if (src == viewQuiz) cardLayout.show(mainPanel, "viewQuiz");
        else if (src == viewUsers) cardLayout.show(mainPanel, "viewUsers");
        else if (src == viewResults) cardLayout.show(mainPanel, "viewResults");
        else if (src == saveAndExit) {
            dispose();
            new Home(); 
        }
    }

    private JPanel createEditableQuizCard(Question q) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setMaximumSize(new Dimension(650, Integer.MAX_VALUE));

        JTextField tfTopic = new JTextField(q.topic);
        JTextField tfQuestion = new JTextField(q.question);
        JTextField tfA = new JTextField(q.optA);
        JTextField tfB = new JTextField(q.optB);
        JTextField tfC = new JTextField(q.optC);
        JTextField tfD = new JTextField(q.optD);
        JTextField tfCorrect = new JTextField(q.correct);

        JTextField[] allFields = {tfTopic, tfQuestion, tfA, tfB, tfC, tfD, tfCorrect};
        for (JTextField f : allFields) {
            f.setFont(new Font("SansSerif", Font.PLAIN, 14));
            f.setEditable(false);
            f.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            f.setBackground(new Color(248, 248, 248));
        }

        JButton btnEdit = new JButton("✏️ Edit");
        styleButton(btnEdit, new Color(52, 152, 219));
        btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnEdit.addActionListener(e -> {
            boolean isEditing = !tfTopic.isEditable();
            for (JTextField f : allFields) {
                f.setEditable(isEditing);
                f.setBackground(isEditing ? Color.WHITE : new Color(248, 248, 248));
            }
            btnEdit.setText(isEditing ? "✅ Done" : "✏️ Edit");
            card.repaint();
        });

        card.add(createFieldRow("📘 Topic:", tfTopic));
        card.add(createFieldRow("❓ Question:", tfQuestion));
        card.add(createFieldRow("A:", tfA));
        card.add(createFieldRow("B:", tfB));
        card.add(createFieldRow("C:", tfC));
        card.add(createFieldRow("D:", tfD));
        card.add(createFieldRow("✅ Correct Option (A/B/C/D):", tfCorrect));
        card.add(Box.createVerticalStrut(10));
        card.add(btnEdit);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 255, 250));
                card.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
            }

        public void mouseExited(MouseEvent e) {
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
        }
    });

    return card;
}

    private JPanel createFieldRow(String label, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        row.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(200, 30));
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(600, 35));
        return row;
    }

    private String getFieldValue(JPanel cardPanel, int index) {
        Component row = cardPanel.getComponent(index);
        if (row instanceof JPanel rowPanel) {
            for (Component c : rowPanel.getComponents()) {
                if (c instanceof JTextField tf) return tf.getText().trim();
            }
        }
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }

}