package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;

public class U3DInputField extends JTextField {
    protected JButton heightlight;
    protected Color heightlightColor;
    protected String textSource;

    public U3DInputField(Color heightlightColor) {
        super();
        textSource = "";
        this.heightlightColor = heightlightColor;
        init();
    }

    public U3DInputField(String text, Color heightlightColor) {
        super(text);
        textSource = text;
        this.heightlightColor = heightlightColor;
        init();
    }

    public void init() {
        setBackground(U3DColors.black);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(U3DColors.forground2, 1),
            BorderFactory.createMatteBorder(0, 2, 0, 0, heightlightColor)
        ));
        setCaretColor(U3DColors.text);
        setLayout(new BorderLayout());
        setForeground(U3DColors.text);
    }
}