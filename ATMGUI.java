import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMGUI extends JFrame {

    private JTextField txtUserID;
    private JPasswordField txtPin;
    private JButton btnLogin;
    private JButton btnTransactionsHistory;
    private JButton btnWithdraw;
    private JButton btnDeposit;
    private JButton btnTransfer;
    private JButton btnQuit;

    private Map<String, User> users;
    private User currentUser;
    private List<Transaction> transactions;
    private int transactionCounter;

    public ATMGUI() {
        super("ATM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels for better organization
        JPanel loginPanel = createLoginPanel();
        JPanel buttonPanel = createButtonPanel();

        // Add panels to the content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(loginPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Initialize data and set window properties
        users = new HashMap<>();
        transactions = new ArrayList<>();
        transactionCounter = 1;

        // Create sample users for demonstration
        User user1 = new User("user1", "1234", 1000.0);
        User user2 = new User("user2", "5678", 1500.0);
        addUser(user1);
        addUser(user2);

        // Disable buttons initially until login
        btnTransactionsHistory.setEnabled(false);
        btnWithdraw.setEnabled(false);
        btnDeposit.setEnabled(false);
        btnTransfer.setEnabled(false);

        setSize(400, 250);
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        txtUserID = new JTextField(15);
        txtPin = new JPasswordField(15);
        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Add components to the login panel using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("User ID: "), gbc);

        gbc.gridy = 1;
        loginPanel.add(new JLabel("PIN: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(txtUserID, gbc);

        gbc.gridy = 1;
        loginPanel.add(txtPin, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(btnLogin, gbc);

        return loginPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btnTransactionsHistory = new JButton("Transactions History");
        btnWithdraw = new JButton("Withdraw");
        btnDeposit = new JButton("Deposit");
        btnTransfer = new JButton("Transfer");
        btnQuit = new JButton("Quit");

        btnTransactionsHistory.setEnabled(false);
        btnWithdraw.setEnabled(false);
        btnDeposit.setEnabled(false);
        btnTransfer.setEnabled(false);

        // Add buttons to the button panel
        buttonPanel.add(btnTransactionsHistory);
        buttonPanel.add(btnWithdraw);
        buttonPanel.add(btnDeposit);
        buttonPanel.add(btnTransfer);
        buttonPanel.add(btnQuit);

        // Add action listeners to buttons
        btnTransactionsHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTransactionHistory();
            }
        });

        btnWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showWithdrawDialog();
            }
        });

        btnDeposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDepositDialog();
            }
        });

        btnTransfer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTransferDialog();
            }
        });

        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        return buttonPanel;
    }

    private void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    private void login() {
        String userId = txtUserID.getText();
        String pin = new String(txtPin.getPassword());

        if (authenticateUser(userId, pin)) {
            JOptionPane.showMessageDialog(null,
                    "Login successful. Your balance is " + currentUser.getBalance() + " INR");
            btnTransactionsHistory.setEnabled(true);
            btnWithdraw.setEnabled(true);
            btnDeposit.setEnabled(true);
            btnTransfer.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
            btnTransactionsHistory.setEnabled(false);
            btnWithdraw.setEnabled(false);
            btnDeposit.setEnabled(false);
            btnTransfer.setEnabled(false);
        }

        // Clear user ID and PIN fields after login attempt
        txtUserID.setText("");
        txtPin.setText("");
    }

    private boolean authenticateUser(String userId, String pin) {
        if (users.containsKey(userId)) {
            User user = users.get(userId);
            if (user.getUserPin().equals(pin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    private void showTransactionHistory() {
        StringBuilder historyText = new StringBuilder("Transaction History:\n");
        for (Transaction transaction : transactions) {
            if (transaction.getUserId().equals(currentUser.getUserId())) {
                historyText.append("Transaction ID: ").append(transaction.getTransactionId())
                        .append(", Amount: INR").append(transaction.getAmount()).append("\n");
            }
        }

        if (historyText.length() == 0) {
            historyText.append("No transactions found.");
        }

        JOptionPane.showMessageDialog(null, historyText.toString());
    }

    private void showWithdrawDialog() {
        String amountStr = JOptionPane.showInputDialog("Enter the amount to withdraw:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                withdraw(amount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
            }
        }
    }

    private void withdraw(double amount) {
        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Withdrawal amount must be greater than zero.");
        } else if (amount > currentUser.getBalance()) {
            JOptionPane.showMessageDialog(null, "Insufficient balance.");
        } else {
            currentUser.setBalance(currentUser.getBalance() - amount);
            Transaction transaction = new Transaction("TXN" + transactionCounter, amount, currentUser.getUserId());
            transactions.add(transaction);
            transactionCounter++;
            JOptionPane.showMessageDialog(null,
                    "Withdrawal successful. Your new balance is " + currentUser.getBalance() + "INR");
        }
    }

    private void showDepositDialog() {
        String amountStr = JOptionPane.showInputDialog("Enter the amount to deposit:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                deposit(amount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
            }
        }
    }

    private void deposit(double amount) {
        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Deposit amount must be greater than zero.");
        } else {
            currentUser.setBalance(currentUser.getBalance() + amount);
            Transaction transaction = new Transaction("TXN" + transactionCounter, amount, currentUser.getUserId());
            transactions.add(transaction);
            transactionCounter++;
            JOptionPane.showMessageDialog(null,
                    "Deposit successful. Your new balance is " + currentUser.getBalance() + " INR");
        }
    }

    private void showTransferDialog() {
        String destinationUserId = JOptionPane.showInputDialog("Enter the destination user ID:");
        if (destinationUserId != null) {
            String amountStr = JOptionPane.showInputDialog("Enter the amount to transfer:");
            if (amountStr != null) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    transfer(destinationUserId, amount);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
                }
            }
        }
    }

    private void transfer(String destinationUserId, double amount) {
        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Transfer amount must be greater than zero.");
        } else if (!users.containsKey(destinationUserId)) {
            JOptionPane.showMessageDialog(null, "Recipient user not found.");
        } else if (amount > currentUser.getBalance()) {
            JOptionPane.showMessageDialog(null, "Insufficient balance.");
        } else {
            currentUser.setBalance(currentUser.getBalance() - amount);
            User recipient = users.get(destinationUserId);
            recipient.setBalance(recipient.getBalance() + amount);

            Transaction transaction = new Transaction("TXN" + transactionCounter, -amount, currentUser.getUserId());
            transactions.add(transaction);
            transactionCounter++;

            transaction = new Transaction("TXN" + transactionCounter, amount, destinationUserId);
            transactions.add(transaction);
            transactionCounter++;

            JOptionPane.showMessageDialog(null,
                    "Transfer successful. Your new balance is " + currentUser.getBalance() + " INR");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ATMGUI();
            }
        });
    }
}

class User {
    private String userId;
    private String userPin;
    private double balance;

    public User(String userId, String userPin, double balance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

class Transaction {
    private String transactionId;
    private double amount;
    private String userId;

    public Transaction(String transactionId, double amount, String userId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public String getUserId() {
        return userId;
    }
}
