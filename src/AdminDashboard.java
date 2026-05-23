import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    public AdminDashboard(Admin admin) {
        setTitle("Admin Dashboard - " + admin.getUsername());
        setSize(520, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        add(UITheme.createHeaderPanel("Admin Panel", "System overview and management"), BorderLayout.NORTH);

        // Card-style button panel
        JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 12));
        cardPanel.setBackground(UITheme.BG_DARK);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JButton viewUsersBtn = UITheme.createPrimaryButton("View All Users");
        JButton viewJobsBtn = UITheme.createAccentButton("View All Jobs");
        JButton viewAppsBtn = UITheme.createPrimaryButton("View All Applications");
        JButton logoutBtn = UITheme.createDangerButton("Logout");

        // Set consistent size
        Dimension btnSize = new Dimension(Integer.MAX_VALUE, 44);
        viewUsersBtn.setMaximumSize(btnSize);
        viewJobsBtn.setMaximumSize(btnSize);
        viewAppsBtn.setMaximumSize(btnSize);
        logoutBtn.setMaximumSize(btnSize);
        viewUsersBtn.setPreferredSize(new Dimension(300, 44));
        viewJobsBtn.setPreferredSize(new Dimension(300, 44));
        viewAppsBtn.setPreferredSize(new Dimension(300, 44));
        logoutBtn.setPreferredSize(new Dimension(300, 44));

        viewUsersBtn.addActionListener(e -> viewAllUsers());
        viewJobsBtn.addActionListener(e -> viewAllJobs());
        viewAppsBtn.addActionListener(e -> viewAllApplications());
        logoutBtn.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        cardPanel.add(viewUsersBtn);
        cardPanel.add(viewJobsBtn);
        cardPanel.add(viewAppsBtn);
        cardPanel.add(logoutBtn);
        add(cardPanel, BorderLayout.CENTER);
    }

    private void viewAllUsers() {
        List<User> users = FileManager.loadUsers();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found.");
            return;
        }
        showList("All Users", users.stream().map(User::toString).toArray(String[]::new));
    }

    private void viewAllJobs() {
        List<JobPosting> jobs = FileManager.loadJobs();
        if (jobs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No jobs found.");
            return;
        }
        showList("All Jobs", jobs.stream().map(JobPosting::toString).toArray(String[]::new));
    }

    private void viewAllApplications() {
        List<Application> apps = FileManager.loadApplications();
        if (apps.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No applications found.");
            return;
        }
        showList("All Applications", apps.stream().map(Application::toString).toArray(String[]::new));
    }

    private void showList(String titleStr, String[] items) {
        JList<String> list = new JList<>(items);
        list.setBackground(UITheme.BG_DARK);
        list.setForeground(UITheme.TEXT_PRIMARY);
        list.setFont(UITheme.BODY_FONT);
        list.setSelectionBackground(UITheme.BG_CARD);
        list.setSelectionForeground(UITheme.TEXT_PRIMARY);
        list.setFixedCellHeight(32);
        list.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        JOptionPane.showMessageDialog(this, scrollPane, titleStr, JOptionPane.INFORMATION_MESSAGE);
    }
}