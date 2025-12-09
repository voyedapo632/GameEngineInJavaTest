package ultra3d.editor.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import ultra3d.framework.U3DField;

public class U3DStringProperty extends U3DPropertyContainer {
    protected U3DInputField value;
    protected U3DField<?> field;

    public U3DStringProperty(U3DField<?> field, String name) {
        super(name);
        this.field = field;

        value = new U3DInputField((String)field.getValue(), U3DColors.text);
        column2.add(value);

        FocusAdapter fa = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateProperty();
            }
        };

        value.addFocusListener(fa);
    }

    public void updateProperty() {
        System.out.println("Hello, World!");
        field.setValue(value.getText());
    }
}
