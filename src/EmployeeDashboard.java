
// File: EmployeeDashboard.java
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDashboard extends JFrame {
    private Employee employee;
    private JList<JobPosting> jobList;
    private DefaultListModel<JobPosting> jobListModel;
    private List<JobPosting> allJobs;
    private JTextField searchField;
    private JLabel resultCountLabel;

    public EmployeeDashboard(Employee employee) {
        this.employee = employee;
        setTitle("Employee Dashboard - " + employee.getUsername());
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        buildUI();
        checkHiringStatus();
    }

    private void checkHiringStatus() {
        List<Application> apps = FileManager.loadApplications().stream()
                .filter(a -> a.getEmployeeUsername().equals(employee.getUsername()) && a.getStatus().equals("Accepted"))
                .collect(Collectors.toList());

        for (Application a : apps) {
            String msg = "<html><div style='text-align:center; padding:10px;'>" +
                    "<h2>Congratulations!</h2>" +
                    "<p>You have been hired for the position of <b>" + a.getJobTitle() + "</b></p>" +
                    "<p>at <b>" + a.getCompanyName() + "</b>!</p></div></html>";

            JOptionPane.showMessageDialog(this, msg, "You're Hired!", JOptionPane.INFORMATION_MESSAGE);
            FileManager.deleteApplication(a.getApplicationId());
        }
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        add(UITheme.createHeaderPanel("Job Board",
                "Welcome, " + employee.getUsername() + "  —  Browse and apply for jobs"), BorderLayout.NORTH);

        // Center: Search + Job List
        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(UITheme.BG_DARK);

        // Search bar panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(UITheme.BG_PANEL);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        searchField = UITheme.createSearchField("Search jobs by title, company, or description...");
        searchPanel.add(searchField, BorderLayout.CENTER);

        resultCountLabel = new JLabel("0 jobs");
        resultCountLabel.setFont(UITheme.LIST_SUB_FONT);
        resultCountLabel.setForeground(UITheme.TEXT_MUTED);
        resultCountLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        searchPanel.add(resultCountLabel, BorderLayout.EAST);

        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Real-time search filtering
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterJobs();
            }

            public void removeUpdate(DocumentEvent e) {
                filterJobs();
            }

            public void changedUpdate(DocumentEvent e) {
                filterJobs();
            }
        });

        // Job list
        jobListModel = new DefaultListModel<>();
        jobList = new JList<>(jobListModel);
        jobList.setCellRenderer(new UITheme.JobCellRenderer());
        jobList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobList.setBackground(UITheme.BG_DARK);
        jobList.setFixedCellHeight(80);

        JScrollPane scrollPane = new JScrollPane(jobList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        btnPanel.setBackground(UITheme.BG_PANEL);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));

        JButton refreshBtn = UITheme.createPrimaryButton("Refresh Jobs");
        JButton applyBtn = UITheme.createAccentButton("Apply for Selected");
        JButton viewAppliedBtn = UITheme.createPrimaryButton("My Applications");
        JButton logoutBtn = UITheme.createDangerButton("Logout");

        refreshBtn.addActionListener(e -> loadJobs());
        applyBtn.addActionListener(e -> applyForJob());
        viewAppliedBtn.addActionListener(e -> viewApplied());
        logoutBtn.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        btnPanel.add(refreshBtn);
        btnPanel.add(applyBtn);
        btnPanel.add(viewAppliedBtn);
        btnPanel.add(logoutBtn);
        add(btnPanel, BorderLayout.SOUTH);

        loadJobs();
    }

    private void loadJobs() {
        allJobs = FileManager.loadJobs();
        filterJobs();
    }

    private void filterJobs() {
        if (allJobs == null)
            allJobs = new ArrayList<>();
        String query = searchField.getText().trim().toLowerCase();
        jobListModel.clear();
        int count = 0;
        for (JobPosting job : allJobs) {
            if (query.isEmpty()
                    || job.getTitle().toLowerCase().contains(query)
                    || job.getDescription().toLowerCase().contains(query)
                    || job.getCompanyName().toLowerCase().contains(query)) {
                jobListModel.addElement(job);
                count++;
            }
        }
        resultCountLabel.setText(count + " job" + (count != 1 ? "s" : "") + " found");
    }

    private void applyForJob() {
        int index = jobList.getSelectedIndex();
        if (index < 0 || jobListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a job.");
            return;
        }
        JobPosting job = jobListModel.getElementAt(index);

        List<Application> existing = FileManager.loadApplications();
        boolean alreadyApplied = existing.stream()
                .anyMatch(a -> a.getEmployeeUsername().equals(employee.getUsername())
                        && a.getJobId().equals(job.getJobId()));
        if (alreadyApplied) {
            JOptionPane.showMessageDialog(this, "You already applied for this job!");
            return;
        }

        Application app = new Application(
                FileManager.generateId("APP"),
                employee.getUsername(),
                job.getJobId(),
                job.getTitle(),
                job.getCompanyName());
        FileManager.saveApplication(app);
        JOptionPane.showMessageDialog(this, "Application submitted successfully!");
    }

    private void viewApplied() {
        List<Application> apps = FileManager.loadApplications().stream()
                .filter(a -> a.getEmployeeUsername().equals(employee.getUsername()))
                .collect(Collectors.toList());

        if (apps.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You haven't applied for any jobs.");
            return;
        }

        StringBuilder sb = new StringBuilder("Your Applications:\n\n");
        for (Application a : apps) {
            sb.append("  - ").append(a.getJobTitle())
                    .append(" at ").append(a.getCompanyName())
                    .append(" | Status: ").append(a.getStatus()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "My Applications", JOptionPane.INFORMATION_MESSAGE);
    }
}