package ultra3d.editor.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class OutlineBorder implements Border {
    private Insets insets;
    private Color color;

    public OutlineBorder(Insets insets, Color color) {
        this.color = color;
        this.insets = insets;
    }


    public Insets getBorderInsets(Component c) {
        return insets;
    }


    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}