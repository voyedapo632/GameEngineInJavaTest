package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalButtonUI;

public class U3DTreeView extends JPanel {
    protected JPanel panelView;
    protected JPanel content;
    protected GridBagConstraints gbc;
    protected JPanel leftColumn;
    protected JPanel centerColumn;
    protected JPanel rightColumn;
    protected JPanel header;
    protected JPanel footer;
    protected JToggleButton visbilityButton;
    public JToggleButton expandButton;
    protected JLabel typeLabel;
    protected JLabel footerText;
    public ArrayList<U3DTreeNode> selectedTreeNods;
    public ArrayList<Integer> keys;
    public JScrollPane scrollPane;
    public ArrayList<U3DTreeNode> childNodes;

    public U3DTreeView() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(350, 450));
        setBackground(U3DColors.background2);
        selectedTreeNods = new ArrayList<>();
        childNodes = new ArrayList<>();
        keys = new ArrayList<>();
        
        panelView = new JPanel(new GridBagLayout());
        panelView.setBackground(U3DColors.background2);

        content = new JPanel(new BorderLayout());
        content.setBackground(U3DColors.background2);

        scrollPane = new JScrollPane(content);
        scrollPane.getVerticalScrollBar().setUI(new U3DScrollBarUI());
        scrollPane.setBackground(null);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(30, 30));
        header.setBackground(U3DColors.forground2);
        add(header, BorderLayout.NORTH);

        footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setPreferredSize(new Dimension(30, 30));
        footer.setBackground(U3DColors.forground2);
        add(footer, BorderLayout.SOUTH);

        footerText = new JLabel("0 Entities");
        footerText.setForeground(U3DColors.text);

        footer.add(footerText);

        JLabel labelText = new JLabel("Label");
        labelText.setForeground(U3DColors.text);
        
        // Left column
        leftColumn = new JPanel();
        leftColumn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftColumn.setBackground(null);
        header.add(leftColumn, BorderLayout.WEST);
        leftColumn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 2, 0, U3DColors.background),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        // Center column
        centerColumn = new JPanel();
        centerColumn.setBackground(null);
        centerColumn.setLayout(new BorderLayout());
        centerColumn.add(labelText);
        header.add(centerColumn, BorderLayout.CENTER);
        centerColumn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 2, 0, U3DColors.background),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        // Right column
        rightColumn = new JPanel();
        rightColumn.setBackground(null);
        rightColumn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.add(rightColumn, BorderLayout.EAST);
        rightColumn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 2, 0, U3DColors.background),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        // Visbility button
        visbilityButton = new JToggleButton("👁");
        visbilityButton.setForeground(U3DColors.text);
        visbilityButton.setPreferredSize(new Dimension(30, 25));
        visbilityButton.setBackground(null);
        visbilityButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftColumn.add(visbilityButton);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keys.contains(KeyEvent.VK_CONTROL) && keyCode == KeyEvent.VK_A) {
                    selectedTreeNods.clear();

                    for (Component c : panelView.getComponents()) {
                        if (c instanceof U3DTreeNode node) {
                            selectedTreeNods.add(node);
                        }
                    }

                    validateTreeNodes();
                } else if (keyCode == KeyEvent.VK_ESCAPE) {
                    selectedTreeNods.clear();
                    validateTreeNodes();
                }

                if (!keys.contains(keyCode)) {
                    keys.add(keyCode);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (int i = 0; i < keys.size(); i++) {
                    Integer key = keys.get(i);

                    if (key.equals(e.getKeyCode())) {
                        keys.remove(i);
                    }
                }
            }
        });

        visbilityButton.setUI(new MetalButtonUI() {
            @Override
            protected Color getSelectColor() {
                return null;
            }
        });

        visbilityButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                int state = itemEvent.getStateChange();

                if (state == ItemEvent.SELECTED) {
                    for (U3DTreeNode node : childNodes) {
                        visbilityButton.setText("⌣");
                        node.treeNodeVisible = false;
                        node.validateU3DTreeNode();
                    }
                }
                else {
                    for (U3DTreeNode node : childNodes) {
                        visbilityButton.setText("👁");
                        node.treeNodeVisible = true;
                        node.validateU3DTreeNode();
                    }
                }

                validate();
                requestFocus();
            }
        });

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
            public void itemStateChanged(ItemEvent itemEvent)
            {
                int state = itemEvent.getStateChange();

                if (state == ItemEvent.SELECTED) {
                    expandButton.setText("🞁");

                    for (U3DTreeNode node : childNodes) {
                        node.isExpanded = true;
                    }
                }
                else {
                    expandButton.setText("🞃");

                    for (U3DTreeNode node : childNodes) {
                        node.isExpanded = false;
                    }
                }

                reloadTreeView();
                requestFocus();
            }
        });

        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Test!");
            }
        });

        // Type label
        typeLabel = new JLabel("Type");
        typeLabel.setPreferredSize(new Dimension(90, 25));
        typeLabel.setForeground(U3DColors.text);
        rightColumn.add(typeLabel);

        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        content.add(panelView, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void validateFooterText() {
        footerText.setText(childNodes.size() + " entities");

        if (!selectedTreeNods.isEmpty()) {
            footerText.setText(footerText.getText() + " (" + selectedTreeNods.size() + " selected)");
        }
    }

    public void addTreeNode(U3DTreeNode node) {
        childNodes.add(node);
        validateFooterText();
    }

    public U3DTreeNode getTreeNode(String uniqueId) {
        for (Component c : panelView.getComponents()) {
            if (c instanceof U3DTreeNode node) {
                if (node.getUniqueId().equals(uniqueId)) {
                    return node;
                }
            }
        }

        return null;
    }

    public ArrayList<U3DTreeNode> getTreeNodes() {
        ArrayList<U3DTreeNode> nodes = new ArrayList<>();

        for (Component c : panelView.getComponents()) {
            if (c instanceof U3DTreeNode node) {
                nodes.add(node);
            }
        }

        return nodes;
    }

    public void removeTreeNode(String uniqueId) {
        for (Component c : panelView.getComponents()) {
            if (c instanceof U3DTreeNode node) {
                if (node.getUniqueId().equals(uniqueId)) {
                    childNodes.remove(node);
                    break;
                }
            }
        }
    }

    public void removeTreeNodes() {
        childNodes.clear();
    }

    public void validateTreeNodes() {
        for (Component c : panelView.getComponents()) {
            if (c instanceof U3DTreeNode node) {
                node.validateU3DTreeNode();
            }
        }

        validateFooterText();
    }

    public boolean nodeHasChildren(U3DTreeNode parentNode) {
        for (U3DTreeNode node : childNodes) {
            if (node.getUniqueId().equals(parentNode.getUniqueId())) {
                continue;
            }

            if (node.getParentTreeNode().equals(parentNode.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public void loadChildrenOf(U3DTreeNode parentNode) {
        for (U3DTreeNode node : childNodes) {
            if (node.getUniqueId().equals(parentNode.getUniqueId())) {
                continue;
            }

            if (node.getParentTreeNode().equals(parentNode.getUniqueId())) {
                node.setIndentLevel(parentNode.indentLevel + 1);
                panelView.add(node, gbc, panelView.getComponentCount());
            }
 
            if (nodeHasChildren(node)) {
                node.expandButton.setForeground(U3DColors.text);
                loadChildrenOf(node);
            } else {
                node.expandButton.setForeground(new Color(0, 0, 0, 0));
            }
        }
    }

    void loadChildrenOf(int indent, String parent) {
        for (U3DTreeNode node : childNodes) {
            if (node.getUniqueId().equals(parent)) {
                continue;
            }
        
            if (node.getParentTreeNode().equals(parent)) {
                node.setIndex(panelView.getComponentCount());
                node.setIndentLevel(indent);
                panelView.add(node, gbc, panelView.getComponentCount());
        
                if (nodeHasChildren(node)) {
                    node.expandButton.setForeground(U3DColors.text);
                    
                    if (node.isExpanded) {
                        loadChildrenOf(indent + 1, node.getUniqueId());
                    }
                } else {
                    node.expandButton.setForeground(new Color(0, 0, 0, 0));
                }
            }
        }
    }

    public void reloadTreeView() {
        int lastScrollPosition = scrollPane.getVerticalScrollBar().getValue();
        panelView.removeAll();
        loadChildrenOf(0, "");
        scrollPane.getVerticalScrollBar().setValue(lastScrollPosition);
        validate();
    }

    public int getTreeNodeCount() {
        return panelView.getComponentCount();
    }
}