import java.io.*;
import java.util.*;

public class FileManager {
    private static final String USERS_FILE = "users.txt";
    private static final String JOBS_FILE = "jobs.txt";
    private static final String APPS_FILE = "applications.txt";

    public static void initFiles() {
        createIfNotExists(USERS_FILE);
        createIfNotExists(JOBS_FILE);
        createIfNotExists(APPS_FILE);
        // Add default admin if no admin exists
        List<User> users = loadUsers();
        boolean hasAdmin = users.stream().anyMatch(u -> u.getRole().equals("ADMIN"));
        if (!hasAdmin) {
            saveUser(new Admin("admin", "admin123"));
        }
    }

    private static void createIfNotExists(String filename) {
        File f = new File(filename);
        if (!f.exists()) {
            try { f.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    switch (parts[0]) {
                        case "EMPLOYEE": users.add(new Employee(parts[1], parts[2])); break;
                        case "COMPANY": if (parts.length >= 4) users.add(new Company(parts[1], parts[2], parts[3])); break;
                        case "ADMIN": users.add(new Admin(parts[1], parts[2])); break;
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return users;
    }

    public static void saveUser(User user) {
        try (FileWriter fw = new FileWriter(USERS_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(user.toFileString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static boolean userExists(String username) {
        return loadUsers().stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public static User authenticate(String username, String password) {
        return loadUsers().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst().orElse(null);
    }

    public static List<JobPosting> loadJobs() {
        List<JobPosting> jobs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(JOBS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    jobs.add(new JobPosting(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return jobs;
    }

    public static void saveJob(JobPosting job) {
        try (FileWriter fw = new FileWriter(JOBS_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(job.toFileString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void deleteJob(String jobId) {
        List<JobPosting> jobs = loadJobs();
        jobs.removeIf(j -> j.getJobId().equals(jobId));
        rewriteJobs(jobs);
        // Also remove related applications
        List<Application> apps = loadApplications();
        apps.removeIf(a -> a.getJobId().equals(jobId));
        rewriteApplications(apps);
    }

    private static void rewriteJobs(List<JobPosting> jobs) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(JOBS_FILE, false))) {
            for (JobPosting j : jobs) pw.println(j.toFileString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<Application> loadApplications() {
        List<Application> apps = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(APPS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    apps.add(new Application(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                } else if (parts.length == 5) {
                    apps.add(new Application(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return apps;
    }

    public static void saveApplication(Application app) {
        try (FileWriter fw = new FileWriter(APPS_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(app.toFileString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void rewriteApplications(List<Application> apps) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(APPS_FILE, false))) {
            for (Application a : apps) pw.println(a.toFileString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void updateApplicationStatus(String applicationId, String status) {
        List<Application> apps = loadApplications();
        for (Application a : apps) {
            if (a.getApplicationId().equals(applicationId)) {
                a.setStatus(status);
                break;
            }
        }
        rewriteApplications(apps);
    }

    public static void deleteApplication(String applicationId) {
        List<Application> apps = loadApplications();
        apps.removeIf(a -> a.getApplicationId().equals(applicationId));
        rewriteApplications(apps);
    }

    public static String generateId(String prefix) {
        return prefix + System.currentTimeMillis();
    }
}