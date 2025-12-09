package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalButtonUI;

public class U3DTreeNode extends JPanel {
    public String uniqueId;
    public String parent;
    public JButton mainButton;
    public int indentLevel;
    public JPanel leftColumn;
    public JPanel centerColumn;
    public JPanel rightColumn;
    public JToggleButton visbilityButton;
    public JToggleButton expandButton;
    protected JLabel typeLabel;
    protected JLabel icon;
    public JLabel name;
    protected JLabel indentSpace;
    protected int index;
    protected Color activeColor;
    protected ArrayList<U3DTreeNode> childNodes;
    protected JPanel childrenPanel;
    public boolean isExpanded;
    protected boolean isSelected;
    protected GridBagConstraints gbc;
    protected String typeText;
    protected U3DTreeView parentTreeView;
    protected boolean treeNodeVisible;

    public void doExpand() {
        expandButton.doClick();
    }

    public ArrayList<U3DTreeNode> getChildNodes() {
        return childNodes;
    }

    public void addChildNode(U3DTreeNode node) {
        childNodes.add(node);
    }

    public void removeChildNode(String uniqueId) {
        for (U3DTreeNode childNode : childNodes) {
            if (childNode.getUniqueId().equals(uniqueId)) {
                childNode.remove(childNode);
                return;
            }
        }
    }

    public void removeChildNodes() {
        childNodes.clear();
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setParentTreeNode(String parent) {
        this.parent = parent;
    }

    public String getParentTreeNode() {
        return parent;
    }

    public U3DTreeNode(String uniqueId, String name, int index, String typeText, U3DTreeView parentTreeView) {
        super();
        parent = "";
        this.parentTreeView = parentTreeView;
        indentLevel = 0;
        isExpanded = false;
        this.typeText = typeText;
        this.index = index;
        this.uniqueId = uniqueId;
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
       
        if (index % 2 == 0) {
            activeColor = U3DColors.background;
        } else {
            activeColor = U3DColors.background3;
        }

        icon = new JLabel("🖿");
        icon.setPreferredSize(new Dimension(30, 25));
        icon.setForeground(U3DColors.text);
        this.name = new JLabel(name);
        this.name.setForeground(U3DColors.text);
        setLayout(new BorderLayout());
        createComponents();
        childNodes = new ArrayList<>();
    }

    public void setIndex(int index) {
        this.index = index;

        if (index % 2 == 0) {
            activeColor = U3DColors.background;
        } else {
            activeColor = U3DColors.background3;
        }
    }
    
    public U3DTreeNode(String uniqueId, JLabel icon, String name, int index, String typeText, U3DTreeView parentTreeView) {
        super();
        parent = "";
        this.parentTreeView = parentTreeView;
        this.uniqueId = uniqueId;
        indentLevel = 0;
        isExpanded = false;
        this.typeText = typeText;
        this.index = index;
        this.icon = icon;
        this.icon.setPreferredSize(new Dimension(30, 25));
        icon.setForeground(U3DColors.text);
        this.name = new JLabel(name);
        this.name.setForeground(U3DColors.text);
        setLayout(new BorderLayout());
        createComponents();
        childNodes = new ArrayList<>();
    }

    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
        validateU3DTreeNode();
    }

    public void doIndent() {
        indentLevel++;
        validateU3DTreeNode();
    }

    public void doUnindent() {
        indentLevel--;
        validateU3DTreeNode();
    }

    public void validateU3DTreeNode() {
        indentSpace.setPreferredSize(new Dimension(15 * indentLevel, 30));

        if (parentTreeView.selectedTreeNods.contains(this)) {
            if (mainButton.hasFocus()) {
                activeColor = U3DColors.skyBlue;
            } else {
                activeColor = U3DColors.selected;
            }
        } else {
            if (index % 2 == 0) {
                activeColor = U3DColors.background;
            } else {
                activeColor = U3DColors.background3;
            }
        }

        if (!treeNodeVisible) {
            visbilityButton.setText("⌣");
        } else {
            visbilityButton.setText("👁");
        }

        if (isExpanded) {
            expandButton.setText("🞁");
        } else {
            expandButton.setText("🞃");
        }

        mainButton.setBackground(activeColor);
        mainButton.validate();
        validate();
    }

    private void createComponents() {
        treeNodeVisible = true;
        indentSpace = new JLabel();
        indentSpace.setPreferredSize(new Dimension(30 * indentLevel, 30));
        mainButton = new JButton();

        childrenPanel = new JPanel(new GridBagLayout());

        mainButton.setLayout(new BorderLayout());
        mainButton.setPreferredSize(new Dimension(25, 25));
        mainButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainButton.setBackground(U3DColors.background);

        mainButton.setUI(new MetalButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.white;
            }
            @Override
            protected Color getFocusColor() {
                return Color.blue;
            }
        });

        U3DTreeNode self = this;

        mainButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!parentTreeView.keys.contains(KeyEvent.VK_SHIFT)) {
                    parentTreeView.selectedTreeNods.clear();
                }

                if (!parentTreeView.selectedTreeNods.contains(self)) {
                    parentTreeView.selectedTreeNods.add(self);
                } else {
                    parentTreeView.selectedTreeNods.remove(self);
                }

                // parentTreeView.validateTreeNodes();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                parentTreeView.validateTreeNodes();
                parentTreeView.requestFocus();
                //validateU3DTreeNode();
            }
        });

        // Left column
        leftColumn = new JPanel();
        leftColumn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftColumn.setBackground(null);
        mainButton.add(leftColumn, BorderLayout.WEST);

        // Center column
        centerColumn = new JPanel();
        centerColumn.setBackground(null);
        centerColumn.setLayout(new BorderLayout());
        centerColumn.add(icon, BorderLayout.WEST);
        centerColumn.add(name, BorderLayout.CENTER);
        mainButton.add(centerColumn, BorderLayout.CENTER);

        // Right column
        rightColumn = new JPanel();
        rightColumn.setBackground(null);
        rightColumn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mainButton.add(rightColumn, BorderLayout.EAST);

        // Visbility button
        visbilityButton = new JToggleButton("👁");
        visbilityButton.setForeground(U3DColors.text);
        visbilityButton.setPreferredSize(new Dimension(30, 25));
        visbilityButton.setBackground(null);
        visbilityButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftColumn.add(visbilityButton);

        visbilityButton.setUI(new MetalButtonUI() {
            @Override
            protected Color getSelectColor() {
                return null;
            }
        });

        visbilityButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                treeNodeVisible = !treeNodeVisible;
                validateU3DTreeNode();
            }
        });

        // Indent space
        leftColumn.add(indentSpace);

        // Expand button
        expandButton = new JToggleButton("🞃");
        expandButton.setForeground(U3DColors.text);
        expandButton.setPreferredSize(new Dimension(30, 25));
        expandButton.setBackground(null);
        expandButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftColumn.add(expandButton);

        expandButton.setUI(new MetalButtonUI() {
            @Override
            protected Color getSelectColor() {
                return null;
            }
        });

        expandButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                isExpanded = !isExpanded;
                parentTreeView.reloadTreeView();
            }
        });

        // Type label
        typeLabel = new JLabel(typeText);
        typeLabel.setPreferredSize(new Dimension(90, 25));
        typeLabel.setForeground(U3DColors.text);
        rightColumn.add(typeLabel);

        // Add main button
        add(mainButton, BorderLayout.CENTER);
        add(childrenPanel, BorderLayout.SOUTH);
        validateU3DTreeNode();
    }
}