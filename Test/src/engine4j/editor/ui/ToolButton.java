package engine4j.editor.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class ToolButton extends JButton {
    public ToolButton(String name) {
        super(name);
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        this.setBackground(EditorStyle.BACKGROUND);
        this.setForeground(Color.lightGray);
    }
}
