/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package ultra3d.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import sr4j.SR4JSurfaceTarget;
import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DDockWindow;
import ultra3d.editor.ui.U3DToolItem;
import ultra3d.editor.ui.U3DToolSelectionRow;
import ultra3d.editor.ui.U3DToolSelectionRowActionEvent;
import ultra3d.editor.ui.U3DToolSelectionRowActionListener;
import ultra3d.framework.EditorCameraSystem;
import ultra3d.framework.StaticMeshComponentSystem;
import ultra3d.framework.TransformGizmoSystem;
import ultra3d.framework.U3DEventInterface;
import ultra3d.framework.U3DScene;

public class SceneDockWindow extends U3DDockWindow implements U3DEventInterface {
    private final U3DEditor editor;
    public final String sceneName;
    private final String scenePath;
    private final U3DScene localScene;
    private final JPanel toolBar;
    protected U3DToolSelectionRow transformGizmoSelect;

    public SceneDockWindow(U3DEditor editor, String sceneName) {
        super(sceneName, new JLabel("🎬"));
        this.editor = editor;
        this.sceneName = sceneName;

        // Toolbar
        toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        toolBar.setPreferredSize(new Dimension(32, 32));
        toolBar.setBackground(U3DColors.forground);
        getContentPanel().add(toolBar, BorderLayout.NORTH);

        transformGizmoSelect = new U3DToolSelectionRow(U3DColors.toolItemBlue);
        transformGizmoSelect.addToolItem(new U3DToolItem(new JLabel("➤"), "Select", U3DScene.SELECTION_MODE_POINTER));
        transformGizmoSelect.addToolItem(new U3DToolItem(new JLabel("✥"), "Translate", U3DScene.SELECTION_MODE_TRANSLATION));
        transformGizmoSelect.addToolItem(new U3DToolItem(new JLabel("📐"), "Scale", U3DScene.SELECTION_MODE_SCALE));
        transformGizmoSelect.addToolItem(new U3DToolItem(new JLabel("🔄"), "Rotate", U3DScene.SELECTION_MODE_ROTATION));
        toolBar.add(transformGizmoSelect);

        transformGizmoSelect.addToolSelectionRowActionListeners(new U3DToolSelectionRowActionListener() {
            @Override
            public void actionPerformed(U3DToolSelectionRowActionEvent e) {
                localScene.selectionMode = e.getSelectedToolItem().getToolItemId();
                localScene.getViewport().requestFocus();
            }
        });

        // Load scene
        scenePath = editor.projectPath + "\\scenes\\" + sceneName;
        editor.sceneManager.loadScene(scenePath);
        localScene = editor.sceneManager.getScene(scenePath);

        // Load defualt systems
        localScene.addComponentSystem(new EditorCameraSystem(localScene, editor.sceneManager, editor));
        localScene.addComponentSystem(new StaticMeshComponentSystem(localScene, editor.sceneManager));
        localScene.addComponentSystem(new TransformGizmoSystem(localScene, editor.sceneManager));
        
        // Display viewport
        getContentPanel().add(localScene.getViewport(), BorderLayout.CENTER);
        validate();

        // Events
        getContentPanel().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent comp) {
                if (localScene.getGraphicsEngine() != null) {
                    localScene.getGraphicsEngine().swapChain.setSurfaceTarget(new SR4JSurfaceTarget(localScene.getViewport()));
                    localScene.getGraphicsEngine().validateEngine();
                }
            }
        });

        getContentPanel().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                editor.outliner.reloadFromScene(localScene);
                
                // Update outliner
                editor.outliner.validateSelectedNodes(localScene);
                editor.outliner.treeView.validate();
                
                // Update details panel
                editor.details.validateDetailsPanel(localScene);
                editor.details.propertiesPanel.validate();
                editor.details.updateUI();
                editor.details.validate();

                localScene.getViewport().requestFocus();
            }
        });

        localScene.getViewport().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_1 -> {
                        localScene.selectionMode = U3DScene.SELECTION_MODE_POINTER;
                        updateSelection();
                    }
                    case KeyEvent.VK_2 -> {
                        localScene.selectionMode = U3DScene.SELECTION_MODE_TRANSLATION;
                        updateSelection();
                    }
                    case KeyEvent.VK_3 -> {
                        localScene.selectionMode = U3DScene.SELECTION_MODE_SCALE;
                        updateSelection();
                    }
                    case KeyEvent.VK_4 -> {
                        localScene.selectionMode = U3DScene.SELECTION_MODE_ROTATION;
                        updateSelection();
                    }
                    default -> {
                    }
                }
            }

            private void updateSelection() {
                transformGizmoSelect.setSelection(transformGizmoSelect.getToolItemById(localScene.selectionMode));
            }
        });
    }

    @Override
    public void onStart() {
        localScene.onStart();
        localScene.getGraphicsEngine().setResolution(1240, 720);
        
        if (getContentPanel().isVisible()) {
            localScene.getGraphicsEngine().swapChain.setSurfaceTarget(new SR4JSurfaceTarget(localScene.getViewport()));
            localScene.getViewport().requestFocus();

            // Update outliner
            editor.outliner.validateSelectedNodes(localScene);
            editor.outliner.treeView.validate();
            
            // Update details panel
            editor.details.validateDetailsPanel(localScene);
            editor.details.propertiesPanel.validate();
            editor.details.updateUI();
            editor.details.validate();
        }

        editor.console.writeLine("Scene \"" + sceneName + "\" has started", U3DColors.text);
    }

    @Override
    public void onValidated() {
        if (getContentPanel().isVisible()) {
            localScene.onValidated();
        }
    }

    String[] selectionUpdate = {
        U3DScene.MSG_HAS_SELECTED,
        U3DScene.MSG_HAS_DESELECTED,
        U3DScene.MSG_HAS_DESELECTED_ALL,
        U3DScene.MSG_COMPONENT_CHANGED
    };

    String[] addingUpdate = {
        U3DScene.MSG_ENTITY_ADDED,
        U3DScene.MSG_ENTITY_COPIED,
        U3DScene.MSG_ENTITYS_PASTED,
        U3DScene.MSG_ENTITY_DELETED
    };

    @Override
    public void onUpdateBegin(float deltaTime) {
        if (getContentPanel().isVisible()) {
            localScene.onUpdateBegin(0);

            if (localScene.messageBox.hasValidMessage(selectionUpdate) || localScene.messageBox.hasValidMessage(addingUpdate)) {
                if (localScene.messageBox.hasValidMessage(addingUpdate)) {
                    editor.outliner.reloadFromScene(localScene);
                }

                // Update outliner
                editor.outliner.validateSelectedNodes(localScene);
                editor.outliner.treeView.validate();
                
                // Update details panel
                editor.details.validateDetailsPanel(localScene);
                editor.details.propertiesPanel.validate();
                editor.details.updateUI();
                editor.details.validate();
                
                // Clear messages
                localScene.messageBox.removeValidMessage(selectionUpdate);
                localScene.messageBox.removeValidMessage(addingUpdate);
            }
        }
    }

    @Override
    public void onUpdateEnd(float deltaTime) {
        if (getContentPanel().isVisible()) {
            localScene.onUpdateEnd(0);
        }
    }

    @Override
    public void onEnd() {
        localScene.onEnd();
    }
}
