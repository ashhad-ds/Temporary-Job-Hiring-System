
// File: LoginScreen.java
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        FileManager.initFiles();
        setTitle("Temp Job Hiring System");
        setSize(420, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Gradient header
        add(UITheme.createHeaderPanel("Welcome Back", "Sign in to your account"), BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(UITheme.BG_DARK);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel userLabel = UITheme.createLabel("Username");
        userLabel.setAlignmentX(LEFT_ALIGNMENT);
        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(6));

        usernameField = UITheme.createTextField(20);
        usernameField.setAlignmentX(LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(16));

        JLabel passLabel = UITheme.createLabel("Password");
        passLabel.setAlignmentX(LEFT_ALIGNMENT);
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(6));

        passwordField = UITheme.createPasswordField(20);
        passwordField.setAlignmentX(LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(24));

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setBackground(UITheme.BG_DARK);
        btnPanel.setAlignmentX(LEFT_ALIGNMENT);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton loginBtn = UITheme.createPrimaryButton("Sign In");
        JButton registerBtn = UITheme.createAccentButton("Register");

        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> {
            new RegisterScreen().setVisible(true);
            dispose();
        });

        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        formPanel.add(btnPanel);

        add(formPanel, BorderLayout.CENTER);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        User user = FileManager.authenticate(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        openDashboard(user);
        dispose();
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "EMPLOYEE":
                new EmployeeDashboard((Employee) user).setVisible(true);
                break;
            case "COMPANY":
                new CompanyDashboard((Company) user).setVisible(true);
                break;
            case "ADMIN":
                new AdminDashboard((Admin) user).setVisible(true);
                break;
        }
    }
}