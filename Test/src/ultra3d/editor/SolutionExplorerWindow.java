package ultra3d.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JLabel;

import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DDockWindow;
import ultra3d.editor.ui.U3DTreeNode;

public class SolutionExplorerWindow extends U3DDockWindow {
    private U3DEditor editor;
    private U3DContentBrowserTreeView treeView;

    public SolutionExplorerWindow(U3DEditor editor) {
        super("Solution Explorer", new JLabel("📂"));
        this.editor = editor;

        // Tree view
        treeView = new U3DContentBrowserTreeView();
        getContentPanel().add(treeView, BorderLayout.CENTER);

        loadDirectoryAsNode(editor.projectPath, "");
        treeView.reloadTreeView();
        treeView.expandButton.doClick();
    }

    public void loadDirectoryAsNode(String path, String parent) {
        File directory = new File(path);
    
        if (directory.isDirectory()) {
            U3DTreeNode node = new U3DTreeNode(path, directory.getName(), 0, "Folder", treeView);
        
            node.leftColumn.remove(node.visbilityButton);
            node.parent = parent;
            treeView.addTreeNode(node);
        
            for (File d : directory.listFiles()) {
                if (d.isDirectory()) {
                    loadDirectoryAsNode(d.getAbsolutePath(), node.getUniqueId());
                } else {
                    String type = "File";
                    JLabel icon = new JLabel("📄");

                    icon.setForeground(U3DColors.text);

                    if (d.getName().endsWith(".xml")) {
                        icon.setText("🎬");
                        type = "Scene";
                    } else if (d.getName().endsWith(".png")) {
                        icon.setText("🏞️");
                        type = "PNG";
                    } else if (d.getName().endsWith(".java")) {
                        icon.setText("J");
                        type = "Java";
                    } else if (d.getName().endsWith(".txt")) {
                        icon.setText("📄");
                        type = "Text";
                    } else if (d.getName().endsWith(".obj")) {
                        icon.setText("🎲");
                        type = "Object";
                    }

                    U3DTreeNode fileNode = new U3DTreeNode(d.getAbsolutePath(), icon, d.getName(), 0, type, treeView);

                    if (d.getName().endsWith(".java")) {
                        fileNode.icon.setForeground(new Color(255, 60, 60));
                    }

                    fileNode.leftColumn.remove(fileNode.visbilityButton);
                    fileNode.parent = node.getUniqueId();
                    treeView.addTreeNode(fileNode);
                }
            }
        }
    }
}
