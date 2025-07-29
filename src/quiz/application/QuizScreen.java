package quiz.application;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class QuizScreen extends JFrame implements ActionListener {

    String userName, topic;
    List<Question> questions;
    int currentQuestion = 0;
    int score = 0;
    int timeLeft = 15;
    Timer swingTimer;

    JLabel questionLabel, timerLabel;
    JRadioButton[] options;
    ButtonGroup group;
    JButton next, lifelineBtn, submitBtn;
    Map<Integer, String> userAnswers = new HashMap<>();

    public QuizScreen(String userName, String topic) {
        this.userName = userName;
        this.topic = topic;

        setTitle("Quiz Time! - " + topic);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon("icons/Home.jpeg").getImage());

        // Top banner
        JLabel banner = new JLabel(new ImageIcon(new ImageIcon("icons/quiz.jpg")
            .getImage().getScaledInstance(1500, 150, Image.SCALE_SMOOTH)));
        banner.setBounds(0, 0, 1500, 150);
        add(banner);

        // Timer Label
        timerLabel = new JLabel("Time Left: 15");
        timerLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        timerLabel.setBounds(1150, 160, 200, 30);
        add(timerLabel);
        fadeInLabel(timerLabel);

        // Question Label
        questionLabel = new JLabel("Question...");
        questionLabel.setBounds(100, 200, 1200, 40);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(questionLabel);
        fadeInLabel(questionLabel);

        // Radio Buttons
        options = new JRadioButton[4];
        group = new ButtonGroup();
        int y = 260;
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(120, y, 1000, 35);
            options[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
            options[i].setBackground(new Color(245, 245, 245));
            options[i].setFocusPainted(false);
            options[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            group.add(options[i]);
            add(options[i]);
            y += 50;
        }

        // Lifeline Button
        lifelineBtn = new JButton("50:50 Lifeline");
        lifelineBtn.setBounds(100, 500, 150, 40);
        lifelineBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lifelineBtn.addActionListener(_ -> applyLifeline());
        styleButton(lifelineBtn);
        add(lifelineBtn);

        // Next Button
        next = new JButton("Next");
        next.setBounds(300, 500, 100, 40);
        next.setFont(new Font("Segoe UI", Font.BOLD, 14));
        next.addActionListener(this);
        styleButton(next);
        add(next);

        // Submit Button
        submitBtn = new JButton("Submit");
        submitBtn.setBounds(420, 500, 100, 40);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setVisible(false);
        submitBtn.addActionListener(_ -> evaluateAnswer());
        styleButton(submitBtn);
        add(submitBtn);

        // Load Questions
        questions = loadQuestions(topic);

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions found for topic: " + topic);
            dispose();
            new UserDashboard(userName);
            return;
        }

        showQuestion(currentQuestion);

        swingTimer = new Timer(1000, _ -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);
            if (timeLeft == 0) {
                evaluateAnswer();
            }
        });
        swingTimer.start();

        setVisible(true);
    }

    private void showQuestion(int index) {
        timeLeft = 15;
        timerLabel.setText("Time Left: 15");
        group.clearSelection();

        Question q = questions.get(index);
        questionLabel.setText("Q" + (index + 1) + ": " + q.question);
        fadeInLabel(questionLabel);

        options[0].setText("A. " + q.optionA);
        options[1].setText("B. " + q.optionB);
        options[2].setText("C. " + q.optionC);
        options[3].setText("D. " + q.optionD);

        for (JRadioButton opt : options) {
            opt.setVisible(true);
            opt.setEnabled(true);
        }

        lifelineBtn.setEnabled(true);
        next.setVisible(index < questions.size() - 1);
        submitBtn.setVisible(index == questions.size() - 1);
    }

    private void evaluateAnswer() {
        swingTimer.stop();

        String selected = null;
        for (JRadioButton option : options) {
            if (option.isSelected()) {
                selected = option.getText().substring(0, 1);
                break;
            }
        }

        Question q = questions.get(currentQuestion);
        if (selected != null && selected.equalsIgnoreCase(q.correctOption)) {
            score++;
        }

        userAnswers.put(currentQuestion, selected != null ? selected : "No Answer");

        currentQuestion++;

        if (currentQuestion < questions.size()) {
            showQuestion(currentQuestion);
            swingTimer.restart();
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        swingTimer.stop();
        saveResult();
        dispose();
        new Score(userName, topic, score, questions.size());
    }

    private void applyLifeline() {
        lifelineBtn.setEnabled(false);

        Question q = questions.get(currentQuestion);
        List<Integer> wrongIndexes = new ArrayList<>();

        if (!q.correctOption.equalsIgnoreCase("A")) wrongIndexes.add(0);
        if (!q.correctOption.equalsIgnoreCase("B")) wrongIndexes.add(1);
        if (!q.correctOption.equalsIgnoreCase("C")) wrongIndexes.add(2);
        if (!q.correctOption.equalsIgnoreCase("D")) wrongIndexes.add(3);

        Collections.shuffle(wrongIndexes);

        for (int i = 0; i < 2 && i < wrongIndexes.size(); i++) {
            options[wrongIndexes.get(i)].setEnabled(false);
            options[wrongIndexes.get(i)].setVisible(false);
        }
    }

    private void saveResult() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results.txt", true))) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println(userName + " | Topic: " + topic + " | Score: " + score + "/" + questions.size() + " | " + timestamp);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save result.");
        }
    }

    private List<Question> loadQuestions(String topic) {
        List<Question> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("examquestion.txt"))) {
            String line;
            boolean inTopic = false;
            String q = "", a = "", b = "", c = "", d = "", ans = "";

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Topic:")) {
                    inTopic = line.substring(6).trim().equalsIgnoreCase(topic);
                } else if (inTopic) {
                    if (line.startsWith("Q:")) q = line.substring(2).trim();
                    else if (line.startsWith("A:")) a = line.substring(2).trim();
                    else if (line.startsWith("B:")) b = line.substring(2).trim();
                    else if (line.startsWith("C:")) c = line.substring(2).trim();
                    else if (line.startsWith("D:")) d = line.substring(2).trim();
                    else if (line.startsWith("Answer:")) {
                        ans = line.substring(7).trim();
                        list.add(new Question(q, a, b, c, d, ans));
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load questions.");
        }
        return list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        evaluateAnswer();
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void fadeInLabel(JLabel label) {
        Timer fadeTimer = new Timer(40, null);
        fadeTimer.addActionListener(new ActionListener() {
            float alpha = 0f;

            public void actionPerformed(ActionEvent evt) {
                alpha += 0.05f;
                label.setForeground(new Color(0f, 0f, 0f, Math.min(1f, alpha)));
                if (alpha >= 1f) ((Timer) evt.getSource()).stop();
            }
        });
        fadeTimer.start();
    }
}
