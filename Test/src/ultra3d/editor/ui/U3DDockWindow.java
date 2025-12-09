package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class U3DDockWindow extends U3DDockManeger {
    protected String name;
    protected JLabel icon;
    protected U3DDockManeger parentPanel;
    protected String dockPosition;
    protected ArrayList<U3DDockTabButton> centerDockWindows;
    protected JFrame mainFrame;
    protected JPanel bodyPanel;
    protected JPanel tabBar;
    protected JPanel contentContainer;
    protected JPanel contentPanel;
    protected U3DDockTabButton mainTabButton;
    protected String activeTab;
    protected boolean isUndocked;

    public U3DDockWindow(String name, JLabel icon) {
        this.name = name;
        this.icon = icon;
        centerDockWindows = new ArrayList<>();
        activeTab = name;
        isUndocked = false;

        bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(new OutlineBorder(new Insets(3, 3, 3, 3), 
                                              U3DColors.background));

        // Main frame
        mainFrame = new JFrame(name);
        mainFrame.setVisible(false);
        
        // Content Panel
        contentContainer = new JPanel(new BorderLayout());
        contentContainer.setBackground(U3DColors.background);
        bodyPanel.add(contentContainer, BorderLayout.CENTER);

        // Content Panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(U3DColors.forground);
        contentContainer.add(contentPanel, BorderLayout.CENTER);

        // Tab bar
        tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        tabBar.setPreferredSize(new Dimension(20, 28));
        tabBar.setBackground(U3DColors.background);
        bodyPanel.add(tabBar, BorderLayout.NORTH);

        // Main tab button
        centerDockWindows.add(new U3DDockTabButton(this, this));
        validateTabs();

        add(bodyPanel, BorderLayout.CENTER);
    }

    public U3DDockManeger getParentPanel() {
        return parentPanel;
    }

    public void setActiveTab(String tabName) {
        activeTab = tabName;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void undock() {
        mainFrame.add(this);
        mainFrame.setSize(getPreferredSize());
        mainFrame.setVisible(true);
        mainFrame.setAlwaysOnTop(true);
        isUndocked = true;
        parentPanel.updateUI();
        validateTabs();
    }

    public final void validateTabs() {
        if (centerDockWindows.isEmpty()) {
            parentPanel.remove(this);
            mainFrame.setVisible(false);
            parentPanel.updateUI();
        }

        tabBar.removeAll();
        contentContainer.removeAll();

        for (U3DDockTabButton tab : centerDockWindows) {
            if (tab.getTabName().equals(activeTab)) {
                tab.setActive();
                contentContainer.add(tab.dockWindow.getContentPanel(), BorderLayout.CENTER);
                tab.dockWindow.getContentPanel().setVisible(true);
            } else {
                tab.dockWindow.getContentPanel().setVisible(false);
                tab.setDeactive();
            }

            tabBar.add(tab);
        }
        
        mainFrame.validate();
        validate();
        updateUI();
    }
}

