package engine4j.editor.ui;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CustomTab {
    public String name;
    public JPanel panel;
    public JLabel icon;

    public CustomTab(String _name, JLabel _icon, JPanel _panel) {
        name = _name;
        panel = _panel;
        icon = _icon;
    }
}