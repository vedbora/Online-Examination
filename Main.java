import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private final JFrame frame = new JFrame("Online Exam");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final JLabel timerLabel = new JLabel("Time Left: 1800s");
    private final JLabel questionLabel = new JLabel();
    private final JLabel resultLabel = new JLabel("Your Score: 0/10");
    private int timeRemaining = 1800, currentQuestionIndex = 0, score = 0;
    private Timer timer;
    private final JRadioButton[] options = new JRadioButton[4];
    private final ButtonGroup group = new ButtonGroup();
    private final JButton nextButton = new JButton("Next");
    private final JButton submitButton = new JButton("Submit");
    private final String[][] questions = {
            {"What is 2 + 2?", "3", "4", "5", "1"},
            {"Capital of France?", "Berlin", "Madrid", "Paris", "London"},
            {"Red Planet?", "Earth", "Mars", "Jupiter", "Venus"},
            {"Largest Ocean?", "Atlantic", "Indian", "Pacific", "Arctic"},
            {"Square root of 64?", "6", "8", "10", "12"},
            {"Fastest land animal?", "Cheetah", "Lion", "Tiger", "Horse"},
            {"Largest continent?", "Asia", "Europe", "Africa", "Australia"},
            {"Chemical symbol for Gold?", "Au", "Ag", "Pb", "Fe"},
            {"Who wrote 'Hamlet'?", "Shakespeare", "Dickens", "Hemingway", "Austen"},
            {"H2O is the chemical formula for?", "Oxygen", "Hydrogen", "Water", "Helium"}
    };
    private final String[] correctAnswers = {"4", "Paris", "Mars", "Pacific", "8", "Cheetah", "Asia", "Au", "Shakespeare", "Water"};

    public Main() {
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createExamPanel(), "Exam");
        mainPanel.add(createResultPanel(), "Result");
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(_ -> { cardLayout.show(mainPanel, "Exam"); startTimer(); loadQuestion(); });
        panel.add(new JLabel("Username:")); panel.add(userField);
        panel.add(new JLabel("Password:")); panel.add(passField);
        panel.add(loginButton);
        return panel;
    }

    private JPanel createExamPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        for (int i = 0; i < 4; i++) { options[i] = new JRadioButton(); group.add(options[i]); panel.add(options[i]); }
        nextButton.addActionListener(_ -> { checkAnswer(); currentQuestionIndex++; loadQuestion(); });
        submitButton.addActionListener(_ -> submitExam());
        panel.add(questionLabel); panel.add(nextButton); panel.add(submitButton); panel.add(timerLabel);
        return panel;
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            group.clearSelection();
            questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + questions[currentQuestionIndex][0]);
            for (int i = 0; i < 4; i++) options[i].setText(questions[currentQuestionIndex][i + 1]);
            nextButton.setEnabled(currentQuestionIndex < questions.length - 1);
            submitButton.setEnabled(currentQuestionIndex == questions.length - 1);
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timerLabel.setText("Time Left: " + (--timeRemaining) + "s");
                if (timeRemaining <= 0) { timer.cancel(); submitExam(); }
            }
        }, 1000, 1000);
    }

    private void checkAnswer() {
        for (JRadioButton option : options)
            if (option.isSelected() && option.getText().equals(correctAnswers[currentQuestionIndex])) score++;
    }

    private void submitExam() {
        timer.cancel(); checkAnswer();
        JOptionPane.showMessageDialog(frame, "Exam Over! Score: " + score + "/" + questions.length, "Exam Finished", JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(mainPanel, "Result");
        resultLabel.setText("Your Score: " + score + "/" + questions.length);
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(_ -> { timeRemaining = 1800; currentQuestionIndex = 0; score = 0; cardLayout.show(mainPanel, "Login"); });
        panel.add(resultLabel); panel.add(logoutButton);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
