package db;

import java.sql.*;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:gymanice.db";
    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            try (Statement st = connection.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }
        }
        return connection;
    }

    public static void initialize() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            createTables(st);
            seedExercises(conn);
            seedPresetMeals(conn);
        } catch (SQLException e) {
            System.err.println("DB init error: " + e.getMessage());
        }
    }

    private static void createTables(Statement st) throws SQLException {

        st.execute("""
            CREATE TABLE IF NOT EXISTS users (
                user_id    INTEGER PRIMARY KEY AUTOINCREMENT,
                username   TEXT NOT NULL UNIQUE,
                email      TEXT NOT NULL UNIQUE,
                password   TEXT NOT NULL,
                full_name  TEXT NOT NULL,
                dob        TEXT NOT NULL,
                phone      TEXT,
                role       TEXT NOT NULL,
                is_verified INTEGER NOT NULL DEFAULT 1,
                created_at TEXT NOT NULL DEFAULT (datetime('now'))
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS trainees (
                trainee_id   INTEGER PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
                height_cm    REAL,
                weight_kg    REAL,
                age          INTEGER,
                fitness_goal TEXT
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS trainers (
                trainer_id     INTEGER PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
                credentials    TEXT,
                specialization TEXT,
                monthly_fee    REAL NOT NULL DEFAULT 0
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS nutritionists (
                nutritionist_id INTEGER PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
                credentials     TEXT,
                specialization  TEXT,
                monthly_fee     REAL NOT NULL DEFAULT 0
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS exercises (
                exercise_id   INTEGER PRIMARY KEY AUTOINCREMENT,
                name          TEXT NOT NULL UNIQUE,
                muscle_group  TEXT NOT NULL,
                description   TEXT,
                tutorial_link TEXT
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS workout_schedules (
                schedule_id   INTEGER PRIMARY KEY AUTOINCREMENT,
                trainer_id    INTEGER NOT NULL REFERENCES trainers(trainer_id) ON DELETE CASCADE,
                trainee_id    INTEGER NOT NULL REFERENCES trainees(trainee_id) ON DELETE CASCADE,
                schedule_date TEXT NOT NULL,
                notes         TEXT,
                created_at    TEXT NOT NULL DEFAULT (datetime('now'))
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS schedule_exercises (
                schedule_id   INTEGER NOT NULL REFERENCES workout_schedules(schedule_id) ON DELETE CASCADE,
                exercise_id   INTEGER NOT NULL REFERENCES exercises(exercise_id) ON DELETE CASCADE,
                target_sets   INTEGER NOT NULL DEFAULT 3,
                target_reps   INTEGER NOT NULL DEFAULT 10,
                target_weight INTEGER NOT NULL DEFAULT 0,
                sort_order    INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY (schedule_id, exercise_id)
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS exercise_performances (
                performance_id INTEGER PRIMARY KEY AUTOINCREMENT,
                schedule_id    INTEGER NOT NULL REFERENCES workout_schedules(schedule_id) ON DELETE CASCADE,
                exercise_id    INTEGER NOT NULL REFERENCES exercises(exercise_id) ON DELETE CASCADE,
                trainee_id     INTEGER NOT NULL REFERENCES trainees(trainee_id) ON DELETE CASCADE,
                logged_date    TEXT NOT NULL,
                actual_sets    INTEGER NOT NULL DEFAULT 0,
                actual_reps    INTEGER NOT NULL DEFAULT 0,
                actual_weight  INTEGER NOT NULL DEFAULT 0,
                notes          TEXT
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS meals (
                meal_id   INTEGER PRIMARY KEY AUTOINCREMENT,
                meal_name TEXT NOT NULL,
                calories  REAL NOT NULL DEFAULT 0,
                protein_g REAL NOT NULL DEFAULT 0,
                carbs_g   REAL NOT NULL DEFAULT 0,
                fat_g     REAL NOT NULL DEFAULT 0,
                meal_type TEXT NOT NULL DEFAULT 'PRESET'
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS preset_meals (
                meal_id       INTEGER PRIMARY KEY REFERENCES meals(meal_id) ON DELETE CASCADE,
                category      TEXT NOT NULL DEFAULT 'LUNCH',
                prep_time_min INTEGER NOT NULL DEFAULT 0,
                recipe        TEXT
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS custom_meals (
                meal_id    INTEGER PRIMARY KEY REFERENCES meals(meal_id) ON DELETE CASCADE,
                trainee_id INTEGER NOT NULL REFERENCES trainees(trainee_id) ON DELETE CASCADE,
                notes      TEXT
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS meal_plans (
                plan_id           INTEGER PRIMARY KEY AUTOINCREMENT,
                plan_name         TEXT NOT NULL,
                nutritionist_id   INTEGER NOT NULL REFERENCES nutritionists(nutritionist_id) ON DELETE CASCADE,
                trainee_id        INTEGER NOT NULL REFERENCES trainees(trainee_id) ON DELETE CASCADE,
                daily_target_cal  REAL NOT NULL DEFAULT 2000,
                status            TEXT NOT NULL DEFAULT 'DRAFT',
                created_at        TEXT NOT NULL DEFAULT (datetime('now'))
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS meal_plan_items (
                plan_id    INTEGER NOT NULL REFERENCES meal_plans(plan_id) ON DELETE CASCADE,
                meal_id    INTEGER NOT NULL REFERENCES meals(meal_id) ON DELETE CASCADE,
                day_number INTEGER NOT NULL DEFAULT 1,
                sort_order INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY (plan_id, meal_id, day_number)
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS progress_records (
                record_id         INTEGER PRIMARY KEY AUTOINCREMENT,
                trainee_id        INTEGER NOT NULL REFERENCES trainees(trainee_id) ON DELETE CASCADE,
                record_date       TEXT NOT NULL,
                weight_kg         REAL NOT NULL,
                height_cm         REAL,
                initial_weight_kg REAL,
                target_weight_kg  REAL,
                progress_percent  REAL,
                predicted_completion TEXT,
                notes             TEXT,
                UNIQUE (trainee_id, record_date)
            )""");

        st.execute("""
            CREATE TABLE IF NOT EXISTS payments (
                payment_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                trainee_id      INTEGER NOT NULL REFERENCES trainees(trainee_id) ON DELETE RESTRICT,
                trainer_id      INTEGER NOT NULL REFERENCES trainers(trainer_id)  ON DELETE RESTRICT,
                payment_type    TEXT NOT NULL DEFAULT 'MADA',
                amount_sar      REAL NOT NULL,
                status          TEXT NOT NULL DEFAULT 'PENDING',
                transaction_ref TEXT UNIQUE,
                payment_date    TEXT NOT NULL DEFAULT (datetime('now'))
            )""");
    }

    private static void seedExercises(Connection conn) throws SQLException {
        String check = "SELECT COUNT(*) FROM exercises";
        try (ResultSet rs = conn.createStatement().executeQuery(check)) {
            if (rs.getInt(1) > 0) return;
        }
        String[][] exercises = {
            {"Barbell Squat",     "Legs",       "Keep back straight, knees over toes"},
            {"Deadlift",          "Back",        "Hinge at hips, neutral spine"},
            {"Bench Press",       "Chest",       "Grip slightly wider than shoulder width"},
            {"Pull-Up",           "Back",        "Full range of motion, controlled descent"},
            {"Overhead Press",    "Shoulders",   "Press straight overhead, brace core"},
            {"Barbell Row",       "Back",        "Pull to lower chest, elbows close"},
            {"Lunges",            "Legs",        "Step forward, knee above ankle"},
            {"Dumbbell Curl",     "Biceps",      "Controlled curl, no swinging"},
            {"Tricep Pushdown",   "Triceps",     "Keep elbows fixed at sides"},
            {"Plank",             "Core",        "Neutral spine, engage core throughout"},
            {"Romanian Deadlift", "Hamstrings",  "Hinge at hips, slight knee bend"},
            {"Lateral Raise",     "Shoulders",   "Raise to shoulder height, slight bend in elbow"}
        };
        String sql = "INSERT INTO exercises (name, muscle_group, description) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String[] ex : exercises) {
                ps.setString(1, ex[0]);
                ps.setString(2, ex[1]);
                ps.setString(3, ex[2]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void seedPresetMeals(Connection conn) throws SQLException {
        String check = "SELECT COUNT(*) FROM meals";
        try (ResultSet rs = conn.createStatement().executeQuery(check)) {
            if (rs.getInt(1) > 0) return;
        }
        Object[][] meals = {
            {"Oatmeal with Berries",       350, 12, 55, 8,  "BREAKFAST", 10},
            {"Scrambled Eggs & Toast",     420, 24, 38, 14, "BREAKFAST", 15},
            {"Grilled Chicken & Rice",     520, 45, 50, 10, "LUNCH",     25},
            {"Tuna Salad Wrap",            380, 30, 35, 10, "LUNCH",     10},
            {"Salmon & Vegetables",        480, 42, 20, 20, "DINNER",    30},
            {"Beef Stir Fry",              560, 40, 45, 18, "DINNER",    25},
            {"Greek Yogurt & Nuts",        280, 18, 22, 12, "SNACK",      5},
            {"Protein Shake & Banana",     310, 28, 40,  5, "SNACK",      5},
            {"Quinoa & Black Beans",       440, 22, 68, 10, "LUNCH",     20},
            {"Turkey & Avocado Sandwich",  490, 35, 42, 16, "LUNCH",     10}
        };
        String mealSql = "INSERT INTO meals (meal_name, calories, protein_g, carbs_g, fat_g, meal_type) VALUES (?,?,?,?,?,'PRESET')";
        String presetSql = "INSERT INTO preset_meals (meal_id, category, prep_time_min) VALUES (?,?,?)";
        try (PreparedStatement mp = conn.prepareStatement(mealSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pp = conn.prepareStatement(presetSql)) {
            for (Object[] m : meals) {
                mp.setString(1, (String) m[0]);
                mp.setInt(2,    (int) m[1]);
                mp.setInt(3,    (int) m[2]);
                mp.setInt(4,    (int) m[3]);
                mp.setInt(5,    (int) m[4]);
                mp.executeUpdate();
                try (ResultSet gk = mp.getGeneratedKeys()) {
                    if (gk.next()) {
                        pp.setInt(1, gk.getInt(1));
                        pp.setString(2, (String) m[5]);
                        pp.setInt(3, (int) m[6]);
                        pp.executeUpdate();
                    }
                }
            }
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException ignored) {}
    }
}
