package ultra3d.editor.ui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;

public class U3DNumberInputField extends U3DInputField {
    protected Float numberSource;

    public U3DNumberInputField(Float numberSource, Color heightlightColor) {
        super(heightlightColor);
        this.numberSource = numberSource;
        setText(numberSource.toString());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(U3DColors.forground2, 1),
            BorderFactory.createMatteBorder(0, 2, 0, 0, heightlightColor)
        ));

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = getText();
                int l = value.length();
                String symbols = "!@#$%^&*()-_~`+=[]{}\\|\"';:<>,/? ";
                char keyToLowerCase = (ke.getKeyChar() + "").toLowerCase().charAt(0);

                if (!symbols.contains(ke.getKeyChar() + "") && !(keyToLowerCase >= 'a' && keyToLowerCase <= 'z')) {
                    setEditable(true);
                } else {
                    setEditable(false);
                }
            }
        });
    }

    Float getValue() {
        return Float.parseFloat(getText());
    }
}