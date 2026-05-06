package gui;

import auth.SessionManager;
import gui.panels.*;
import gui.util.AppColors;
import gui.util.AppFonts;
import gui.util.UIHelper;
import user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final int SIDEBAR_W = 200;

    private JPanel    contentArea;
    private CardLayout cards;
    private JButton   activeBtn;

    public MainFrame() {
        User user = SessionManager.getCurrentUser();
        setTitle("Gymanice – " + user.getName() + " (" + user.getRole() + ")");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        buildUI(user.getRole());
    }

    private void buildUI(String role) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_PRIMARY);

        root.add(buildSidebar(role), BorderLayout.WEST);
        root.add(buildContent(role), BorderLayout.CENTER);

        setContentPane(root);
    }

    // ── Sidebar ───────────────────────────────────────────────

    private JPanel buildSidebar(String role) {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(SIDEBAR_W, 0));
        sidebar.setBackground(AppColors.SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo
        JLabel logo = UIHelper.label("GYMANICE", AppFonts.HEADING, AppColors.ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(new EmptyBorder(28, 0, 8, 0));

        User user = SessionManager.getCurrentUser();
        JLabel username = UIHelper.muted(user.getName());
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setBorder(new EmptyBorder(0, 0, 24, 0));

        sidebar.add(logo);
        sidebar.add(username);
        sidebar.add(separator());

        // Navigation buttons based on role
        String[] panels = switch (role) {
            case "TRAINER"      -> new String[]{"Profile", "My Schedules", "Payments Received"};
            case "NUTRITIONIST" -> new String[]{"Profile", "Meal Plans"};
            default             -> new String[]{"Profile", "Workout", "Nutrition", "Progress", "Payment"};
        };

        for (String panel : panels) {
            JButton btn = navButton(panel);
            sidebar.add(btn);
            btn.addActionListener(e -> switchPanel(panel, btn));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(separator());

        JButton logoutBtn = navButton("Logout");
        logoutBtn.setForeground(AppColors.DANGER);
        logoutBtn.addActionListener(e -> logout());
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(16));

        return sidebar;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton("  " + text);
        btn.setFont(AppFonts.NAV);
        btn.setForeground(AppColors.TEXT_MUTED);
        btn.setBackground(AppColors.SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(SIDEBAR_W, 46));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeBtn) btn.setBackground(AppColors.SIDEBAR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeBtn) btn.setBackground(AppColors.SIDEBAR);
            }
        });
        return btn;
    }

    private JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(AppColors.BORDER);
        sep.setMaximumSize(new Dimension(SIDEBAR_W, 1));
        return sep;
    }

    // ── Content area ──────────────────────────────────────────

    private JPanel buildContent(String role) {
        cards       = new CardLayout();
        contentArea = new JPanel(cards);
        contentArea.setBackground(AppColors.BG_PRIMARY);

        switch (role) {
            case "TRAINER" -> {
                contentArea.add(new ProfilePanel(),          "Profile");
                contentArea.add(new TrainerSchedulesPanel(), "My Schedules");
                contentArea.add(new PaymentPanel(),          "Payments Received");
            }
            case "NUTRITIONIST" -> {
                contentArea.add(new ProfilePanel(),    "Profile");
                contentArea.add(new NutritionPanel(),  "Meal Plans");
            }
            default -> {
                contentArea.add(new ProfilePanel(),   "Profile");
                contentArea.add(new WorkoutPanel(),   "Workout");
                contentArea.add(new NutritionPanel(), "Nutrition");
                contentArea.add(new ProgressPanel(),  "Progress");
                contentArea.add(new PaymentPanel(),   "Payment");
            }
        }

        cards.show(contentArea, "Profile");
        return contentArea;
    }

    private void switchPanel(String name, JButton btn) {
        cards.show(contentArea, name);
        if (activeBtn != null) {
            activeBtn.setBackground(AppColors.SIDEBAR);
            activeBtn.setForeground(AppColors.TEXT_MUTED);
        }
        activeBtn = btn;
        btn.setBackground(AppColors.SIDEBAR_ACTIVE);
        btn.setForeground(AppColors.ACCENT);
    }

    private void logout() {
        if (UIHelper.confirm(this, "Are you sure you want to logout?")) {
            SessionManager.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
