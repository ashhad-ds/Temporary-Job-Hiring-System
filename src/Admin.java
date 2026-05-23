public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    @Override
    public String toFileString() {
        return getRole() + "," + getUsername() + "," + getPassword();
    }
}