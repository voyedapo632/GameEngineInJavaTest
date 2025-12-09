package ultra3d.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;

import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DDockWindow;
import ultra3d.editor.ui.U3DScrollBarUI;
import ultra3d.editor.ui.U3DTreeNode;
import ultra3d.editor.ui.U3DTreeView;

class U3DItemContainer extends JButton {
    protected JLabel icon;
    protected JPanel iconContainer;
    protected JTextArea textLabel;

    public U3DItemContainer(JLabel icon, String text) {
        super();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(80, 130));
        setBackground(U3DColors.forground3);
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, U3DColors.background));

        addChangeListener((ChangeEvent e) -> {
            updateFocusStyle();
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateFocusStyle();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateFocusStyle();
            }
        });

        // Icon
        this.icon = icon;
        this.icon.setHorizontalAlignment(SwingConstants.CENTER);
        this.icon.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
        
        // Icon container
        iconContainer = new JPanel(new BorderLayout());
        iconContainer.setBackground(U3DColors.background2);
        iconContainer.add(this.icon, BorderLayout.CENTER);
        iconContainer.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.cyan));
        add(iconContainer, BorderLayout.CENTER);

        // Text
        textLabel = new JTextArea(text);
        textLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        textLabel.setEditable(false);
        textLabel.setLineWrap(true);
        textLabel.setBackground(null);
        textLabel.setForeground(U3DColors.text);
        textLabel.setPreferredSize(new Dimension(80, 50));
        textLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        
        add(textLabel, BorderLayout.SOUTH);
    }

    protected void updateFocusStyle() {
        if (hasFocus()) {
            setBackground(U3DColors.skyBlue);
        } else if (getModel().isRollover()) {
            setBackground(U3DColors.forground3Hover);
        } else {
            setBackground(U3DColors.forground3);
        }
    }
}

class U3DItemContainerPlain extends U3DItemContainer {
    public U3DItemContainerPlain(JLabel icon, String text) {
        super(icon, text);
        setBorder(null);
        setBackground(null);
        iconContainer.setBorder(null);
        iconContainer.setBackground(null);
    }

    @Override
    protected void updateFocusStyle() {
        if (hasFocus()) {
            setBackground(U3DColors.forground3);
        } else if (getModel().isRollover()) {
            setBackground(U3DColors.forground2);
        } else {
            setBackground(null);
        }
    }
}

class U3DScrollableWrapLayout extends FlowLayout {
	public U3DScrollableWrapLayout() {
		super();
	}

	public U3DScrollableWrapLayout(int align) {
		super(align);
	}

	public U3DScrollableWrapLayout(int align, int hgap, int vgap) {
		super(align, hgap, vgap);
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		return layoutSize(target, true);
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		Dimension minimum = layoutSize(target, false);
		minimum.width -= (getHgap() + 1);
		return minimum;
	}

	private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;
            Container container = target;

            while (container.getSize().width == 0 && container.getParent() != null) {
                container = container.getParent();
            }

            targetWidth = container.getSize().width;

            if (targetWidth == 0)
                targetWidth = Integer.MAX_VALUE;

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
            int maxWidth = targetWidth - horizontalInsetsAndGap;

            //  Fit components into the allowed width
            Dimension dim = new Dimension(0, 0);
            int rowWidth = 0;
            int rowHeight = 0;

            int nmembers = target.getComponentCount();

            for (int i = 0; i < nmembers; i++)
            {
                Component m = target.getComponent(i);

                if (m.isVisible())
                {
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

                    //  Can't add the component to current row. Start a new row.

                    if (rowWidth + d.width > maxWidth)
                    {
                        addRow(dim, rowWidth, rowHeight);
                        rowWidth = 0;
                        rowHeight = 0;
                    }

                    //  Add a horizontal gap for all components after the first

                    if (rowWidth != 0)
                    {
                        rowWidth += hgap;
                    }

                    rowWidth += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                }
            }

            addRow(dim, rowWidth, rowHeight);

            dim.width += horizontalInsetsAndGap;
            dim.height += insets.top + insets.bottom + vgap * 2;

            //	When using a scroll pane or the DecoratedLookAndFeel we need to
            //  make sure the preferred size is less than the size of the
            //  target containter so shrinking the container size works
            //  correctly. Removing the horizontal gap is an easy way to do this.

            Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);

            if (scrollPane != null && target.isValid())
            {
                dim.width -= (hgap + 1);
            }

            return dim;
        }
	}

	private void addRow(Dimension dim, int rowWidth, int rowHeight) {
		dim.width = Math.max(dim.width, rowWidth);

		if (dim.height > 0)
		{
			dim.height += getVgap();
		}

		dim.height += rowHeight;
	}
}

class U3DContentBrowserTreeView extends U3DTreeView {
    public U3DContentBrowserTreeView() {
        leftColumn.remove(visbilityButton);
    }
}

public class ContentBrowserWindow extends U3DDockWindow {
    private U3DEditor editor;
    private JPanel gridPanel;
    private JScrollPane gridScroll;
    private U3DContentBrowserTreeView treeView;
    private JSplitPane splitPane;
    private U3DTreeNode allTreeNode;
    private JPanel header;
    private JLabel headerText;

    public ContentBrowserWindow(U3DEditor editor) {
        super("Content Browser", new JLabel("🖿"));
        this.editor = editor;

        // Header
        header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setPreferredSize(new Dimension(30, 30));
        header.setBackground(U3DColors.forground2);
        getContentPanel().add(header, BorderLayout.NORTH);

        headerText = new JLabel(editor.projectPath);
        headerText.setForeground(U3DColors.text);

        header.add(headerText);

        // Grid panel
        gridPanel = new JPanel(new U3DScrollableWrapLayout(FlowLayout.LEFT, 5, 5));
        gridPanel.setBackground(U3DColors.forground);
        
        // Grid scroll
        gridScroll = new JScrollPane(gridPanel);
        gridScroll.getVerticalScrollBar().setUI(new U3DScrollBarUI());
        gridScroll.getHorizontalScrollBar().setUI(new U3DScrollBarUI());
        gridScroll.setAutoscrolls(false);
        gridScroll.setBackground(null);
        gridScroll.setBorder(null);
        
        // Tree view
        treeView = new U3DContentBrowserTreeView();
        
        // Split pane
        UIManager.put("SplitPaneDivider.border", new LineBorder(U3DColors.background, 10));
        splitPane = new JSplitPane(SwingConstants.VERTICAL, treeView, gridScroll);
        
        splitPane.setDividerLocation(250);
        splitPane.setLastDividerLocation(250);
        splitPane.setDividerSize(4);
        splitPane.setBackground(U3DColors.forground);
        splitPane.setBorder(null);
        getContentPanel().add(splitPane);
        
        // Tree view items
        allTreeNode = new U3DTreeNode("All", "All", 0, "Folder", treeView);
        treeView.selectedTreeNods.add(allTreeNode);
        reloadTreeView();
        loadAll();
    }

    public final void loadAll() {
        gridPanel.removeAll();
        loadDirectoryAsItems(editor.projectPath);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gridScroll.getVerticalScrollBar().setValue(0);
            }
        });
        updateUI();
    }

    public final void reloadTreeView() {
        treeView.childNodes.clear();

        allTreeNode.leftColumn.remove(allTreeNode.visbilityButton);
        allTreeNode.mainButton.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                loadAll();
            }
        });

        treeView.addTreeNode(allTreeNode);
        loadDirectoryAsNode(editor.projectPath,  "");
        treeView.reloadTreeView();
        treeView.expandButton.doClick();
    }

    private void populateGridPanel(String path, boolean doClear) {
        if (doClear) {
            gridPanel.removeAll();
        }

        File directory = new File(path);

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                JLabel icon = new JLabel("📄");

                icon.setForeground(U3DColors.text);

                if (file.getName().endsWith(".xml")) {
                    icon.setText("🎬");
                } else if (file.getName().endsWith(".png")) {
                    icon.setText("🏞️");
                } else if (file.getName().endsWith(".java")) {
                    icon.setText("🇯");
                    icon.setForeground(new Color(255, 60, 60));
                } else if (file.getName().endsWith(".txt")) {
                    icon.setText("🗎");
                }

                U3DItemContainer item = new U3DItemContainer(icon, file.getName());

                item.addActionListener((ActionEvent e) -> {
                    System.out.println(file.getName());
                });
                
                gridPanel.add(item);
            } else {
                U3DItemContainerPlain item = new U3DItemContainerPlain(new JLabel("🖿"), file.getName());

                item.icon.setForeground(new Color(1.0f, 0.8f, 0.25f));

                item.addActionListener((ActionEvent e) -> {
                    populateGridPanel(file.getAbsolutePath(), true);
                    treeView.selectedTreeNods.clear();
                    treeView.selectedTreeNods.add(treeView.getTreeNode(file.getAbsolutePath()));
                    treeView.validateTreeNodes();
                });
                
                gridPanel.add(item);
            }
        }
        
        headerText.setText(path);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gridScroll.getVerticalScrollBar().setValue(0);
            }
        });
        updateUI();
    }

    public void loadDirectoryAsNode(String path, String parent) {
        File directory = new File(path);

        if (directory.isDirectory()) {
            U3DTreeNode node = new U3DTreeNode(path, directory.getName(), 0, "Folder", treeView);

            node.mainButton.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    populateGridPanel(path, true);
                }
            });
            node.leftColumn.remove(node.visbilityButton);
            node.parent = parent;
            treeView.addTreeNode(node);

            for (File d : directory.listFiles()) {
                if (d.isDirectory()) {
                    loadDirectoryAsNode(d.getAbsolutePath(), node.getUniqueId());
                }
            }
        }
    }

    public void loadDirectoryAsItems(String path) {
        File directory = new File(path);
        
        if (directory.isDirectory()) {
            populateGridPanel(path, false);

            for (File d : directory.listFiles()) {
                if (d.isDirectory()) {
                    loadDirectoryAsItems(d.getAbsolutePath());
                }
            }
        }
    }

    // public void loadDirectoryAsNode(String path, String parent) {
    //     File directory = new File(path);
// 
    //     if (directory.isDirectory()) {
    //         U3DTreeNode node = new U3DTreeNode(path, directory.getName(), 0, "Folder", treeView);
// 
    //         node.leftColumn.remove(node.visbilityButton);
    //         node.parent = parent;
    //         treeView.addTreeNode(node);
// 
    //         for (File d : directory.listFiles()) {
    //             if (d.isDirectory()) {
    //                 loadDirectoryAsNode(d.getAbsolutePath(), node.getUniqueId());
    //             } else {
    //                 U3DTreeNode fileNode = new U3DTreeNode(d.getAbsolutePath(), d.getName(), 0, "File", treeView);
// 
    //                 fileNode.leftColumn.remove(fileNode.visbilityButton);
    //                 fileNode.parent = node.getUniqueId();
    //                 treeView.addTreeNode(fileNode);
    //             }
    //         }
    //     }
    // }
}
