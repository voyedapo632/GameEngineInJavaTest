package ultra3d.editor.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import ultra3d.framework.U3DField;
import ultra3d.util.U3DVector3f;

public class U3DVec3Property extends U3DPropertyContainer {
    protected U3DNumberInputField xValue;
    protected U3DNumberInputField yValue;
    protected U3DNumberInputField zValue;
    protected U3DField<?> field;

    public U3DVec3Property(U3DField<?> field, String name) {
        super(name);
        this.field = field;
        xValue = new U3DNumberInputField(((U3DVector3f)field.getValue()).x, U3DColors.simpleRed);
        yValue = new U3DNumberInputField(((U3DVector3f)field.getValue()).y, U3DColors.simpleGreen);
        zValue = new U3DNumberInputField(((U3DVector3f)field.getValue()).z, U3DColors.skyBlue);

        FocusAdapter fa = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateProperty();
            }
        };

        xValue.addFocusListener(fa);
        yValue.addFocusListener(fa);
        zValue.addFocusListener(fa);

        column2.add(xValue);
        column2.add(yValue);
        column2.add(zValue);
    }

    public void updateProperty() {
        ((U3DVector3f)field.getValue()).x = xValue.getValue();
        ((U3DVector3f)field.getValue()).y = yValue.getValue();
        ((U3DVector3f)field.getValue()).z = zValue.getValue();
    }
}