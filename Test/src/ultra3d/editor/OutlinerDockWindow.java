package ultra3d.editor;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

import ultra3d.editor.ui.U3DDockWindow;
import ultra3d.editor.ui.U3DTreeNode;
import ultra3d.editor.ui.U3DTreeView;
import ultra3d.framework.U3DEntity;
import ultra3d.framework.U3DScene;

public class OutlinerDockWindow extends U3DDockWindow {
    private U3DEditor editor;
    public U3DTreeView treeView;
    public U3DTreeNode rootTreeNode;
    public U3DScene lastScene;

    public OutlinerDockWindow(U3DEditor editor) {
        super("Outliner", new JLabel("🛠"));
        this.editor = editor;
        
        // Tree view
        treeView = new U3DTreeView();
        getContentPanel().add(treeView, BorderLayout.CENTER);

        rootTreeNode = new U3DTreeNode("Root", "Root", 0, "Folder", treeView);
        treeView.addTreeNode(rootTreeNode);
        rootTreeNode.isExpanded = true;
        
        treeView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
            
                if ((treeView.keys.contains(KeyEvent.VK_CONTROL) && keyCode == KeyEvent.VK_A) || keyCode == KeyEvent.VK_ESCAPE) {
                    updateSceneSelection();
                }
            }
        
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public void reloadFromScene(U3DScene scene) {
        if (scene == null) {
            return;
        }

        lastScene = scene;
        treeView.removeTreeNodes();
        treeView.addTreeNode(rootTreeNode);

        for (U3DEntity entity : scene.getEntities()) {
            String name = "Entity";

            if (entity.hasComponent("Static Mesh")) {
                name = "Static Mesh";
            }

            U3DTreeNode treeNode = new U3DTreeNode(entity.getEntityId(), new JLabel("📦"), entity.getEntityId(), scene.getEntities().indexOf(entity) + 1, name, treeView);
            treeNode.setParentTreeNode(entity.getParentId());
            
            treeNode.mainButton.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    updateSceneSelection();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    updateSceneSelection();
                }
            });
            
            treeView.addTreeNode(treeNode);
        }

        treeView.reloadTreeView();
    }

    public void updateSceneSelection() {
        lastScene.selectedEntities.clear();

        for (U3DTreeNode node : treeView.selectedTreeNods) {
            lastScene.selectedEntities.add(node.getUniqueId());
        }

        // Update details panel
        editor.details.validateDetailsPanel(lastScene);
        editor.details.propertiesPanel.validate();
        editor.details.updateUI();
        editor.details.validate();
    }

    public void validateSelectedNodes(U3DScene scene) {
        treeView.selectedTreeNods.clear();
            
        for (U3DTreeNode node : treeView.getTreeNodes()) {
            if (scene.getSelectedEntities().contains(node.getUniqueId())) {
                treeView.selectedTreeNods.add(node);
            }
        }
        
        treeView.validateTreeNodes();
    }
}
