
// File: UITheme.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UITheme {
    // Color Palette
    public static final Color PRIMARY = new Color(99, 102, 241);
    public static final Color PRIMARY_HOVER = new Color(79, 70, 229);
    public static final Color ACCENT = new Color(16, 185, 129);
    public static final Color ACCENT_HOVER = new Color(5, 150, 105);
    public static final Color DANGER = new Color(239, 68, 68);
    public static final Color DANGER_HOVER = new Color(220, 38, 38);
    public static final Color WARNING = new Color(245, 158, 11);

    public static final Color BG_DARK = new Color(15, 23, 42);
    public static final Color BG_PANEL = new Color(30, 41, 59);
    public static final Color BG_CARD = new Color(51, 65, 85);
    public static final Color BG_INPUT = new Color(30, 41, 59);
    public static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color BORDER_COLOR = new Color(71, 85, 105);

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font LIST_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font LIST_SUB_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SEARCH_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public static void applyGlobalDefaults() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", BODY_FONT);
        UIManager.put("TextField.background", BG_INPUT);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("PasswordField.background", BG_INPUT);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", TEXT_PRIMARY);
        UIManager.put("ComboBox.background", BG_INPUT);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", PRIMARY);
        UIManager.put("ComboBox.selectionForeground", TEXT_PRIMARY);
        UIManager.put("List.background", BG_DARK);
        UIManager.put("List.foreground", TEXT_PRIMARY);
        UIManager.put("List.selectionBackground", new Color(51, 65, 85));
        UIManager.put("List.selectionForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.background", BG_PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.messageFont", BODY_FONT);
        UIManager.put("OptionPane.buttonFont", BUTTON_FONT);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("Viewport.background", BG_DARK);
        UIManager.put("ScrollBar.background", BG_PANEL);
        UIManager.put("ScrollBar.thumb", BORDER_COLOR);
        UIManager.put("ScrollBar.thumbDarkShadow", BORDER_COLOR);
        UIManager.put("ScrollBar.thumbHighlight", BG_CARD);
        UIManager.put("ScrollBar.thumbShadow", BORDER_COLOR);
        UIManager.put("ScrollBar.track", BG_PANEL);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("TextField.font", INPUT_FONT);
        UIManager.put("PasswordField.font", INPUT_FONT);
        UIManager.put("ComboBox.font", INPUT_FONT);
    }

    // ─── Styled Components ───────────────────────────────────────

    public static JButton createButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text) {
            private boolean hovering = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovering ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(BUTTON_FONT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 28, 36));
        return btn;
    }

    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY, PRIMARY_HOVER);
    }

    public static JButton createAccentButton(String text) {
        return createButton(text, ACCENT, ACCENT_HOVER);
    }

    public static JButton createDangerButton(String text) {
        return createButton(text, DANGER, DANGER_HOVER);
    }

    public static JTextField createTextField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(INPUT_FONT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    public static JPasswordField createPasswordField(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(INPUT_FONT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    public static JTextField createSearchField(String placeholder) {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(TEXT_MUTED);
                    g2.setFont(SEARCH_FONT);
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(SEARCH_FONT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        return f;
    }

    public static JPanel createHeaderPanel(String title, String subtitle) {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(79, 70, 229), getWidth(), getHeight(),
                        new Color(124, 58, 237));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        JLabel t = new JLabel(title);
        t.setFont(TITLE_FONT);
        t.setForeground(Color.WHITE);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(t);
        if (subtitle != null) {
            header.add(Box.createVerticalStrut(4));
            JLabel s = new JLabel(subtitle);
            s.setFont(SUBTITLE_FONT);
            s.setForeground(new Color(199, 210, 254));
            s.setAlignmentX(Component.LEFT_ALIGNMENT);
            header.add(s);
        }
        return header;
    }

    public static JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_SECONDARY);
        l.setFont(BODY_FONT);
        return l;
    }

    // ─── Job List Cell Renderer ──────────────────────────────────

    public static class JobCellRenderer extends JPanel implements ListCellRenderer<JobPosting> {
        private JLabel titleLabel = new JLabel();
        private JLabel companyLabel = new JLabel();
        private JLabel descLabel = new JLabel();
        private JLabel idLabel = new JLabel();

        public JobCellRenderer() {
            setLayout(new BorderLayout(8, 0));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)));
            setOpaque(true);

            titleLabel.setFont(LIST_TITLE_FONT);
            companyLabel.setFont(LIST_SUB_FONT);
            descLabel.setFont(LIST_SUB_FONT);
            idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            titleLabel.setAlignmentX(LEFT_ALIGNMENT);
            companyLabel.setAlignmentX(LEFT_ALIGNMENT);
            descLabel.setAlignmentX(LEFT_ALIGNMENT);
            textPanel.add(titleLabel);
            textPanel.add(Box.createVerticalStrut(3));
            textPanel.add(companyLabel);
            textPanel.add(Box.createVerticalStrut(2));
            textPanel.add(descLabel);

            idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            idLabel.setVerticalAlignment(SwingConstants.TOP);

            add(textPanel, BorderLayout.CENTER);
            add(idLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends JobPosting> list, JobPosting job, int index,
                boolean sel, boolean focus) {
            Color bg = sel ? BG_CARD : BG_DARK;
            setBackground(bg);
            titleLabel.setForeground(sel ? Color.WHITE : TEXT_PRIMARY);
            companyLabel.setForeground(sel ? new Color(199, 210, 254) : ACCENT);
            descLabel.setForeground(sel ? new Color(199, 210, 254) : TEXT_SECONDARY);
            idLabel.setForeground(TEXT_MUTED);

            if (job != null) {
                titleLabel.setText(job.getTitle());
                companyLabel.setText(job.getCompanyName());
                descLabel.setText(job.getDescription());
                idLabel.setText(job.getJobId());
            }
            return this;
        }
    }
}
