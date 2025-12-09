package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class U3DPropertyViewPanel extends JPanel {
    public JPanel contentPanel;
    public JScrollPane scrollPane;
    public GridBagConstraints gbc;
    public ArrayList<U3DPropertyGroup> groups;
    public JPanel container;

    public U3DPropertyViewPanel() {
        super(new BorderLayout());
        setBackground(U3DColors.background2);
        container = new JPanel(new BorderLayout());
        container.setBackground(U3DColors.background2);
        
        groups = new ArrayList<>();
        
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        
        contentPanel = new JPanel(new GridBagLayout());
        scrollPane = new JScrollPane(container);
        scrollPane.getVerticalScrollBar().setUI(new U3DScrollBarUI());
        scrollPane.setBackground(null);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        container.add(contentPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addPropertyGroup(U3DPropertyGroup group) {
        group.setParentPropertyViewPanel(this);
        groups.add(group);
        contentPanel.add(group, gbc, contentPanel.getComponentCount());
    }

    public void removePropertyGroup(String name) {
        for (U3DPropertyGroup group : groups) {
            if (group.getName().equals(name)) {
                groups.remove(group);
                contentPanel.remove(group);
            }
        }
    }

    public void removePropertyGroups() {
        for (int i = groups.size() - 1; i >= 0; i--) {
            groups.remove(i);
            contentPanel.remove(i);
        }
    }

    @Override
    public void validate() {
        super.validate();

        // contentPanel.removeAll();
// 
        // for (U3DPropertyGroup group : groups) {
        //     contentPanel.add(group, gbc, contentPanel.getComponentCount());
        //     group.validate();
        // }

        contentPanel.validate();
        scrollPane.validate();
    }
}