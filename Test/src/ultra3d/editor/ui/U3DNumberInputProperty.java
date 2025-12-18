package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import ultra3d.framework.U3DField;

public class U3DNumberInputProperty extends U3DPropertyContainer{
    protected U3DField<?> field;
    protected U3DNumberInputField numberInput;

    public U3DNumberInputProperty(U3DField<?> field, String name) {
        super(name);
        this.field = field;
        numberInput = new U3DNumberInputField((Float)field.getValue(), U3DColors.white);
        numberInput.setPreferredSize(new Dimension(120, 120));
        numberInput.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                updateProperty();
            }
        });

        column2.setLayout(new BorderLayout());
        column2.add(numberInput, BorderLayout.WEST);
    }

    public void updateProperty() {
        field.setValue(numberInput.getValue());
    }
}
