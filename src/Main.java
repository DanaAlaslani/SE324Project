import user.Trainee;
import user.Trainer;
import user.Nutritionist;
import workout.Exercise;
import workout.WorkoutSchedule;
import workout.ExercisePerformance;
import nutrition.PresetMeal;
import nutrition.CustomMeal;
import nutrition.MealPlan;
import nutrition.NutritionService;
import calculator.Calculator;
import calculator.Ingredient;
import progress.ProgressTracker;
import payment.Payment;
import payment.PaymentService;
import payment.PaymentException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("       GYMANICE FITNESS SYSTEM           ");
        System.out.println("==========================================\n");

        // --- 1. USER MANAGEMENT ---
        System.out.println("--- [1] USER MANAGEMENT ---");

        Trainer trainer = new Trainer(
                "trainer_ahmed", "pass1234", "Ahmed Ali", "1990-05-10",
                "ahmed@gym.com", "0512345678", 299.0f, "NASM Certified", "Strength & Conditioning");
        trainer.viewProfile();

        Nutritionist nutritionist = new Nutritionist(
                "nutri_sara", "pass5678", "Sara Hassan", "1988-03-15",
                "sara@gym.com", "0523456789", 199.0f, "Registered Dietitian", "Sports Nutrition");
        nutritionist.viewProfile();

        Trainee trainee = new Trainee(
                "trainee_rima", "pass9012", "Rima Al-Saedi", "2000-07-20",
                "rima@gmail.com", "0534567890", 165.0f, 72.0f, 24, "Lose Weight");
        trainee.viewProfile();

        // --- 2. BMI CALCULATOR ---
        System.out.println("\n--- [2] BMI CALCULATOR ---");
        Calculator calc = new Calculator();
        float bmi = calc.calculateBMI(trainee.getHeight(), trainee.getWeight());
        System.out.printf("BMI: %.2f  ->  Category: %s%n", bmi, calc.getBMICategory(bmi));

        // --- 3. WORKOUT MODULE ---
        System.out.println("\n--- [3] WORKOUT SCHEDULE ---");
        trainer.createWorkoutSchedule(trainee.getUsername(), trainee.getFitnessGoal());

        WorkoutSchedule schedule = new WorkoutSchedule(1, 1, 1, new Date());
        schedule.addExercise(new Exercise("Barbell Squat", "Legs",
                "Keep back straight, knees over toes", "https://youtube.com/squat", 4, 10, 60));
        schedule.addExercise(new Exercise("Bench Press", "Chest",
                "Grip slightly wider than shoulder width", "https://youtube.com/bench", 3, 12, 50));
        schedule.addExercise(new Exercise("Deadlift", "Back",
                "Hinge at hips, maintain neutral spine", "https://youtube.com/deadlift", 3, 8, 80));

        System.out.println(schedule);
        for (Exercise e : schedule.getExercises())
            System.out.println("  * " + e);

        ExercisePerformance perf = new ExercisePerformance(1, 1, new Date(), 4, 10, 55);
        System.out.println("\nLogged Performance:");
        perf.logPerformance();

        // --- 4. NUTRITION MODULE ---
        System.out.println("\n--- [4] NUTRITION MODULE ---");
        NutritionService nutritionSvc = new NutritionService(1);
        MealPlan plan = nutritionSvc.createMealPlan(1, 1800.0f, "Rima Weight-Loss Plan");

        PresetMeal breakfast = new PresetMeal("Oatmeal with Berries", 350, 12, 55, 8, "Breakfast", 10);
        PresetMeal lunch     = new PresetMeal("Grilled Chicken & Rice", 520, 45, 50, 10, "Lunch", 25);
        CustomMeal dinner    = new CustomMeal("Light Garden Salad", 280, 20, 15, 12, 1, "Low carb");

        nutritionSvc.addMealToPlan(plan, breakfast);
        nutritionSvc.addMealToPlan(plan, lunch);
        nutritionSvc.addMealToPlan(plan, dinner);
        plan.activate();
        plan.displayMealPlan();

        System.out.println("\nMacros for Breakfast:");
        Map<String, Double> macros = calc.getMacros(breakfast);
        macros.forEach((k, v) -> System.out.printf("  %-10s: %.1f%n", k, v));

        System.out.println("\nCustom Ingredient Calculation (150g Chicken + 100g Rice):");
        Ingredient chicken = new Ingredient("Chicken Breast", 0, 31, 3.6f, 0);
        Ingredient rice    = new Ingredient("White Rice", 80, 7, 0.3f, 0);
        Map<Ingredient, Float> ingredientMap = new HashMap<>();
        ingredientMap.put(chicken, 150f);
        ingredientMap.put(rice, 100f);
        Map<String, Float> customMacros = calc.calculateMacrosFromIngredients(ingredientMap);
        customMacros.forEach((k, v) -> System.out.printf("  %-10s: %.1f%n", k, v));

        // --- 5. PROGRESS TRACKING ---
        System.out.println("\n--- [5] PROGRESS TRACKING ---");
        ProgressTracker tracker = new ProgressTracker(1, 72.0f, 62.0f);
        tracker.updateWeight(70.5f);
        tracker.updateWeight(69.0f);
        tracker.displayProgress();

        // --- 6. PAYMENT SYSTEM ---
        System.out.println("\n--- [6] PAYMENT SYSTEM ---");
        PaymentService paymentSvc = new PaymentService();
        try {
            Payment p1 = paymentSvc.makePayment(1, 1, "MADA", 299.0);
            System.out.printf("Status : %s | Ref: %s%n", p1.getStatus(), p1.getTransactionRef());
            System.out.printf("Platform Fee (5%%): %.2f SAR | Trainer Earns: %.2f SAR%n",
                    p1.getPlatformFee(), p1.getTrainerAmount());
            paymentSvc.makePayment(1, 1, "APPLE_PAY", 199.0);
        } catch (PaymentException e) {
            System.out.println("Payment error: " + e.getMessage());
        }
        paymentSvc.printSummary();

        // --- 7. PROFILE EDIT ---
        System.out.println("\n--- [7] PROFILE EDIT ---");
        trainee.editProfile("Rima Alsaedi", "rima.updated@gmail.com", "0599999999");
        trainee.resetPassword("newSecure123");
        trainee.setHealthMetrics(165.0f, 69.0f);
        float updatedBmi = calc.calculateBMI(165.0f, 69.0f);
        System.out.printf("Updated BMI: %.2f  ->  %s%n", updatedBmi, calc.getBMICategory(updatedBmi));

        System.out.println("\n==========================================");
        System.out.println("         System demo complete.           ");
        System.out.println("==========================================");
    }
}
