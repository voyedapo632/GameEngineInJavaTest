package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import ultra3d.framework.U3DField;

public class U3DCheckBoxProperty extends U3DPropertyContainer {
    protected U3DField<?> field;
    protected U3DCheckBoxField checkBoxField;

    public U3DCheckBoxProperty(U3DField<?> field, String name) {
        super(name);
        this.field = field;
        checkBoxField = new U3DCheckBoxField((Boolean)field.getValue());

        checkBoxField.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent)
            {
                int _state = itemEvent.getStateChange();

                if (_state == ItemEvent.SELECTED) {
                    field.setValue(true);
                }
                else {
                    field.setValue(false);
                }

                validate();
            }
        });

        column2.setLayout(new BorderLayout());
        column2.add(checkBoxField, BorderLayout.WEST);
    }

    public void updateProperty() {
    }
}
