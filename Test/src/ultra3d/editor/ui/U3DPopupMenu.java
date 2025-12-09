package ultra3d.editor.ui;

import java.awt.Insets;

import javax.swing.JPopupMenu;

public class U3DPopupMenu extends JPopupMenu {
    U3DMenuItem anItem;
    public U3DPopupMenu() {
        setBorder(new OutlineBorder(new Insets(1, 1, 1, 1), 
                                              U3DColors.darkGray));
        setBackground(U3DColors.forground);
    }
}