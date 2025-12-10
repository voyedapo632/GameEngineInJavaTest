package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

class RoundedButtonBorder implements Border {
    private int radius;
    private Color color;

    RoundedButtonBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }


    public Insets getBorderInsets(Component c) {
        return new Insets(2, 5, 2, 5);
    }


    public boolean isBorderOpaque() {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRoundRect(x, y, width, height, radius, radius);
    }
}

public class U3DButton extends JButton {
    protected JLabel textLabel;

    public U3DButton(String text) {
        super(text);
        
        textLabel = new JLabel(text);
        textLabel.setBackground(U3DColors.skyBlue);
        textLabel.setForeground(U3DColors.white);
        
        setBackground(null);
        setLayout(new BorderLayout());
        setBorder(new RoundedButtonBorder(3, U3DColors.skyBlue));
        add(textLabel, BorderLayout.CENTER);
    }
}