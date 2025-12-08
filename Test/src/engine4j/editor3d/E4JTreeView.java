package engine4j.editor3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalButtonUI;

public class E4JTreeView extends JPanel {
    protected JPanel panelView;
    protected JPanel content;
    protected GridBagConstraints gbc;
    protected JPanel leftColumn;
    protected JPanel centerColumn;
    protected JPanel rightColumn;
    protected JPanel header;
    protected JPanel footer;
    protected JToggleButton visbilityButton;
    protected JToggleButton expandButton;
    protected JLabel typeLabel;
    protected JLabel footerText;
    
    public E4JTreeView() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(350, 450));
        setBackground(E4JColors.background2);
        
        panelView = new JPanel(new GridBagLayout());
        panelView.setBackground(E4JColors.background2);

        content = new JPanel(new BorderLayout());
        content.setBackground(E4JColors.background2);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(30, 30));
        header.setBackground(E4JColors.forground2);
        add(header, BorderLayout.NORTH);

        footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setPreferredSize(new Dimension(30, 30));
        footer.setBackground(E4JColors.forground2);
        add(footer, BorderLayout.SOUTH);

        footerText = new JLabel("0 Entities");
        footerText.setForeground(E4JColors.text);

        footer.add(footerText);

        JLabel labelText = new JLabel("Label");
        labelText.setForeground(E4JColors.text);
        
        // Left column
        leftColumn = new JPanel();
        leftColumn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftColumn.setBackground(null);
        header.add(leftColumn, BorderLayout.WEST);

        // Center column
        centerColumn = new JPanel();
        centerColumn.setBackground(null);
        centerColumn.setLayout(new BorderLayout());
        centerColumn.add(labelText);
        header.add(centerColumn, BorderLayout.CENTER);

        // Right column
        rightColumn = new JPanel();
        rightColumn.setBackground(null);
        rightColumn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.add(rightColumn, BorderLayout.EAST);

        // Visbility button
        visbilityButton = new JToggleButton("👁");
        visbilityButton.setForeground(E4JColors.text);
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
                int state = itemEvent.getStateChange();

                if (state == ItemEvent.SELECTED) {
                    visbilityButton.setText("⌣");
                }
                else {
                    visbilityButton.setText("👁");
                }

                validate();
            }
        });

        // Expand button
        expandButton = new JToggleButton("🞃");
        expandButton.setForeground(E4JColors.text);
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
                }
                else {
                    expandButton.setText("🞃");
                }
            }
        });

        // Type label
        typeLabel = new JLabel("Type");
        typeLabel.setPreferredSize(new Dimension(90, 25));
        typeLabel.setForeground(E4JColors.text);
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

    public void addTreeNode(E4JTreeNode node) {
        panelView.add(node, gbc, getTreeNodeCount());
        footerText.setText(getTreeNodeCount() + " Entities");
    }

    public E4JTreeNode getTreeNode(String uniqueId) {
        for (Component c : panelView.getComponents()) {
            if (c instanceof E4JTreeNode node) {
                if (node.getUniqueId().equals(uniqueId)) {
                    return node;
                }
            }
        }

        return null;
    }

    public void removeTreeNode(String uniqueId) {
        for (Component c : panelView.getComponents()) {
            if (c instanceof E4JTreeNode node) {
                if (node.getUniqueId().equals(uniqueId)) {
                    panelView.remove(node);
                    break;
                }
            }
        }
    }

    public int getTreeNodeCount() {
        return panelView.getComponentCount();
    }
}