package gui;

import auth.AuthService;
import auth.SessionManager;
import gui.util.AppColors;
import gui.util.AppFonts;
import gui.util.UIHelper;
import user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private JTextField      emailField;
    private JPasswordField  passwordField;
    private JLabel          errorLabel;

    public LoginFrame() {
        setTitle("Gymanice – Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_PRIMARY);

        // ── Header ──
        JPanel header = new JPanel();
        header.setBackground(AppColors.BG_PRIMARY);
        header.setBorder(new EmptyBorder(48, 0, 24, 0));
        JLabel title = new JLabel("GYMANICE");
        title.setFont(AppFonts.TITLE);
        title.setForeground(AppColors.ACCENT);
        JLabel sub = new JLabel("Your Personal Fitness Platform");
        sub.setFont(AppFonts.SMALL);
        sub.setForeground(AppColors.TEXT_MUTED);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(sub);

        // ── Form card ──
        JPanel card = UIHelper.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 36, 32, 36));

        JLabel formTitle = UIHelper.heading("Sign In");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField    = UIHelper.inputField("Email");
        passwordField = UIHelper.passwordField();

        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppFonts.SMALL);
        errorLabel.setForeground(AppColors.DANGER);

        JButton loginBtn = UIHelper.primaryButton("Sign In");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton registerBtn = UIHelper.ghostButton("Create Account");
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        card.add(formTitle);
        card.add(Box.createVerticalStrut(24));
        card.add(fieldRow("Email", emailField));
        card.add(Box.createVerticalStrut(14));
        card.add(fieldRow("Password", passwordField));
        card.add(Box.createVerticalStrut(8));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(registerBtn);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(AppColors.BG_PRIMARY);
        center.setBorder(new EmptyBorder(0, 40, 40, 40));
        center.add(card);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);

        // ── Actions ──
        loginBtn.addActionListener(e -> attemptLogin());
        registerBtn.addActionListener(e -> openRegister());
        getRootPane().setDefaultButton(loginBtn);
    }

    private JPanel fieldRow(String label, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(AppColors.BG_CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = UIHelper.muted(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.add(lbl);
        row.add(Box.createVerticalStrut(4));
        row.add(field);
        return row;
    }

    private void attemptLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            User user = AuthService.login(email, password);
            if (user == null) {
                errorLabel.setText("Invalid email or password.");
                return;
            }
            SessionManager.login(user);
            dispose();
            new MainFrame().setVisible(true);
        } catch (SQLException ex) {
            UIHelper.showError(this, "Database error: " + ex.getMessage());
        }
    }

    private void openRegister() {
        dispose();
        new RegisterFrame().setVisible(true);
    }
}
