package engine4j.editor3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalButtonUI;

public class E4JTreeNode extends JPanel {
    protected String uniqueId;
    protected JButton mainButton;
    protected int indentLevel;
    protected JPanel leftColumn;
    protected JPanel centerColumn;
    protected JPanel rightColumn;
    protected JToggleButton visbilityButton;
    protected JToggleButton expandButton;
    protected JLabel typeLabel;
    protected JLabel icon;
    protected JLabel name;
    protected JLabel indentSpace;
    protected int index;
    protected Color activeColor;
    protected ArrayList<E4JTreeNode> childNodes;
    protected JPanel childrenPanel;
    protected boolean isExpanded;
    protected GridBagConstraints gbc;
    protected String typeText;

    public void doExpand() {
        expandButton.doClick();
    }

    public ArrayList<E4JTreeNode> getChildNodes() {
        return childNodes;
    }

    public void addChildNode(E4JTreeNode node) {
        childNodes.add(node);
    }

    public void removeChildNode(String uniqueId) {
        for (E4JTreeNode childNode : childNodes) {
            if (childNode.getUniqueId().equals(uniqueId)) {
                childNode.remove(childNode);
                return;
            }
        }
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public E4JTreeNode(String uniqueId, String name, int index, String typeText) {
        super();
        indentLevel = 0;
        isExpanded = false;
        this.typeText = typeText;
        this.index = index;
        this.uniqueId = uniqueId;
       
        if (index % 2 == 0) {
            activeColor = E4JColors.background;
        } else {
            activeColor = E4JColors.background3;
        }

        icon = new JLabel("🖿");
        icon.setPreferredSize(new Dimension(30, 25));
        icon.setForeground(E4JColors.text);
        this.name = new JLabel(name);
        this.name.setForeground(E4JColors.text);
        setLayout(new BorderLayout());
        createComponents();
        childNodes = new ArrayList<>();
    }
    
    
    public E4JTreeNode(String uniqueId, JLabel icon, String name, int index, String typeText) {
        super();
        this.uniqueId = uniqueId;
        indentLevel = 0;
        isExpanded = false;
        this.typeText = typeText;
        this.index = index;
        this.icon = icon;
        this.icon.setPreferredSize(new Dimension(30, 25));
        icon.setForeground(E4JColors.text);
        this.name = new JLabel(name);
        this.name.setForeground(E4JColors.text);
        setLayout(new BorderLayout());
        createComponents();
        childNodes = new ArrayList<>();
    }


    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
        validateE4JTreeNode();
    }


    public void doIndent() {
        indentLevel++;
        validateE4JTreeNode();
    }


    public void doUnindent() {
        indentLevel--;
        validateE4JTreeNode();
    }

    public void validateE4JTreeNode() {
        indentSpace.setPreferredSize(new Dimension(15 * indentLevel, 30));

        if (mainButton.hasFocus()) {
            activeColor = E4JColors.selected;
        } else {
            if (index % 2 == 0) {
                activeColor = E4JColors.background;
            } else {
                activeColor = E4JColors.background3;
            }
        }

        mainButton.setBackground(activeColor);
        mainButton.validate();

        if (isExpanded) {
            for (E4JTreeNode node : childNodes) {
                node.setIndentLevel(indentLevel + 1);
                childrenPanel.add(node, gbc, childNodes.indexOf(node));
            }
        } else {
            childrenPanel.removeAll();
        }

        validate();
    }

    private void createComponents() {
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        indentSpace = new JLabel();
        indentSpace.setPreferredSize(new Dimension(30 * indentLevel, 30));
        mainButton = new JButton();

        childrenPanel = new JPanel(new GridBagLayout());

        mainButton.setLayout(new BorderLayout());
        mainButton.setPreferredSize(new Dimension(25, 25));
        mainButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainButton.setBackground(E4JColors.background);


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


        mainButton.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                validateE4JTreeNode();
            }
           
            @Override
            public void focusLost(FocusEvent e) {
                validateE4JTreeNode();
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
            public void itemStateChanged(ItemEvent itemEvent)
            {
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


        // Indent space
        leftColumn.add(indentSpace);


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
                    isExpanded = true;
                }
                else {
                    expandButton.setText("🞃");
                    isExpanded = false;
                }

                validateE4JTreeNode();
            }
        });

        // Type label
        typeLabel = new JLabel(typeText);
        typeLabel.setPreferredSize(new Dimension(90, 25));
        typeLabel.setForeground(E4JColors.text);
        rightColumn.add(typeLabel);

        // Add main button
        add(mainButton, BorderLayout.CENTER);
        add(childrenPanel, BorderLayout.SOUTH);
        validateE4JTreeNode();
    }
}