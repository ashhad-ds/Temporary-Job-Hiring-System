// File: JobPosting.java
public class JobPosting {
    private String jobId;
    private String title;
    private String description;
    private String companyUsername;
    private String companyName;

    public JobPosting(String jobId, String title, String description, String companyUsername, String companyName) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.companyUsername = companyUsername;
        this.companyName = companyName;
    }

    public String getJobId() { return jobId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCompanyUsername() { return companyUsername; }
    public String getCompanyName() { return companyName; }

    public String toFileString() {
        return jobId + "|" + title + "|" + description + "|" + companyUsername + "|" + companyName;
    }

    @Override
    public String toString() {
        return "[" + jobId + "] " + title + " - " + companyName;
    }
}