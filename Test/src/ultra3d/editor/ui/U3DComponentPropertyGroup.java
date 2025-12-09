package ultra3d.editor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ultra3d.editor.DetailsDockWindow;
import ultra3d.framework.U3DComponent;
import ultra3d.framework.U3DField;

public class U3DComponentPropertyGroup extends U3DPropertyGroup {
    protected DetailsDockWindow details;
    protected U3DComponent component;

    public U3DComponentPropertyGroup(String name, U3DComponent component, DetailsDockWindow details) {
        super(name);
        this.component = component;
        this.details = details;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    doPop(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    doPop(e);
            }

            private void doPop(MouseEvent e) {
                U3DPopupMenu menu = new U3DPopupMenu();
                U3DMenuItem closeMenuItem = new U3DMenuItem("Add Field");

                closeMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        component.addComponentField(new U3DField<Boolean>("New Field", false));
                        details.validateDetailsPanel(details.lastScene);
                        details.validate();
                        details.updateUI();
                    }
                });

                menu.add(closeMenuItem);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
}