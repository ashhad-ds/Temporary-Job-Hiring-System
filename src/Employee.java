// File: Employee.java
public class Employee extends User {
    public Employee(String username, String password) {
        super(username, password, "EMPLOYEE");
    }

    @Override
    public String toFileString() {
        return getRole() + "," + getUsername() + "," + getPassword();
    }
}
