# Temporary Job Hiring System

A desktop job hiring application built with Java Swing. Supports three roles — **Admin**, **Company**, and **Employee** — with a dark-themed UI and file-based data persistence.

---

## Features

- Role-based login (Admin / Company / Employee)
- Companies can post and delete job listings
- Employees can browse, search, and apply for jobs
- Companies can view applicants and hire them
- Hired employees get a notification on next login
- Admin can view all users, jobs, and applications
- Data stored in plain text files (no database needed)

---

## Requirements

- **Java JDK 8 or higher**
- No external libraries required — uses only standard Java SE

---

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/ashhad-ds/Temporary-Job-Hiring-System.git
cd Temporary-Job-Hiring-System
```

### 2. Compile

```bash
javac -d out src/*.java
```

### 3. Run

```bash
java -cp out Main
```

> On Windows you can also open the `src/` folder in any Java IDE (IntelliJ, Eclipse, VS Code with Java extension) and run `Main.java` directly.

---

## Default Login

An admin account is created automatically on first launch:

| Role  | Username | Password |
|-------|----------|----------|
| Admin | `admin`  | `admin123` |

Register new Company or Employee accounts from the login screen.

---

## How to Use

### As an Employee
1. Register with role **Employee**
2. Browse available jobs on the Job Board
3. Use the search bar to filter by title, company, or description
4. Select a job and click **Apply for Selected**
5. Check **My Applications** to track your application status
6. If hired, you'll see a congratulations popup on your next login

### As a Company
1. Register with role **Company** and enter your company name
2. Click **Post New Job** to create a listing
3. Select a job and click **View Applicants** to see who applied
4. Click **Hire / Accept** to hire an applicant
5. Use **Delete Selected** to remove a job posting

### As Admin
1. Log in with the default admin credentials
2. View all registered users, active jobs, and submitted applications

---

## Project Structure

```
├── src/
│   ├── Main.java               # Entry point
│   ├── User.java               # Abstract base class
│   ├── Employee.java           # Employee user type
│   ├── Company.java            # Company user type
│   ├── Admin.java              # Admin user type
│   ├── JobPosting.java         # Job listing model
│   ├── Application.java        # Job application model
│   ├── FileManager.java        # File I/O and data persistence
│   ├── UITheme.java            # Centralized UI styling
│   ├── LoginScreen.java        # Login window
│   ├── RegisterScreen.java     # Registration window
│   ├── EmployeeDashboard.java  # Employee UI
│   ├── CompanyDashboard.java   # Company UI
│   └── AdminDashboard.java     # Admin UI
├── users.txt                   # Stored user accounts
├── jobs.txt                    # Stored job postings
├── applications.txt            # Stored applications
└── .gitignore
```

---

## Data Storage

All data is saved locally in `.txt` files in the project root directory. These files are created automatically on first run if they don't exist.

> **Note:** Passwords are stored in plain text. This project is intended for educational purposes only and is not suitable for production use.
