import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class GuessTheNumberGame extends JFrame {
    private int randomNumber;
    private int maxNumber;
    private int minNumber;
    private int attempts;
    private int maxAttempts;
    private int score;
    private JLabel titleLabel;
    private JLabel attemptsLabel;
    private JLabel messageLabel;
    private JTextField guessTextField;
    private JButton guessButton;
    private JButton newGameButton;

    public GuessTheNumberGame(int minNumber, int maxNumber, int maxAttempts) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        this.maxAttempts = maxAttempts;
        initGame();
    }

    private void initGame() {
        randomNumber = generateRandomNumber();
        attempts = 0;
        score = 0;

        titleLabel = new JLabel("Guess the Number Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        attemptsLabel = new JLabel("Attempts: " + attempts);
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        messageLabel = new JLabel("Enter a number between " + minNumber + " and " + maxNumber);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        guessTextField = new JTextField(10);
        guessTextField.setFont(new Font("Arial", Font.PLAIN, 16));

        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("Arial", Font.PLAIN, 16));
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGuessButtonClicked();
            }
        });
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 16));
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNewGameButtonClicked();
            }
        });

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add an empty border around the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(attemptsLabel, gbc);

        gbc.gridy = 2;
        mainPanel.add(messageLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 5, 10);
        mainPanel.add(guessTextField, gbc);

        // Button panel with BoxLayout to align Guess and New Game buttons on the same
        // line with space in between
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(guessButton);

        // Adding a space between the Guess and New Game buttons
        buttonPanel.add(Box.createHorizontalStrut(10)); // Adjust the space size as needed

        buttonPanel.add(newGameButton);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        setContentPane(mainPanel);
        setTitle("Guess the Number Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int generateRandomNumber() {
        return (int) (Math.random() * (maxNumber - minNumber + 1)) + minNumber;
    }

    private void onGuessButtonClicked() {
        try {
            int userGuess = Integer.parseInt(guessTextField.getText());
            attempts++;
            attemptsLabel.setText("Attempts: " + attempts);

            if (userGuess == randomNumber) {
                messageLabel.setText("Congratulations! You guessed the number!");
                score += (maxAttempts - attempts + 1); // Give points based on attempts
                guessButton.setEnabled(false);
            } else if (userGuess < randomNumber) {
                messageLabel.setText("Try a higher number.");
            } else {
                messageLabel.setText("Try a lower number.");
            }

            if (attempts >= maxAttempts) {
                messageLabel.setText("Out of attempts! The number was " + randomNumber + ".");
                guessButton.setEnabled(false);
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("Invalid input! Please enter a valid number.");
        }
    }

    private void onNewGameButtonClicked() {
        randomNumber = generateRandomNumber();
        attempts = 0;
        score = 0;
        attemptsLabel.setText("Attempts: " + attempts);
        messageLabel.setText("Enter a number between " + minNumber + " and " + maxNumber);
        guessTextField.setText("");
        guessButton.setEnabled(true);
    }

    public static void main(String[] args) {
        int minNumber = 1;
        int maxNumber = 100;
        int maxAttempts = 10;
        new GuessTheNumberGame(minNumber, maxNumber, maxAttempts);
    }
}
