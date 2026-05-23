import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UITheme.applyGlobalDefaults();
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}