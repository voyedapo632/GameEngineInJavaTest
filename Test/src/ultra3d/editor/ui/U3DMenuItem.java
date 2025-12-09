package ultra3d.editor.ui;

import javax.swing.JMenuItem;

public class U3DMenuItem extends JMenuItem {
    public U3DMenuItem(String text) {
        super(text);
        setBorder(null);
        setBackground(null);
        setForeground(U3DColors.text);
    }
}