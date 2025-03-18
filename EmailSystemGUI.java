import java.util.ArrayList;
import java.util.Stack;
import javax.swing.*;

public class EmailSystemGUI {

    // List to store users and their credentials
    static ArrayList<String[]> users = new ArrayList<>();
    // Each user's email stack to store received emails
    static ArrayList<Stack<String>> emailStacks = new ArrayList<>();

    // UI Components
    JFrame frame;
    JPanel panel;
    JTextArea emailDisplay;
    JTextField usernameField;
    JPasswordField passwordField;
    JTextField recipientField;
    JTextField emailContentField;
    JLabel statusLabel;
    int currentUserIndex = -1;

    public EmailSystemGUI() {
        // Adding default users
        users.add(new String[]{"user1", "pass1"});
        users.add(new String[]{"user2", "pass2"});
        users.add(new String[]{"user3", "pass3"});

        // Initialize email stacks for each user
        for (int i = 0; i < users.size(); i++) {
            emailStacks.add(new Stack<>());
        }

        // Initialize Swing components
        frame = new JFrame("Email System");
        panel = new JPanel();
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 20, 80, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(100, 80, 100, 25);
        panel.add(registerButton);

        emailDisplay = new JTextArea();
        emailDisplay.setBounds(10, 110, 260, 150);
        emailDisplay.setEditable(false);
        panel.add(emailDisplay);

        JLabel recipientLabel = new JLabel("Recipient:");
        recipientLabel.setBounds(10, 270, 80, 25);
        panel.add(recipientLabel);

        recipientField = new JTextField(20);
        recipientField.setBounds(100, 270, 165, 25);
        panel.add(recipientField);

        JLabel emailLabel = new JLabel("Email Content:");
        emailLabel.setBounds(10, 300, 100, 25);
        panel.add(emailLabel);

        emailContentField = new JTextField(20);
        emailContentField.setBounds(100, 300, 165, 25);
        panel.add(emailContentField);

        JButton sendEmailButton = new JButton("Send Email");
        sendEmailButton.setBounds(10, 330, 120, 25);
        panel.add(sendEmailButton);

        JButton viewLatestButton = new JButton("View Latest Email");
        viewLatestButton.setBounds(140, 330, 150, 25);
        panel.add(viewLatestButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(100, 370, 100, 25);
        panel.add(logoutButton);

        statusLabel = new JLabel("Status: ");
        statusLabel.setBounds(10, 400, 300, 25);
        panel.add(statusLabel);

        frame.add(panel);
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Button actions
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());
        sendEmailButton.addActionListener(e -> sendEmail());
        viewLatestButton.addActionListener(e -> viewLatestEmail());
        logoutButton.addActionListener(e -> logout());
    }

    // Login function
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Status: Username/Password cannot be empty");
            return;
        }

        currentUserIndex = loginUser(username, password);

        if (currentUserIndex != -1) {
            statusLabel.setText("Status: Logged in as " + username);
        } else {
            statusLabel.setText("Status: Login failed, check credentials");
        }
    }

    // Registration function
    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Status: Username/Password cannot be empty");
            return;
        }

        if (findUserIndex(username) != -1) {
            statusLabel.setText("Status: Username already exists");
            return;
        }

        currentUserIndex = addUser(username, password);
        statusLabel.setText("Status: Registered and logged in as " + username);
    }

    // Send email to another user
    private void sendEmail() {
        if (currentUserIndex == -1) {
            statusLabel.setText("Status: Please login first.");
            return;
        }

        String recipient = recipientField.getText();
        String emailContent = emailContentField.getText();

        if (recipient.isEmpty() || emailContent.isEmpty()) {
            statusLabel.setText("Status: Recipient/Email content cannot be empty.");
            return;
        }

        int recipientIndex = findUserIndex(recipient);

        if (recipientIndex == -1) {
            statusLabel.setText("Status: Recipient not found.");
            return;
        }

        // Send the email to the recipient's stack
        emailStacks.get(recipientIndex).push(emailContent);
        statusLabel.setText("Status: Email sent to " + recipient);
    }

    // View the latest email for the logged-in user
    private void viewLatestEmail() {
        if (currentUserIndex == -1) {
            statusLabel.setText("Status: Please login first.");
            return;
        }

        if (!emailStacks.get(currentUserIndex).isEmpty()) {
            String latestEmail = emailStacks.get(currentUserIndex).peek();
            emailDisplay.setText("Latest email: \n" + latestEmail);
        } else {
            emailDisplay.setText("No emails in your inbox.");
        }
    }

    // Logout function
    private void logout() {
        currentUserIndex = -1;
        statusLabel.setText("Status: Logged out.");
        emailDisplay.setText("");
        recipientField.setText("");
        emailContentField.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }

    // Login verification
    public static int loginUser(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i)[0].equals(username) && users.get(i)[1].equals(password)) {
                return i;
            }
        }
        return -1;
    }

    // Add new user
    public static int addUser(String newUsername, String newPassword) {
        users.add(new String[]{newUsername, newPassword});
        emailStacks.add(new Stack<>());
        return users.size() - 1;
    }

    // Find the index of a user by username
    public static int findUserIndex(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i)[0].equals(username)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        new EmailSystemGUI();
    }
}
