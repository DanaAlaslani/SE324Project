public class App {

public abstract class User {

    private String username;
    private String role;
    private String password;
    private String name;
    private String dob; // Date of Birth

    // Constructor
    public User(String username, String role, String password, String name, String dob) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.name = name;
        this.dob = dob;
    }

    // Methods
    public void register(String email, String password, String phone, String dob, String name) {
        System.out.println("User registered: " + name);
    }

    public boolean login(String email, String password) {
        System.out.println("User logged in: " + email);
        return true;
    }

    public void resetPassword() {
        System.out.println("Password reset for: " + username);
    }

    public void viewProfile() {
        System.out.println("Viewing profile of: " + name);
    }

    public void editProfile() {
        System.out.println("Editing profile of: " + name);
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
}


public class Trainee extends User {

    private float height;
    private float weight;
    private float fitnessGoal;
    private int age;

    // Constructor
    public Trainee(String username, String role, String password, String name, String dob,
                   float height, float weight, float fitnessGoal, int age) {
        super(username, role, password, name, dob);
        this.height = height;
        this.weight = weight;
        this.fitnessGoal = fitnessGoal;
        this.age = age;
    }

    // Methods
    public void setFitnessGoal() {
        System.out.println("Fitness goal set to: " + fitnessGoal);
    }

    public void setHealthMetrics() {
        System.out.println("Health metrics set - Height: " + height + ", Weight: " + weight);
    }

    public void viewWorkoutSchedule() {
        System.out.println("Viewing workout schedule for: " + getName());
    }

    public void viewMealPlan() {
        System.out.println("Viewing meal plan for: " + getName());
    }

    public void viewProgress() {
        System.out.println("Viewing progress for: " + getName());
    }

    public void viewPersonalInfo() {
        System.out.println("Name: " + getName() + ", Age: " + age +
                           ", Height: " + height + ", Weight: " + weight);
    }

    public void enterCustomMeal() {
        System.out.println("Entering custom meal for: " + getName());
    }

    // Getters and Setters
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public float getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(float fitnessGoal) { this.fitnessGoal = fitnessGoal; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}


public class Nutritionist extends User {

    private float fee;
    private String credentials;

    // Constructor
    public Nutritionist(String username, String role, String password, String name, String dob,
                        float fee, String credentials) {
        super(username, role, password, name, dob);
        this.fee = fee;
        this.credentials = credentials;
    }

    // Methods
    public void createMealPlan() {
        System.out.println("Meal plan created by: " + getName());
    }

    public void editMealPlan() {
        System.out.println("Meal plan edited by: " + getName());
    }

    // Getters and Setters
    public float getFee() { return fee; }
    public void setFee(float fee) { this.fee = fee; }

    public String getCredentials() { return credentials; }
    public void setCredentials(String credentials) { this.credentials = credentials; }
}


public class Trainer extends User {

    private float fee;
    private String credentials;

    // Constructor
    public Trainer(String username, String role, String password, String name, String dob,
                   float fee, String credentials) {
        super(username, role, password, name, dob);
        this.fee = fee;
        this.credentials = credentials;
    }

    // Methods
    public void createWorkoutSchedule() {
        System.out.println("Workout schedule created by: " + getName());
    }

    public void editWorkoutSchedule() {
        System.out.println("Workout schedule edited by: " + getName());
    }

    // Getters and Setters
    public float getFee() { return fee; }
    public void setFee(float fee) { this.fee = fee; }

    public String getCredentials() { return credentials; }
    public void setCredentials(String credentials) { this.credentials = credentials; }
}
}
