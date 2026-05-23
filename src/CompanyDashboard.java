// File: CompanyDashboard.java
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDashboard extends JFrame {
    private Company company;
    private JList<JobPosting> jobList;
    private DefaultListModel<JobPosting> jobListModel;
    private List<JobPosting> currentJobs;

    public CompanyDashboard(Company company) {
        this.company = company;
        setTitle("Company Dashboard - " + company.getCompanyName());
        setSize(700, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        add(UITheme.createHeaderPanel(company.getCompanyName(), "Manage your job postings and view applicants"), BorderLayout.NORTH);

        // Job list with custom renderer
        jobListModel = new DefaultListModel<>();
        jobList = new JList<>(jobListModel);
        jobList.setCellRenderer(new UITheme.JobCellRenderer());
        jobList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobList.setBackground(UITheme.BG_DARK);
        jobList.setFixedCellHeight(80);

        JScrollPane scrollPane = new JScrollPane(jobList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        btnPanel.setBackground(UITheme.BG_PANEL);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));

        JButton postJobBtn = UITheme.createAccentButton("Post New Job");
        JButton refreshBtn = UITheme.createPrimaryButton("Refresh");
        JButton deleteJobBtn = UITheme.createDangerButton("Delete Selected");
        JButton viewApplicantsBtn = UITheme.createPrimaryButton("View Applicants");
        JButton logoutBtn = UITheme.createDangerButton("Logout");

        postJobBtn.addActionListener(e -> postJob());
        refreshBtn.addActionListener(e -> loadJobs());
        deleteJobBtn.addActionListener(e -> deleteJob());
        viewApplicantsBtn.addActionListener(e -> viewApplicants());
        logoutBtn.addActionListener(e -> { new LoginScreen().setVisible(true); dispose(); });

        btnPanel.add(postJobBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(deleteJobBtn);
        btnPanel.add(viewApplicantsBtn);
        btnPanel.add(logoutBtn);
        add(btnPanel, BorderLayout.SOUTH);

        loadJobs();
    }

    private void loadJobs() {
        currentJobs = FileManager.loadJobs().stream()
                .filter(j -> j.getCompanyUsername().equals(company.getUsername()))
                .collect(Collectors.toList());
        jobListModel.clear();
        for (JobPosting job : currentJobs) jobListModel.addElement(job);
    }

    private void postJob() {
        JTextField titleField = UITheme.createTextField(20);
        JTextField descField = UITheme.createTextField(20);

        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBackground(UITheme.BG_PANEL);
        panel.add(UITheme.createLabel("Job Title:"));
        panel.add(titleField);
        panel.add(UITheme.createLabel("Description:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Post New Job", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            if (title.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            JobPosting job = new JobPosting(
                    FileManager.generateId("JOB"),
                    title, desc,
                    company.getUsername(),
                    company.getCompanyName()
            );
            FileManager.saveJob(job);
            JOptionPane.showMessageDialog(this, "Job posted successfully!");
            loadJobs();
        }
    }

    private void deleteJob() {
        int index = jobList.getSelectedIndex();
        if (index < 0 || jobListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a job to delete.");
            return;
        }
        JobPosting job = jobListModel.getElementAt(index);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete job: " + job.getTitle() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            FileManager.deleteJob(job.getJobId());
            JOptionPane.showMessageDialog(this, "Job deleted.");
            loadJobs();
        }
    }

    private void viewApplicants() {
        int index = jobList.getSelectedIndex();
        if (index < 0 || jobListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a job.");
            return;
        }
        JobPosting job = jobListModel.getElementAt(index);
        List<Application> apps = FileManager.loadApplications().stream()
                .filter(a -> a.getJobId().equals(job.getJobId()))
                .collect(Collectors.toList());

        if (apps.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No applicants for this job.");
            return;
        }

        JDialog dialog = new JDialog(this, "Applicants for " + job.getTitle(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(UITheme.BG_DARK);

        DefaultListModel<Application> appModel = new DefaultListModel<>();
        for (Application a : apps) appModel.addElement(a);

        JList<Application> appList = new JList<>(appModel);
        appList.setBackground(UITheme.BG_DARK);
        appList.setForeground(Color.WHITE);
        appList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appList.setFixedCellHeight(40);
        appList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scroll = new JScrollPane(appList);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UITheme.BG_PANEL);
        
        JButton acceptBtn = UITheme.createAccentButton("Hire / Accept");
        acceptBtn.addActionListener(e -> {
            Application selected = appList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(dialog, "Please select an applicant.");
                return;
            }
            if (selected.getStatus().equals("Accepted")) {
                JOptionPane.showMessageDialog(dialog, "This applicant is already hired.");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(dialog, "Accept " + selected.getEmployeeUsername() + " for this position?", "Confirm Hire", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                FileManager.updateApplicationStatus(selected.getApplicationId(), "Accepted");
                JOptionPane.showMessageDialog(dialog, "Employee hired successfully!");
                dialog.dispose();
            }
        });

        JButton closeBtn = UITheme.createPrimaryButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(acceptBtn);
        btnPanel.add(closeBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}