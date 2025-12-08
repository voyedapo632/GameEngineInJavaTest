package engine4j.editor3d;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;

public class E4JInputField extends JTextField {
    protected JButton heightlight;
    protected Color heightlightColor;

    public E4JInputField(Color heightlightColor) {
        super();
        this.heightlightColor = heightlightColor;
        init();
    }

    public E4JInputField(String text, Color heightlightColor) {
        super(text);
        this.heightlightColor = heightlightColor;
        init();
    }

    public void init() {
        setBackground(E4JColors.black);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(E4JColors.forground2, 1),
            BorderFactory.createMatteBorder(0, 2, 0, 0, heightlightColor)
        ));
        setCaretColor(E4JColors.text);
        setLayout(new BorderLayout());
        setForeground(E4JColors.text);
    }
}
