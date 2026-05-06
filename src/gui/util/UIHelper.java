package gui.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UIHelper {

    private UIHelper() {}

    // ── Buttons ──────────────────────────────────────────────

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(AppFonts.BUTTON);
        btn.setForeground(AppColors.TEXT_DARK);
        btn.setBackground(AppColors.ACCENT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(AppColors.ACCENT_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(AppColors.ACCENT);
            }
        });
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(AppFonts.BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(AppColors.DANGER);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(AppFonts.BUTTON);
        btn.setForeground(AppColors.ACCENT);
        btn.setBackground(AppColors.BG_PRIMARY);
        btn.setBorder(new LineBorder(AppColors.ACCENT, 1));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // ── Labels ────────────────────────────────────────────────

    public static JLabel label(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    public static JLabel heading(String text) {
        return label(text, AppFonts.HEADING, AppColors.TEXT_PRIMARY);
    }

    public static JLabel muted(String text) {
        return label(text, AppFonts.SMALL, AppColors.TEXT_MUTED);
    }

    // ── Inputs ────────────────────────────────────────────────

    public static JTextField inputField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(AppFonts.INPUT);
        field.setForeground(AppColors.TEXT_PRIMARY);
        field.setBackground(AppColors.INPUT_BG);
        field.setCaretColor(AppColors.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AppColors.BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
        return field;
    }

    public static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(AppFonts.INPUT);
        field.setForeground(AppColors.TEXT_PRIMARY);
        field.setBackground(AppColors.INPUT_BG);
        field.setCaretColor(AppColors.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AppColors.BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
        return field;
    }

    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(AppFonts.INPUT);
        box.setForeground(AppColors.TEXT_PRIMARY);
        box.setBackground(AppColors.INPUT_BG);
        return box;
    }

    // ── Table ─────────────────────────────────────────────────

    public static JTable styledTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(AppFonts.TABLE);
        table.setForeground(AppColors.TEXT_PRIMARY);
        table.setBackground(AppColors.BG_CARD);
        table.setGridColor(AppColors.BORDER);
        table.setRowHeight(32);
        table.setSelectionBackground(AppColors.ACCENT_DARK);
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(AppFonts.TABLE_HDR);
        header.setForeground(AppColors.TEXT_PRIMARY);
        header.setBackground(AppColors.BG_PRIMARY);
        header.setBorder(new LineBorder(AppColors.BORDER, 1));
        return table;
    }

    // ── Panels ────────────────────────────────────────────────

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(AppColors.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AppColors.BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)));
        return p;
    }

    public static JPanel darkPanel() {
        JPanel p = new JPanel();
        p.setBackground(AppColors.BG_PRIMARY);
        return p;
    }

    // ── Scroll pane ───────────────────────────────────────────

    public static JScrollPane scrollPane(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBackground(AppColors.BG_CARD);
        sp.getViewport().setBackground(AppColors.BG_CARD);
        sp.setBorder(new LineBorder(AppColors.BORDER, 1));
        return sp;
    }

    // ── Dialogs ───────────────────────────────────────────────

    public static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
