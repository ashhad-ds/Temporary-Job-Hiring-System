public class Application {
    private String applicationId;
    private String employeeUsername;
    private String jobId;
    private String jobTitle;
    private String companyName;

    private String status; // Pending, Accepted, Rejected

    public Application(String applicationId, String employeeUsername, String jobId, String jobTitle, String companyName) {
        this(applicationId, employeeUsername, jobId, jobTitle, companyName, "Pending");
    }

    public Application(String applicationId, String employeeUsername, String jobId, String jobTitle, String companyName, String status) {
        this.applicationId = applicationId;
        this.employeeUsername = employeeUsername;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.status = status;
    }

    public String getApplicationId() { return applicationId; }
    public String getEmployeeUsername() { return employeeUsername; }
    public String getJobId() { return jobId; }
    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return applicationId + "|" + employeeUsername + "|" + jobId + "|" + jobTitle + "|" + companyName + "|" + status;
    }

    @Override
    public String toString() {
        return "App[" + applicationId + "] Job: " + jobTitle + " Status: " + status + " by " + employeeUsername;
    }
}