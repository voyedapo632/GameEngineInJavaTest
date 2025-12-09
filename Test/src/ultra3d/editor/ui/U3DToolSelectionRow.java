package ultra3d.editor.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class U3DToolSelectionRow extends JPanel {
    protected Color selectedColor;
    protected U3DToolItem selectedToolItem;
    protected ArrayList<U3DToolSelectionRowActionListener> toolSelectionRowActionListeners;
    
    public U3DToolSelectionRow(Color selectioColor) {
        super(new FlowLayout(FlowLayout.LEFT, 3, 0));
        this.selectedColor = selectioColor;
        selectedToolItem = null;
        toolSelectionRowActionListeners = new ArrayList<>();
        setBackground(null);
    }

    public void addToolSelectionRowActionListeners(U3DToolSelectionRowActionListener e) {
        toolSelectionRowActionListeners.add(e);
    }

    public void clearToolSelectionRowActionListeners() {
        toolSelectionRowActionListeners.clear();
    }

    public void addToolItem(U3DToolItem toolItem) {
        add(toolItem);

        if (selectedToolItem == null) {
            selectedToolItem = toolItem;
            validateSelection();
        }

        toolItem.addActionListener((ActionEvent e) -> {
            selectedToolItem = toolItem;

            for (U3DToolSelectionRowActionListener i : toolSelectionRowActionListeners) {
                i.actionPerformed(new U3DToolSelectionRowActionEvent(toolItem));
            }

            validateSelection();
        });
    }

    public void setSelection(U3DToolItem item) {
        selectedToolItem = item;
        validateSelection();
    }

    public U3DToolItem getToolItemById(int id) {
        for (Component c : getComponents()) {
            if (c instanceof U3DToolItem toolItem) {
                if (toolItem.getToolItemId() == id) {
                    return toolItem;
                }
            }
        }

        return null;
    }

    public void validateSelection() {
        for (Component c : getComponents()) {
            if (c instanceof U3DToolItem toolItem) {
                if (toolItem.equals(selectedToolItem)) {
                    toolItem.icon.setForeground(selectedColor);
                } else {
                    toolItem.icon.setForeground(U3DColors.text);
                }
            }
        }
    }
}
