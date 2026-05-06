package gui;

import auth.AuthService;
import gui.util.AppColors;
import gui.util.AppFonts;
import gui.util.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {

    private JComboBox<String> roleBox;
    private JTextField usernameField, nameField, emailField, phoneField, dobField;
    private JPasswordField passwordField;

    // Trainee extras
    private JTextField heightField, weightField, ageField, goalField;

    // Trainer / Nutritionist extras
    private JTextField feeField, credField, specField;

    private JPanel extraPanel;
    private CardLayout extraCards;

    public RegisterFrame() {
        setTitle("Gymanice – Create Account");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_PRIMARY);

        // ── Header ──
        JLabel title = UIHelper.label("Create Account", AppFonts.TITLE, AppColors.ACCENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(32, 0, 16, 0));

        // ── Scroll wrapper ──
        JPanel form = buildForm();
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColors.BG_PRIMARY);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildForm() {
        JPanel card = UIHelper.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 36, 24, 36));

        // Role selector
        roleBox = UIHelper.comboBox(new String[]{"TRAINEE", "TRAINER", "NUTRITIONIST"});
        card.add(fieldRow("Role", roleBox));
        card.add(gap());

        // Common fields
        usernameField = UIHelper.inputField("username");
        nameField     = UIHelper.inputField("Full Name");
        emailField    = UIHelper.inputField("email@example.com");
        passwordField = UIHelper.passwordField();
        phoneField    = UIHelper.inputField("05xxxxxxxx");
        dobField      = UIHelper.inputField("YYYY-MM-DD");

        card.add(fieldRow("Username",     usernameField));  card.add(gap());
        card.add(fieldRow("Full Name",    nameField));      card.add(gap());
        card.add(fieldRow("Email",        emailField));     card.add(gap());
        card.add(fieldRow("Password",     passwordField));  card.add(gap());
        card.add(fieldRow("Phone",        phoneField));     card.add(gap());
        card.add(fieldRow("Date of Birth (YYYY-MM-DD)", dobField)); card.add(gap());

        // ── Role-specific extra fields ──
        extraCards = new CardLayout();
        extraPanel = new JPanel(extraCards);
        extraPanel.setBackground(AppColors.BG_CARD);
        extraPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        extraPanel.add(buildTraineeExtras(),  "TRAINEE");
        extraPanel.add(buildProfExtras(),     "TRAINER");
        extraPanel.add(buildProfExtras2(),    "NUTRITIONIST");

        card.add(extraPanel);
        card.add(gap());

        JButton registerBtn = UIHelper.primaryButton("Create Account");
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton backBtn = UIHelper.ghostButton("Back to Login");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        card.add(registerBtn);
        card.add(gap());
        card.add(backBtn);

        roleBox.addActionListener(e ->
                extraCards.show(extraPanel, (String) roleBox.getSelectedItem()));
        registerBtn.addActionListener(e -> attemptRegister());
        backBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        JPanel wrap = UIHelper.darkPanel();
        wrap.setLayout(new BorderLayout());
        wrap.setBorder(new EmptyBorder(0, 32, 32, 32));
        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel buildTraineeExtras() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(AppColors.BG_CARD);
        heightField = UIHelper.inputField("e.g. 170");
        weightField = UIHelper.inputField("e.g. 70");
        ageField    = UIHelper.inputField("e.g. 25");
        goalField   = UIHelper.inputField("Lose Weight / Build Muscle / Maintain");
        p.add(fieldRow("Height (cm)", heightField)); p.add(gap());
        p.add(fieldRow("Weight (kg)", weightField)); p.add(gap());
        p.add(fieldRow("Age",         ageField));    p.add(gap());
        p.add(fieldRow("Fitness Goal",goalField));   p.add(gap());
        return p;
    }

    private JPanel buildProfExtras() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(AppColors.BG_CARD);
        feeField  = UIHelper.inputField("59 – 89 SAR");
        credField = UIHelper.inputField("e.g. NASM Certified");
        specField = UIHelper.inputField("e.g. Strength & Conditioning");
        p.add(fieldRow("Monthly Fee (SAR)", feeField)); p.add(gap());
        p.add(fieldRow("Credentials",       credField)); p.add(gap());
        p.add(fieldRow("Specialization",    specField)); p.add(gap());
        return p;
    }

    private JPanel buildProfExtras2() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(AppColors.BG_CARD);
        feeField  = UIHelper.inputField("59 – 89 SAR");
        credField = UIHelper.inputField("e.g. Registered Dietitian");
        specField = UIHelper.inputField("e.g. Sports Nutrition");
        p.add(fieldRow("Monthly Fee (SAR)", feeField)); p.add(gap());
        p.add(fieldRow("Credentials",       credField)); p.add(gap());
        p.add(fieldRow("Specialization",    specField)); p.add(gap());
        return p;
    }

    private void attemptRegister() {
        String role     = (String) roleBox.getSelectedItem();
        String username = usernameField.getText().trim();
        String name     = nameField.getText().trim();
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String phone    = phoneField.getText().trim();
        String dob      = dobField.getText().trim();

        if (username.isEmpty() || name.isEmpty() || email.isEmpty()
                || password.isEmpty() || dob.isEmpty()) {
            UIHelper.showError(this, "Please fill in all required fields.");
            return;
        }
        if (password.length() < 6) {
            UIHelper.showError(this, "Password must be at least 6 characters.");
            return;
        }

        try {
            boolean ok;
            switch (role) {
                case "TRAINEE" -> {
                    float h   = parseFloat(heightField.getText(), 0);
                    float w   = parseFloat(weightField.getText(), 0);
                    int   age = parseInt(ageField.getText(), 0);
                    String goal = goalField.getText().trim();
                    ok = AuthService.registerTrainee(username, email, password, name, dob, phone, h, w, age, goal);
                }
                case "TRAINER" -> {
                    float fee = parseFloat(feeField.getText(), 69);
                    ok = AuthService.registerTrainer(username, email, password, name, dob, phone,
                            fee, credField.getText().trim(), specField.getText().trim());
                }
                default -> {
                    float fee = parseFloat(feeField.getText(), 69);
                    ok = AuthService.registerNutritionist(username, email, password, name, dob, phone,
                            fee, credField.getText().trim(), specField.getText().trim());
                }
            }
            if (ok) {
                UIHelper.showSuccess(this, "Account created! Please sign in.");
                dispose();
                new LoginFrame().setVisible(true);
            }
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.contains("UNIQUE")) {
                UIHelper.showError(this, "Email or username already in use.");
            } else {
                UIHelper.showError(this, "Registration failed: " + msg);
            }
        }
    }

    private float parseFloat(String s, float def) {
        try { return Float.parseFloat(s.trim()); } catch (Exception e) { return def; }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private JPanel fieldRow(String labelText, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(AppColors.BG_CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = UIHelper.muted(labelText);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.add(lbl);
        row.add(Box.createVerticalStrut(4));
        row.add(field);
        return row;
    }

    private Component gap() { return Box.createVerticalStrut(12); }
}
