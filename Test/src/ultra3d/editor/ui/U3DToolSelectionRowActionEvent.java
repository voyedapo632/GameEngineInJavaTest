package ultra3d.editor.ui;

public class U3DToolSelectionRowActionEvent {
    private U3DToolItem selectedToolItem;
    
    public U3DToolSelectionRowActionEvent(U3DToolItem selectedToolItem) {
        this.selectedToolItem = selectedToolItem;
    }
    
    public U3DToolItem getSelectedToolItem() {
        return selectedToolItem;
    }

    public void setSelectedToolItem(U3DToolItem selectedToolItem) {
        this.selectedToolItem = selectedToolItem;
    }
}