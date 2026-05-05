import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class App {



    public static void main(String[] args) {
        System.out.println("Gymanice System Loaded successfully.");
        // You can test your objects here
    }
}

// ==========================================
// USER MANAGEMENT MODULE
// ==========================================

abstract class User {
    protected String username;
    protected String role;
    protected String password;
    protected String name;
    protected String dob;
    protected String email;
    protected String phone;
    protected boolean isVerified;

    public User(String username, String role, String password, String name,
                String dob, String email, String phone) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.isVerified = true; 
    }

    public void resetPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }
        this.password = newPassword;
        System.out.println("Password reset successfully for: " + name);
    }

    public void viewProfile() {
        System.out.println("\n=== Profile ===");
        System.out.println("Name     : " + name);
        System.out.println("Username : " + username);
        System.out.println("Email    : " + email);
        System.out.println("Phone    : " + phone);
        System.out.println("Role     : " + role);
        System.out.println("Verified : " + isVerified);
    }

    public void editProfile(String newName, String newEmail, String newPhone) {
        if (newName == null || newName.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        if (newEmail == null || newEmail.isEmpty()) throw new IllegalArgumentException("Email cannot be empty.");
        
        this.name = newName;
        this.email = newEmail;
        this.phone = newPhone;
        System.out.println("Profile updated successfully.");
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}

class Trainee extends User {
    private float height;
    private float weight;
    private float fitnessGoal;
    private int age;
    private float bmi;
    private float totalCaloriesToday;
    private List<String> mealsToday;

    // Default constructor for registration
    public Trainee() {
        super(null, "Trainee", null, null, null, null, null);
        this.mealsToday = new ArrayList<>();
        this.setVerified(false); 
    }

    // Trainee-exclusive registration
    public void register(String username, String email, String password, String phone, String dob, String name) {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email required.");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password too short.");

        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.name = name;
        
        // TODO: Insert into your actual MySQL Database here
        // Example: dbConnection.insertTrainee(this);
        
        System.out.println("Trainee registered successfully. A verification code has been sent to " + email);
    }

    public void verifyAccount(String code) {
        if (code != null && !code.isEmpty()) {
            this.setVerified(true);
            System.out.println("Account verified successfully!");
        }
    }

    public void setHealthMetrics(float height, float weight) {
        this.height = height;
        this.weight = weight;
        calculateBMI();
    }

    private void calculateBMI() {
        float heightInMeters = height / 100;
        this.bmi = weight / (heightInMeters * heightInMeters);
    }
}

class Trainer extends User {
    private float fee;
    private String credentials;
    private String specialization;

    // Created exclusively by Admin/IT
    public Trainer(String username, String password, String name, String dob,
                   String email, String phone, float fee, String credentials, String specialization) {
        super(username, "Trainer", password, name, dob, email, phone);
        this.fee = fee;
        this.credentials = credentials;
        this.specialization = specialization;
    }

    public void createWorkoutSchedule(String traineeUsername, String goal) {
        System.out.println("Workout schedule created for " + traineeUsername + " focusing on: " + goal);
    }

    @Override
    public void viewProfile() {
        super.viewProfile();
        System.out.println("Credentials   : " + credentials);
        System.out.println("Specialization: " + specialization);
        System.out.printf("Monthly Fee   : %.0f SAR%n", fee);
    }
}

class Nutritionist extends User {
    private float fee;
    private String credentials;
    private String specialization;

    // Created exclusively by Admin/IT
    public Nutritionist(String username, String password, String name, String dob,
                        String email, String phone, float fee, String credentials, String specialization) {
        super(username, "Nutritionist", password, name, dob, email, phone);
        this.fee = fee;
        this.credentials = credentials;
        this.specialization = specialization;
    }

    public void createMealPlan(String traineeUsername, float dailyCalorieTarget) {
        System.out.println("Meal plan created for " + traineeUsername + " (" + dailyCalorieTarget + " kcal)");
    }

    @Override
    public void viewProfile() {
        super.viewProfile();
        System.out.println("Credentials   : " + credentials);
        System.out.println("Specialization: " + specialization);
        System.out.printf("Monthly Fee   : %.0f SAR%n", fee);
    }
}

// ==========================================
// NUTRITION & CALCULATOR MODULE
// ==========================================

abstract class Meal {
    protected int mealId;
    protected String name;
    protected String mealType;
    protected float carbs;
    protected float protein;
    protected float fat;
    protected float fiber; 
    protected float calories;

    public abstract void calculateMacros();

    public float getCarbs() { return carbs; }
    public float getProtein() { return protein; }
    public float getFat() { return fat; }
    public float getFiber() { return fiber; }
    public float getCalories() { return calories; }
    public String getName() { return name; }
}

class PresetMeal extends Meal {
    private String recipe;

    public PresetMeal(int mealId, String name) {
        this.mealId = mealId;
        this.name = name;
        calculateMacros(); 
    }

    @Override
    public void calculateMacros() {
        // TODO: Replace this with your actual MySQL JDBC connection
        // System.out.println("Fetching macros from Database for preset meal: " + this.name);
        
        // Simulated DB Data
        this.carbs = 45.0f;
        this.protein = 30.0f;
        this.fat = 15.0f;
        this.fiber = 8.0f;
        this.calories = (this.carbs * 4) + (this.protein * 4) + (this.fat * 9); 
    }

    public String getRecipe() { return recipe; }
}

class Ingredient {
    private String name;
    private float carbsPerGram;
    private float proteinPerGram;
    private float fatPerGram;
    private float fiberPerGram;

    public Ingredient(String name, float carbs, float protein, float fat, float fiber) {
        this.name = name;
        this.carbsPerGram = carbs / 100f; 
        this.proteinPerGram = protein / 100f;
        this.fatPerGram = fat / 100f;
        this.fiberPerGram = fiber / 100f;
    }

    public float getCarbsPerGram() { return carbsPerGram; }
    public float getProteinPerGram() { return proteinPerGram; }
    public float getFatPerGram() { return fatPerGram; }
    public float getFiberPerGram() { return fiberPerGram; }
}

class CustomMeal extends Meal {
    private Map<Ingredient, Float> ingredients = new HashMap<>();

    public CustomMeal(String name) {
        this.name = name;
    }

    public void addIngredient(Ingredient ingredient, float amountInGrams) {
        ingredients.put(ingredient, amountInGrams);
    }

    @Override
    public void calculateMacros() {
        this.carbs = 0; this.protein = 0; this.fat = 0; this.fiber = 0; this.calories = 0;

        for (Map.Entry<Ingredient, Float> entry : ingredients.entrySet()) {
            Ingredient ing = entry.getKey();
            float amount = entry.getValue();

            this.carbs += ing.getCarbsPerGram() * amount;
            this.protein += ing.getProteinPerGram() * amount;
            this.fat += ing.getFatPerGram() * amount;
            this.fiber += ing.getFiberPerGram() * amount;
        }
        
        this.calories = (this.carbs * 4) + (this.protein * 4) + (this.fat * 9);
        System.out.println("Custom Meal Macros Calculated Successfully.");
    }
}

class Calculator {
    public Map<String, Float> calculateMacros(Meal meal) {
        meal.calculateMacros(); 
        
        Map<String, Float> macros = new HashMap<>();
        macros.put("Carbs", meal.getCarbs());
        macros.put("Protein", meal.getProtein());
        macros.put("Fat", meal.getFat());
        macros.put("Fiber", meal.getFiber());
        macros.put("Calories", meal.getCalories());
        
        return macros;
    }

    public float calculateBMI(float heightInCm, float weightInKg) {
        float heightInMeters = heightInCm / 100f;
        return weightInKg / (heightInMeters * heightInMeters);
    }
}

// ==========================================
// PROGRESS TRACKING MODULE
// ==========================================

class ProgressTracker {
    private int traineeId;
    private Date startDate;
    private Date endDate;
    private float progress;
    private Date predictedCompletion;

    public ProgressTracker(int traineeId, Date startDate) {
        this.traineeId = traineeId;
        this.startDate = startDate;
    }

    public void calculateProgress(float initialWeight, float currentWeight, float targetWeight) {
        if (initialWeight == targetWeight) {
            this.progress = 100.0f;
            return;
        }
        
        float totalToLoseOrGain = Math.abs(initialWeight - targetWeight);
        float currentDifference = Math.abs(initialWeight - currentWeight);
        
        this.progress = (currentDifference / totalToLoseOrGain) * 100;
        
        if (this.progress > 100.0f) this.progress = 100.0f; 
    }

    public void predictCompletion(float currentWeight, float targetWeight) {
        float weightRemaining = Math.abs(currentWeight - targetWeight);
        int weeksRequired = (int) (weightRemaining / 0.5f); 
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); 
        cal.add(Calendar.WEEK_OF_YEAR, weeksRequired);
        
        this.predictedCompletion = cal.getTime();
    }

    public void displayProgress() {
        System.out.println("\n--- Progress Report for Trainee ID: " + traineeId + " ---");
        System.out.printf("Current Progress: %.2f%%\n", progress);
        System.out.println("Predicted Goal Completion: " + predictedCompletion);
    }
}