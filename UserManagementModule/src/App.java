import java.util.List;
import java.util.ArrayList;
public class App {

public abstract class User {

    private String username;
    private String role;
    private String password;
    private String name;
    private String dob;
    private String email;
    private String phone;
    private boolean isVerified;

    // Constructor used by admin when creating Trainer/Nutritionist accounts
    public User(String username, String role, String password, String name,
                String dob, String email, String phone) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.isVerified = true; // Admin-created accounts are pre-verified
    }

    // Login method with validation for email and password.
    public boolean login(String email, String password) throws IllegalArgumentException {
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty.");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty.");
        if (this.email.equals(email) && this.password.equals(password) && this.isVerified) {
            System.out.println("Login successful: " + name);
            return true;
        }
        System.out.println("Login failed: Invalid credentials or unverified account.");
        return false;
    }

    // Allows user to reset their password with validation.
    public void resetPassword(String newPassword) throws IllegalArgumentException {
        if (newPassword == null || newPassword.length() < 6)
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        this.password = newPassword;
        System.out.println("Password reset successfully for: " + name);
    }

    // Views the user's profile information.
    public void viewProfile() {
        System.out.println("=== Profile ===");
        System.out.println("Name     : " + name);
        System.out.println("Username : " + username);
        System.out.println("Email    : " + email);
        System.out.println("Phone    : " + phone);
        System.out.println("Role     : " + role);
        System.out.println("DOB      : " + dob);
        System.out.println("Verified : " + isVerified);
    }

   // Allows user to edit their profile information (name, email, phone).
    public void editProfile(String newName, String newEmail, String newPhone)
            throws IllegalArgumentException {
        if (newName == null || newName.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        if (newEmail == null || newEmail.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty.");
        this.name = newName;
        this.email = newEmail;
        this.phone = newPhone;
        System.out.println("Profile updated successfully for: " + name);
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    // No getter for password - security best practice
    public void setPassword(String password) { this.password = password; }
}




//Trainee class
public class Trainee extends User {

    private float height;
    private float weight;
    private float fitnessGoal;
    private int age;
    private float bmi;
    private float dailyCalorieTarget;
    private float totalCaloriesToday;
    private float progressPercentage;
    private String predictedCompletionDate;
    private List<String> mealsToday;

    // Constructor for Trainee - called when a new trainee self-registers.
    public Trainee(String username, String password, String name, String dob,
                   String email, String phone, float height, float weight,
                   float fitnessGoal, int age) {
        super(username, "Trainee", password, name, dob, email, phone);
        this.height = height;
        this.weight = weight;
        this.fitnessGoal = fitnessGoal;
        this.age = age;
        this.totalCaloriesToday = 0;
        this.mealsToday = new ArrayList<>();
        this.progressPercentage = 0;
        this.setVerified(false); // Trainee must verify via code after registering
    }

    // Self-registration method for Trainee with validation.
    public void register(String email, String password, String phone, String dob, String name)
            throws IllegalArgumentException {
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty.");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        if (phone == null || phone.isEmpty())
            throw new IllegalArgumentException("Phone cannot be empty.");
        this.setEmail(email);
        this.setPassword(password);
        this.setPhone(phone);
        this.setDob(dob);
        this.setName(name);
        System.out.println("Trainee registered successfully: " + name);
        System.out.println("A verification code has been sent to: " + email);
    }

    // Verifies the trainee's account using a code (simulated here).
     
    public void verifyAccount(String code) throws IllegalArgumentException {
        if (code == null || code.isEmpty())
            throw new IllegalArgumentException("Verification code cannot be empty.");
        // In real system, compare against the OTP sent to email/phone
        this.setVerified(true);
        System.out.println("Account verified successfully for: " + getName());
    }

   // Allows trainee to set or update their fitness goal (target weight).
    public void setFitnessGoal(float goalWeight) throws IllegalArgumentException {
        if (goalWeight <= 0)
            throw new IllegalArgumentException("Fitness goal must be a positive value.");
        this.fitnessGoal = goalWeight;
        System.out.println("Fitness goal set to: " + goalWeight + " kg");
    }

    // Allows trainee to update their health metrics (height and weight).
    public void setHealthMetrics(float height, float weight) throws IllegalArgumentException {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive.");
        if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");
        this.height = height;
        this.weight = weight;
        System.out.println("Health metrics updated - Height: " + height + " cm, Weight: " + weight + " kg");
    }

  // Calculates BMI based on current height and weight.
    public float calculateBMI() {
        float heightInMeters = height / 100;
        this.bmi = weight / (heightInMeters * heightInMeters);
        System.out.printf("BMI calculated: %.2f%n", bmi);
        return bmi;
    }

 // Views the trainee's assigned workout schedule.
    public void viewWorkoutSchedule() {
        System.out.println("=== Weekly Workout Schedule for: " + getName() + " ===");
        System.out.println("(Schedule assigned by Trainer - fetched from WorkoutSchedule module)");
    }

    // Views the trainee's assigned meal plan.
    public void viewMealPlan() {
        System.out.println("=== Weekly Meal Plan for: " + getName() + " ===");
        System.out.println("(Meal plan assigned by Nutritionist - fetched from MealPlan module)");
    }

  // Allows trainee to enter a custom meal with calorie count for the day.
    public void enterCustomMeal(String mealName, float calories) throws IllegalArgumentException {
        if (mealName == null || mealName.isEmpty())
            throw new IllegalArgumentException("Meal name cannot be empty.");
        if (calories < 0)
            throw new IllegalArgumentException("Calories cannot be negative.");
        mealsToday.add(mealName + " (" + calories + " kcal)");
        totalCaloriesToday += calories;
        System.out.println("Meal added: " + mealName + " - " + calories + " kcal");
        System.out.println("Total calories today: " + totalCaloriesToday + " kcal");
    }

   // Views progress towards fitness goal as a percentage.
    public void viewProgress() {
        if (weight != fitnessGoal) {
            progressPercentage = (1 - (Math.abs(weight - fitnessGoal) / Math.abs(weight - fitnessGoal))) * 100;
        } else {
            progressPercentage = 100;
        }
        System.out.println("=== Progress for: " + getName() + " ===");
        System.out.printf("Current Weight : %.1f kg%n", weight);
        System.out.printf("Goal Weight    : %.1f kg%n", fitnessGoal);
        System.out.printf("Progress       : %.1f%%%n", progressPercentage);
    }

   // Views personal information including health metrics, fitness goal, BMI, and calories.
    public void viewPersonalInfo() {
        viewProfile();
        System.out.println("Age            : " + age);
        System.out.printf("Height         : %.1f cm%n", height);
        System.out.printf("Weight         : %.1f kg%n", weight);
        System.out.printf("Fitness Goal   : %.1f kg%n", fitnessGoal);
        System.out.printf("BMI            : %.2f%n", bmi);
        System.out.printf("Calories Today : %.1f kcal%n", totalCaloriesToday);
    }

  // Logs workout performance (sets, reps, weight) for an exercise.
    public void logWorkoutPerformance(String exerciseName, int sets, int reps, float actualWeight)
            throws IllegalArgumentException {
        if (exerciseName == null || exerciseName.isEmpty())
            throw new IllegalArgumentException("Exercise name cannot be empty.");
        if (sets <= 0 || reps <= 0)
            throw new IllegalArgumentException("Sets and reps must be greater than zero.");
        System.out.println("Performance logged for: " + exerciseName);
        System.out.println("  Sets: " + sets + ", Reps: " + reps + ", Weight: " + actualWeight + " kg");
    }

    // Views predicted goal completion date based on current progress and goal.
    public void viewPredictedCompletionDate() {
        System.out.println("Predicted goal completion: " + predictedCompletionDate);
    }

    // Getters and setters
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public float getFitnessGoal() { return fitnessGoal; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public float getBmi() { return bmi; }

    public float getTotalCaloriesToday() { return totalCaloriesToday; }

    public float getDailyCalorieTarget() { return dailyCalorieTarget; }
    public void setDailyCalorieTarget(float target) { this.dailyCalorieTarget = target; }

    public List<String> getMealsToday() { return mealsToday; }

    public float getProgressPercentage() { return progressPercentage; }

    public String getPredictedCompletionDate() { return predictedCompletionDate; }
    public void setPredictedCompletionDate(String date) { this.predictedCompletionDate = date; }
}


//Nutritionist class
public class Nutritionist extends User {

    private float fee;
    private String credentials;
    private String specialization;
    private boolean isApproved;

    // Constructor called by ADMIN only to create a Nutritionist account.
    public Nutritionist(String username, String password, String name, String dob,
                        String email, String phone, float fee, String credentials,
                        String specialization) {
        super(username, "Nutritionist", password, name, dob, email, phone);
        // isVerified is set to true in parent - admin-created accounts are pre-verified
        this.fee = fee;
        this.credentials = credentials;
        this.specialization = specialization;
        this.isApproved = true; // Admin already approved when creating the account
    }

    // Creates a meal plan for a specific trainee based on their calorie target.
    public void createMealPlan(String traineeUsername, float dailyCalorieTarget)
            throws IllegalArgumentException {
        if (traineeUsername == null || traineeUsername.isEmpty())
            throw new IllegalArgumentException("Trainee username cannot be empty.");
        if (dailyCalorieTarget <= 0)
            throw new IllegalArgumentException("Daily calorie target must be positive.");
        System.out.println("Meal plan created by " + getName() + " for trainee: " + traineeUsername);
        System.out.println("Daily Calorie Target: " + dailyCalorieTarget + " kcal");
    }

    // Edits an existing meal plan for a trainee.
    public void editMealPlan(int mealPlanId, String updatedDetails)
            throws IllegalArgumentException {
        if (mealPlanId <= 0)
            throw new IllegalArgumentException("Invalid meal plan ID.");
        if (updatedDetails == null || updatedDetails.isEmpty())
            throw new IllegalArgumentException("Updated details cannot be empty.");
        System.out.println("Meal plan ID " + mealPlanId + " updated by: " + getName());
        System.out.println("Changes: " + updatedDetails);
    }

    //Trainee progress tracking for nutrition
    public void trackTraineeProgress(String traineeUsername) throws IllegalArgumentException {
        if (traineeUsername == null || traineeUsername.isEmpty())
            throw new IllegalArgumentException("Trainee username cannot be empty.");
        System.out.println("Tracking progress for trainee: " + traineeUsername + " by Nutritionist: " + getName());
    }

    // view nutritionist profile with credentials, specialization, fee, and approval status
    @Override
    public void viewProfile() {
        super.viewProfile();
        System.out.println("Credentials   : " + credentials);
        System.out.println("Specialization: " + specialization);
        System.out.printf("Monthly Fee   : %.0f SAR%n", fee);
        System.out.println("Approved      : " + isApproved);
    }
// getters and setters
    public float getFee() { return fee; }
    public void setFee(float fee) throws IllegalArgumentException {
        if (fee < 59 || fee > 89)
            throw new IllegalArgumentException("Fee must be between 59 and 89 SAR.");
        this.fee = fee;
    }

    public String getCredentials() { return credentials; }
    public void setCredentials(String credentials) { this.credentials = credentials; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { this.isApproved = approved; }
}


//Trainer class
public class Trainer extends User {

    private float fee;
    private String credentials;
    private String specialization;
    private boolean isApproved;

    // Constructor called by ADMIN only to create a Trainer account.
    public Trainer(String username, String password, String name, String dob,
                   String email, String phone, float fee, String credentials,
                   String specialization) {
        super(username, "Trainer", password, name, dob, email, phone);
        // isVerified is set to true in parent - admin-created accounts are pre-verified
        this.fee = fee;
        this.credentials = credentials;
        this.specialization = specialization;
        this.isApproved = true; // Admin already approved when creating the account
    }

    // Creates a workout schedule for a specific trainee based on their goal.
    public void createWorkoutSchedule(String traineeUsername, String goal)
            throws IllegalArgumentException {
        if (traineeUsername == null || traineeUsername.isEmpty())
            throw new IllegalArgumentException("Trainee username cannot be empty.");
        if (goal == null || goal.isEmpty())
            throw new IllegalArgumentException("Goal cannot be empty.");
        System.out.println("Workout schedule created by " + getName() + " for trainee: " + traineeUsername);
        System.out.println("Goal: " + goal);
    }

    // Edits an existing workout schedule for a trainee.
     
    public void editWorkoutSchedule(int scheduleId, String updatedDetails)
            throws IllegalArgumentException {
        if (scheduleId <= 0)
            throw new IllegalArgumentException("Invalid schedule ID.");
        if (updatedDetails == null || updatedDetails.isEmpty())
            throw new IllegalArgumentException("Updated details cannot be empty.");
        System.out.println("Workout schedule ID " + scheduleId + " updated by: " + getName());
        System.out.println("Changes: " + updatedDetails);
    }

    // Adds an exercise to a workout schedule with a tutorial link.
    public void addExerciseWithTutorial(int scheduleId, String exerciseName, String tutorialLink)
            throws IllegalArgumentException {
        if (exerciseName == null || exerciseName.isEmpty())
            throw new IllegalArgumentException("Exercise name cannot be empty.");
        if (tutorialLink == null || tutorialLink.isEmpty())
            throw new IllegalArgumentException("Tutorial link cannot be empty.");
        System.out.println("Exercise added to schedule " + scheduleId + ": " + exerciseName);
        System.out.println("Tutorial: " + tutorialLink);
    }

    //Trainee progress tracking
    public void trackTraineeProgress(String traineeUsername) throws IllegalArgumentException {
        if (traineeUsername == null || traineeUsername.isEmpty())
            throw new IllegalArgumentException("Trainee username cannot be empty.");
        System.out.println("Tracking progress for trainee: " + traineeUsername + " by Trainer: " + getName());
    }

    
    @Override
    public void viewProfile() {
        super.viewProfile();
        System.out.println("Credentials   : " + credentials);
        System.out.println("Specialization: " + specialization);
        System.out.printf("Monthly Fee   : %.0f SAR%n", fee);
        System.out.println("Approved      : " + isApproved);
    }
// getters and setters
    public float getFee() { return fee; }
    public void setFee(float fee) throws IllegalArgumentException {
        if (fee < 59 || fee > 89)
            throw new IllegalArgumentException("Fee must be between 59 and 89 SAR.");
        this.fee = fee;
    }

    public String getCredentials() { return credentials; }
    public void setCredentials(String credentials) { this.credentials = credentials; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { this.isApproved = approved; }
}
}