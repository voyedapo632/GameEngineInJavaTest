
package engine4j.editor.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import engine4j.editor.ui.TabManager;

class OutlineBorder implements Border {
    private Insets insets;
    private Color color;

    public OutlineBorder(Insets insets, Color color) {
        this.color = color;
        this.insets = insets;
    }


    public Insets getBorderInsets(Component c) {
        return insets;
    }


    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}

class RoundedBorder implements Border {
    private int radius;
    private Color color;

    RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }


    public Insets getBorderInsets(Component c) {
        return new Insets(0, radius-3, 4, radius);
    }


    public boolean isBorderOpaque() {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRoundRect(x, y, width, height+radius, radius, radius);
    }
}

class NewBorder implements Border {
    private int radius;
    private Color color;

    NewBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }


    public Insets getBorderInsets(Component c) {
        return new Insets(0, radius-3, 4, radius);
    }


    public boolean isBorderOpaque() {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(EditorStyle.FOCUS_SELECTION);
        g.fillRoundRect(x, y, width, height+radius, radius, radius);
        g.setColor(color);
        g.fillRoundRect(x + 1, y + 1, width - 2, height+radius, radius, radius);
    }
}

public class TabLayout extends TabManager {
    public TabManager mainManager;
    public JPanel mainPanel;
    public JPanel titleBar;
    public JPanel tabView;
    public JButton dockButton;
    public JFrame mainFrame;
    public boolean isDocked;
    public String dockPosition;
    public TabManager parentWindow;
    public Vector<CustomTab> tabs;
    public String activeTab;
    
    public void dockAt(TabManager _parent, String _dockPosition) {
        dockPosition = _dockPosition;
        parentWindow = _parent;
        String type = parentWindow.getClass().getSimpleName();

        System.out.println(type);
        
        //TabLayout parentPanel = (TabLayout)parentWindow;

        if (parentWindow.getClass().getSimpleName().startsWith("TabLayout") &&
            dockPosition.startsWith(BorderLayout.CENTER)) {
            
            for (CustomTab tab : tabs) {
                ((TabLayout)parentWindow).addTab(tab);
            }
        } else {
            parentWindow.add(this, _dockPosition);
        }

        mainFrame.setVisible(false);
        parentWindow.validate();
        parentWindow.updateUI();
        this.validate();
        this.updateUI();
        mainManager.validate();
        mainManager.updateUI();
        
        if (mainManager.getParent() != null) {
            mainManager.getParent().validate();
        }

        isDocked = true;
    }

    public void undock() {
        parentWindow.remove(this);
        parentWindow.validate();
        parentWindow.updateUI();

        mainFrame.add(this);
        mainFrame.setVisible(true);
        mainFrame.validate();

        this.validate();
        this.updateUI();
        mainManager.validate();
        mainManager.updateUI();

        if (mainManager.getParent() != null) {
            mainManager.getParent().validate();
        }

        isDocked = false;
    }

    public TabLayout(Dimension size, TabManager _mainManager) {
        tabs = new Vector<CustomTab>();
        activeTab = "";
        this.setLayout(new BorderLayout());
        this.setPreferredSize(size);
        isDocked = false;
        mainManager = _mainManager;
        //this.setBorder(BorderFactory.createLineBorder(EditorStyle.BACKGROUND, 2));

        // Mian Frame
        mainFrame = new JFrame(activeTab);
        mainFrame.setSize(size);
        mainFrame.setVisible(false);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(size);
        mainPanel.setBorder(new OutlineBorder(new Insets(0, 4, 5, 2), 
                                              EditorStyle.BACKGROUND));

        // Tab view
        tabView = new JPanel();
        tabView.setLayout(new FlowLayout(FlowLayout.LEFT));
        tabView.setPreferredSize(new Dimension(32, 32));
        tabView.setBackground(EditorStyle.BACKGROUND);

        // Dock button
        dockButton = new JButton("🡥");

        dockButton.setPreferredSize(new Dimension(32, 32));
        dockButton.setBackground(null);
        dockButton.setForeground(Color.lightGray);
        dockButton.setBorder(null);
        dockButton.setFocusPainted(false);

        dockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent comp) {
                if (isDocked) {
                    undock();
                } else {
                    setPreferredSize(mainFrame.getSize());
                    dockAt(parentWindow, dockPosition);
                }
                validate();
            }
        });

        // Title bar
        titleBar = new JPanel();
        titleBar.setLayout(new BorderLayout());
        titleBar.setPreferredSize(new Dimension(32, 32));
        titleBar.setBackground(EditorStyle.BACKGROUND);
        titleBar.add(tabView, BorderLayout.CENTER);
        titleBar.add(dockButton, BorderLayout.EAST);

        mainPanel.add(titleBar, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
    }


    public void addTab(CustomTab tab) {
        tabs.add(tab);
       
        for (CustomTab t : tabs) {
            t.panel.setVisible(true);
        }


        validateTabs();
    }


    public void validateTabs() {
        if (tabs.isEmpty()) {
            return;
        }

        if (activeTab.isEmpty()) {
            activeTab = tabs.get(0).name;
        }

        tabView.removeAll();
        
        for (CustomTab tab : tabs) {
            JButton btn = new JButton();

            btn.setPreferredSize(new Dimension(150, 28));
            btn.setLayout(new BorderLayout());
            
            // JLabel icon = new JLabel(UIManager.getIcon("FileView.directoryIcon"));
            JLabel icon = tab.icon;
            icon.setForeground(Color.lightGray);
            
            JButton closeBtn = new JButton("x");
            closeBtn.setPreferredSize(new Dimension(14, 10));
            closeBtn.setBorder(null);
            closeBtn.setBackground(new Color(0, 0, 0, 0));
            closeBtn.setForeground(Color.lightGray);

            closeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent comp) {
                    int index = tabs.indexOf(tab);
                    
                    tabs.remove(tab);
                    tab.panel.setVisible(false);

                    if (activeTab.equals(tab.name)) {
                        if (index < 0) {
                            activeTab = tabs.get(0).name;
                        } else if (index >= tabs.size()) {
                            activeTab = tabs.getLast().name;
                        } else {
                            activeTab = tabs.get(index).name;
                        }
                    }
                    
                    validateTabs();
                }
            });

            JLabel text = new JLabel(tab.name);
            text.setBorder(new EmptyBorder(0, 5, 0, 5));
            text.setMaximumSize(new Dimension(10, 23));
            text.setForeground(Color.lightGray);
           
            btn.add(icon, BorderLayout.WEST);
            btn.add(text, BorderLayout.CENTER);
            btn.add(closeBtn, BorderLayout.EAST);
            btn.setBackground(null);
            btn.setFocusPainted(false);
            
            if (tab.name.startsWith(activeTab) && tab.name.length() == activeTab.length()) {
                btn.setBorder(new RoundedBorder(10, EditorStyle.BACKGROUND_ACCENT));
                mainPanel.add(tab.panel, BorderLayout.CENTER);
                tab.panel.setVisible(true);
                System.out.println(tab.name + " is active!");
            } else {
                System.out.println(tab.name + " is not active!");
                btn.setBorder(new RoundedBorder(10, EditorStyle.BACKGROUND_ACCENT_2));
                tab.panel.setVisible(false);
            }


            tabView.add(btn);


            if (!(tab.name.startsWith(activeTab) && tab.name.length() == activeTab.length())) {
                btn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent comp) {
                        activeTab = tab.name;
                        validateTabs();
                        System.out.println("Tab Clicked! | " + tab.panel.isVisible() + "!");
                    }
                });
            }
        }

        tabView.validate();
        tabView.updateUI();
        mainPanel.validate();
        mainPanel.updateUI();
        this.validate();
        this.updateUI();
    }
}

