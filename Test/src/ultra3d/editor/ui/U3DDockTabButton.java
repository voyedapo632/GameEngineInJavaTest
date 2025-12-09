package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

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
        g.fillRoundRect(x, y, width, height+20, radius, radius);
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
        return new Insets(0, 0, 4, 0);
    }


    public boolean isBorderOpaque() {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(U3DColors.background);
        g.fillRoundRect(x, y, width, height+radius, radius, radius);
        g.setColor(color);
        //g.fillRoundRect(x + 1, y + 1, width - 2, height+radius, radius, radius);
    }
}

public class U3DDockTabButton extends JButton {
    public U3DDockWindow parentDockWindow;
    public U3DDockWindow dockWindow;
    public JLabel tabIcon;
    public JLabel tabName;
    public JButton closeButton;

    public U3DDockTabButton(U3DDockWindow dockWindow, U3DDockWindow parentDockWindow) {
        super();
        setLayout(new BorderLayout());
        setBackground(null);
        setPreferredSize(new Dimension(140, 28));
        setDeactive();
        
        this.dockWindow = dockWindow;
        this.parentDockWindow = parentDockWindow;

        // Icon
        tabIcon = dockWindow.icon;
        tabIcon.setPreferredSize(new Dimension(20, 20));
        tabIcon.setHorizontalAlignment(SwingConstants.CENTER);
        tabIcon.setForeground(U3DColors.text);
        add(tabIcon, BorderLayout.WEST);
        
        // Name
        tabName = new JLabel(dockWindow.name);
        tabName.setForeground(U3DColors.text);
        add(tabName, BorderLayout.CENTER);
        
        // Close button
        closeButton = new JButton(" x ");
        closeButton.setBorder(null);
        closeButton.setBackground(new Color(0, 0, 0, 0));
        closeButton.setForeground(U3DColors.text);

        // Events
        U3DDockTabButton self = this;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dockWindow.getContentPanel().requestFocus();

                if (e.isPopupTrigger())
                    doPop(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    doPop(e);
            }

            private void doPop(MouseEvent e) {
                U3DPopupMenu menu = new U3DPopupMenu();
                U3DMenuItem undockWindowMenuItem;

                if (!dockWindow.isUndocked) {
                    undockWindowMenuItem = new U3DMenuItem("Undock Window");

                    undockWindowMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (dockWindow != parentDockWindow) {
                                closeButton.doClick();
                            }
                            
                            dockWindow.undock();
                        }
                    });
                } else {
                    undockWindowMenuItem = new U3DMenuItem("Dock Window");

                    undockWindowMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dockWindow.parentPanel.addChildDockWindow(dockWindow, dockWindow.dockPosition);
                            dockWindow.parentPanel.updateUI();
                            dockWindow.mainFrame.setVisible(false);
                            dockWindow.isUndocked = false;
                            //dockWindow.undock();
                        }
                    });
                }

                U3DMenuItem closeMenuItem = new U3DMenuItem("Close");

                closeMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        closeButton.doClick();
                    }
                });

                menu.add(undockWindowMenuItem);
                menu.add(closeMenuItem);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (!parentDockWindow.activeTab.equals(getTabName())) {
                    parentDockWindow.setActiveTab(getTabName());
                    parentDockWindow.validateTabs();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                int index = parentDockWindow.centerDockWindows.indexOf(self);
                
                self.dockWindow.getContentPanel().setVisible(false);
                parentDockWindow.centerDockWindows.remove(self);

                if (!parentDockWindow.centerDockWindows.isEmpty()) {
                    if (index > 0) {
                        parentDockWindow.setActiveTab(parentDockWindow.centerDockWindows.get(index - 1).getTabName());
                    } else if (index < parentDockWindow.centerDockWindows.size()) {
                        parentDockWindow.setActiveTab(parentDockWindow.centerDockWindows.get(index).getTabName());
                    }
                }

                parentDockWindow.validateTabs();
            }
        });
    }

    public String getTabName() {
        return dockWindow.name;
    }

    public void setActive() {
        setBorder(new RoundedBorder(5, U3DColors.forground));
        add(closeButton, BorderLayout.EAST);
        this.validate();
    }
    
    public void setDeactive() {
        setBorder(new RoundedBorder(5, new Color(0, 0, 0, 0)));
        
        if (closeButton != null) {
            remove(closeButton);
        }

        this.validate();
    }
}
