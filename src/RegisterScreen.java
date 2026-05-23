import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JFrame {
    private JTextField usernameField, companyNameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JLabel companyNameLabel;

    public RegisterScreen() {
        setTitle("Register - Temp Job Hiring System");
        setSize(420, 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        add(UITheme.createHeaderPanel("Create Account", "Register as Employee or Company"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(UITheme.BG_DARK);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Role
        JLabel roleLabel = UITheme.createLabel("Account Type");
        roleLabel.setAlignmentX(LEFT_ALIGNMENT);
        formPanel.add(roleLabel);
        formPanel.add(Box.createVerticalStrut(6));
        roleCombo = new JComboBox<>(new String[] { "EMPLOYEE", "COMPANY" });
        roleCombo.setBackground(UITheme.BG_INPUT);
        roleCombo.setForeground(UITheme.TEXT_PRIMARY);
        roleCombo.setFont(UITheme.INPUT_FONT);
        roleCombo.setAlignmentX(LEFT_ALIGNMENT);
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(roleCombo);
        formPanel.add(Box.createVerticalStrut(14));

        // Username
        JLabel userLabel = UITheme.createLabel("Username");
        userLabel.setAlignmentX(LEFT_ALIGNMENT);
        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(6));
        usernameField = UITheme.createTextField(20);
        usernameField.setAlignmentX(LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(14));

        // Password
        JLabel passLabel = UITheme.createLabel("Password");
        passLabel.setAlignmentX(LEFT_ALIGNMENT);
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(6));
        passwordField = UITheme.createPasswordField(20);
        passwordField.setAlignmentX(LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(14));

        // Company name (conditional)
        companyNameLabel = UITheme.createLabel("Company Name");
        companyNameLabel.setAlignmentX(LEFT_ALIGNMENT);
        companyNameLabel.setVisible(false);
        formPanel.add(companyNameLabel);
        formPanel.add(Box.createVerticalStrut(6));
        companyNameField = UITheme.createTextField(20);
        companyNameField.setAlignmentX(LEFT_ALIGNMENT);
        companyNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        companyNameField.setVisible(false);
        formPanel.add(companyNameField);
        formPanel.add(Box.createVerticalStrut(20));

        roleCombo.addActionListener(e -> {
            boolean isCompany = roleCombo.getSelectedItem().equals("COMPANY");
            companyNameField.setVisible(isCompany);
            companyNameLabel.setVisible(isCompany);
            revalidate();
            repaint();
        });

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setBackground(UITheme.BG_DARK);
        btnPanel.setAlignmentX(LEFT_ALIGNMENT);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton registerBtn = UITheme.createAccentButton("Register");
        JButton backBtn = UITheme.createPrimaryButton("Back to Login");
        registerBtn.addActionListener(e -> handleRegister());
        backBtn.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        btnPanel.add(registerBtn);
        btnPanel.add(backBtn);
        formPanel.add(btnPanel);

        add(formPanel, BorderLayout.CENTER);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        if (FileManager.userExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user;
        if (role.equals("COMPANY")) {
            String companyName = companyNameField.getText().trim();
            if (companyName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter company name.");
                return;
            }
            user = new Company(username, password, companyName);
        } else {
            user = new Employee(username, password);
        }

        FileManager.saveUser(user);
        JOptionPane.showMessageDialog(this, "Registration successful!");
        new LoginScreen().setVisible(true);
        dispose();
    }
}