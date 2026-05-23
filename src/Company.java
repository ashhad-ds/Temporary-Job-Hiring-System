public class Company extends User {
    private String companyName;

    public Company(String username, String password, String companyName) {
        super(username, password, "COMPANY");
        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName; }

    @Override
    public String toFileString() {
        return getRole() + "," + getUsername() + "," + getPassword() + "," + companyName;
    }
}