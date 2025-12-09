package ultra3d.editor.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class U3DDockManeger extends JPanel {
    public static final String DOCK_POSITION_LEFT =  BorderLayout.WEST;
    public static final String DOCK_POSITION_RIGHT =  BorderLayout.EAST;
    public static final String DOCK_POSITION_BOTTOM =  BorderLayout.SOUTH;
    public static final String DOCK_POSITION_TOP =  BorderLayout.NORTH;
    public static final String DOCK_POSITION_CENTER =  "CENTER";

    public U3DDockManeger() {
        super(new BorderLayout());
    }

    public void addChildDockWindow(U3DDockWindow childDockPanel, String dockPosition) {
        childDockPanel.parentPanel = this;
        childDockPanel.dockPosition = dockPosition;

        if (dockPosition.equals(U3DDockManeger.DOCK_POSITION_CENTER)) {
            if (this instanceof U3DDockWindow w) {
                w.centerDockWindows.add(new U3DDockTabButton(childDockPanel, w));
                w.validateTabs();
            } else {
                add(childDockPanel, BorderLayout.CENTER);
            }
        } else {
            add(childDockPanel, dockPosition);
        }
    }
}
