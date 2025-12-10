package ultra3d.editor.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class U3DToolItemSeperator extends JPanel {
    public U3DToolItemSeperator(int height, Color color) {
        setPreferredSize(new Dimension(2, height));
        setBackground(color);
    }
}
