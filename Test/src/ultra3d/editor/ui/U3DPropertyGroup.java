package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalButtonUI;

public class U3DPropertyGroup extends JPanel {
    protected ArrayList<U3DPropertyContainer> properties;
    protected JToggleButton expandButton;
    protected JLabel nameLabel;
    protected String name;
    protected boolean isExpanded;
    protected JPanel mainPanel;
    protected JPanel childrenPanel;
    protected GridBagConstraints gbc;
    protected U3DPropertyViewPanel parentPropertyViewPanel;

    public U3DPropertyGroup(String name) {
        super(new BorderLayout());
        properties = new ArrayList<>();
        this.name = name;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(U3DColors.forground2);
        mainPanel.setPreferredSize(new Dimension(30, 30));
        mainPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, U3DColors.background));

        // Expand button
        expandButton = new JToggleButton("🞃");
        expandButton.setForeground(U3DColors.text);
        expandButton.setPreferredSize(new Dimension(30, 25));
        expandButton.setBackground(null);
        expandButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        mainPanel.add(expandButton, BorderLayout.WEST);

        expandButton.setUI(new MetalButtonUI() {
            @Override
            protected Color getSelectColor() {
                return null;
            }
        });

        expandButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                doExpand();
            }
        });

        // Label
        nameLabel = new JLabel(name);
        nameLabel.setForeground(U3DColors.text);
        mainPanel.add(nameLabel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Child panel
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        childrenPanel = new JPanel(new GridBagLayout());
        add(childrenPanel, BorderLayout.SOUTH);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameLabel = new JLabel(name);
        this.validateU3DPropertyGroup();
    }

    public U3DPropertyViewPanel getParentPropertyViewPanel() {
        return parentPropertyViewPanel;
    }

    public void setParentPropertyViewPanel(U3DPropertyViewPanel parentPropertyViewPanel) {
        this.parentPropertyViewPanel = parentPropertyViewPanel;
    }

    public ArrayList<U3DPropertyContainer> getProperties() {
        return properties;
    }

    public void doExpand() {
       // expandButton.doClick();

        if (isExpanded) {
            expandButton.setText("🞃");
            isExpanded = false;
        }
        else {
            expandButton.setText("🞁");
            isExpanded = true;
        }

        validateU3DPropertyGroup();
    }

    public void addProperty(U3DPropertyContainer property) {
        properties.add(property);
        childrenPanel.add(property, gbc, properties.indexOf(property));
    }

    public void removeProperty(String propertyName) {
        for (U3DPropertyContainer property : properties) {
            if (property.getName().equals(propertyName)) {
                properties.remove(property);
                childrenPanel.remove(property);
                return;
            }
        }
    }

    @Override public void validate() {
        super.validate();
        childrenPanel.setVisible(isExpanded);
    }

    public void validateU3DPropertyGroup() {
        childrenPanel.setVisible(isExpanded);
        
        if (getParent() != null) {
            getParent().validate();
        }
        
        if (parentPropertyViewPanel != null) {
            parentPropertyViewPanel.validate();
        }
    }
}