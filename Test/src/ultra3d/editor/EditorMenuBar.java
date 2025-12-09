package ultra3d.editor;

import java.awt.Insets;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.border.EmptyBorder;

import ultra3d.editor.ui.OutlineBorder;
import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DMenuItem;

public class EditorMenuBar extends JMenuBar {
    private U3DEditor editor;
    
    public EditorMenuBar(U3DEditor editor) {
        this.editor = editor;

        // Mener bar
        setBackground(U3DColors.background);
        setBorder(new EmptyBorder(1, 1, 1, 1));
        setForeground(U3DColors.text);
        
        JMenu file = new JMenu("File");
        file.getPopupMenu().setBorder(new OutlineBorder(new Insets(1, 1, 1, 1), 
                                              U3DColors.darkGray));
        file.getPopupMenu().setBackground(U3DColors.forground);
        file.setForeground(U3DColors.text);
        U3DMenuItem saveMenuItem = new U3DMenuItem("Save");
        U3DMenuItem exitMenuItem = new U3DMenuItem("Exit");
        
        file.add(saveMenuItem);
        file.add(exitMenuItem);
        
        JMenu edit = new JMenu("Edit");
        edit.getPopupMenu().setBorder(new OutlineBorder(new Insets(1, 1, 1, 1), 
                                              U3DColors.darkGray));
        edit.getPopupMenu().setBackground(U3DColors.forground);
        edit.setForeground(U3DColors.text);
        JMenu view = new JMenu("View");
        view.getPopupMenu().setBorder(new OutlineBorder(new Insets(1, 1, 1, 1), 
                                              U3DColors.darkGray));
        view.getPopupMenu().setBackground(U3DColors.forground);
        view.setForeground(U3DColors.text);
        JMenu selection = new JMenu("Selection");
        selection.getPopupMenu().setBorder(new OutlineBorder(new Insets(1, 1, 1, 1), 
                                              U3DColors.darkGray));
        selection.getPopupMenu().setBackground(U3DColors.forground);
        selection.setForeground(U3DColors.text);
        JMenu debug = new JMenu("Debug");
        debug.getPopupMenu().setBorder(new OutlineBorder(new Insets(1, 1, 1, 1), 
                                              U3DColors.darkGray));
        debug.getPopupMenu().setBackground(U3DColors.forground);
        U3DMenuItem playMenuItem = new U3DMenuItem("▶︎ Play");
        U3DMenuItem stopMenuItem = new U3DMenuItem("⏹ Stop");
        
        debug.add(playMenuItem);
        debug.add(stopMenuItem);
        debug.setForeground(U3DColors.text);
        JMenu help = new JMenu("Help");
        help.getPopupMenu().setBorder(new OutlineBorder(new Insets(1, 1, 1, 1), 
                                              U3DColors.darkGray));
        help.getPopupMenu().setBackground(U3DColors.forground);
        help.setForeground(U3DColors.text);

        add(file);
        add(edit);
        add(view);
        add(selection);
        add(debug);
        add(help);
        
        editor.setJMenuBar(this);
    }
}
