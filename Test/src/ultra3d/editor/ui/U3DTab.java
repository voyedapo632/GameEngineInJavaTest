package ultra3d.editor.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class U3DTab {
    public String name;
    public JPanel panel;
    public JLabel icon;

    public U3DTab(String _name, JLabel _icon, JPanel _panel) {
        name = _name;
        panel = _panel;
        icon = _icon;
    }
}
