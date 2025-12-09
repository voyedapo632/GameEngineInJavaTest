package ultra3d.editor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalButtonUI;

public class U3DCheckBoxField extends JToggleButton {
    public boolean state;

    public U3DCheckBoxField(boolean _state) {
        super();
        this.state = _state;
        setSelected(state);

        setUI(new MetalButtonUI() {
            @Override
            protected Color getSelectColor() {
                return null;
            }
        });

        setPreferredSize(new Dimension(20, 20));
        setBackground(U3DColors.black);
        setBorder(BorderFactory.createLineBorder(U3DColors.forground2, 1));
        setForeground(U3DColors.skyBlue);

        if (state) {
            setText("✔");
        } else {
            setText("");
        }

        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent)
            {
                int _state = itemEvent.getStateChange();

                if (_state == ItemEvent.SELECTED) {
                    state = true;
                    setText("✔");
                }
                else {
                    state = false;
                    setText("");
                }

                validate();
            }
        });
    }
}
