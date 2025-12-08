package engine4j.editor3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalButtonUI;

import engine4j.core.E4JGameObject;
import engine4j.core.E4JScene;
import engine4j.core.E4JSceneManager;
import engine4j.editor.ui.EditorStyle;
import engine4j.util.SafeList;

public class Main {
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Editor");

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(new Dimension(1200, 700));
       
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(350, 450));
        panel.setBackground(E4JColors.background2);
        
        E4JTreeView treeView = new E4JTreeView();

        E4JSceneManager sceneManager = new E4JSceneManager();
        sceneManager.loadScene("Test\\src\\engine4j\\core\\levelData.xml");

        E4JScene scene1 = sceneManager.getScene("Scene 1");
        
        for (int i = 0; i < scene1.getGameObjects().getSize(); i++) {
            E4JGameObject gb = scene1.getGameObjects().get(i);

            if (gb != null) {
                E4JTreeNode treeNode1 = new E4JTreeNode(gb.getId(), gb.getName(), i, gb.getType());
                treeView.addTreeNode(treeNode1);
            }
        }

        // for (int i = 0; i < 10; i++) {
        //     E4JTreeNode treeNode1 = new E4JTreeNode("Game Object (" + i + ")", "Node", i, "Folder");
        //     treeView.addTreeNode(treeNode1);
// 
        //     for (int j = 0; j < 3; j++) {
        //         E4JTreeNode childNode = new E4JTreeNode("Child Game Object (" + j + ")", new JLabel("📄"),"Child Node", j, "Entity");
        //         treeNode1.addChildNode(childNode);
        //     }
        // }
       
        panel.add(treeView, BorderLayout.CENTER);
        mainFrame.add(treeView, BorderLayout.WEST);
        
        JPanel properties = new JPanel(new BorderLayout());
        properties.setPreferredSize(new Dimension(350, 450));
        properties.setBackground(E4JColors.background2);

        class E4JNumberInputField extends E4JInputField {
            public E4JNumberInputField(Color heightlightColor) {
                super(heightlightColor);
                setText("0.0");

                addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent ke) {
                        String value = getText();
                        int l = value.length();
                        String symbols = "!@#$%^&*()-_~`+=[]{}\\|\"';:<>,/? ";
                        char keyToLowerCase = (ke.getKeyChar() + "").toLowerCase().charAt(0);

                        if (!symbols.contains(ke.getKeyChar() + "") && !(keyToLowerCase >= 'a' && keyToLowerCase <= 'z')) {
                            setEditable(true);
                        } else {
                            setEditable(false);
                        }
                    }
                });
            }
        }

        class E4JPropertyContainer extends JPanel {
            protected JPanel column1;
            protected JPanel column2;
            protected JLabel nameLabel;
            protected String name;
            protected int parentWidth;

            public void setParentWidth(int width) {
                parentWidth = width;
            }

            public int getParentWidth() {
                return parentWidth;
            }

            public E4JPropertyContainer(String name) {
                super(new BorderLayout());
                this.name = name;
                parentWidth = 300;
                setBackground(E4JColors.forground);
                setPreferredSize(new Dimension(30, 30));

                column1 = new JPanel(new GridLayout(1, 3, 2, 2));
                column1.setPreferredSize(new Dimension(150, 30));
                column1.setBackground(null);
                column1.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 2, 2, 0, E4JColors.background),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));

                nameLabel = new JLabel(name);
                nameLabel.setForeground(E4JColors.text);
                column1.add(nameLabel);

                add(column1, BorderLayout.WEST);
                
                column2 = new JPanel(new GridLayout(1, 3, 2, 2));
                column2.setBackground(null);
                column2.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 2, 2, 0, E4JColors.background),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
                add(column2, BorderLayout.CENTER);

                addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent componentEvent) {
                        if (getParent() != null) {
                            column1.setPreferredSize(new Dimension((int)(parentWidth / 2.5), 30));
                        }
                    }
                });
            }

            @Override 
            public void validate() {
                if (getParent() != null) {
                    column1.setPreferredSize(new Dimension((int)(parentWidth / 2.5), 30));
                }
                super.validate();
            }
        }

        class E4JVec3Property extends E4JPropertyContainer {
            protected E4JNumberInputField xValue;
            protected E4JNumberInputField yValue;
            protected E4JNumberInputField zValue;

            public E4JVec3Property(String name) {
                super(name);
                xValue = new E4JNumberInputField(Color.red);
                yValue = new E4JNumberInputField(Color.green);
                zValue = new E4JNumberInputField(Color.blue);

                column2.add(xValue);
                column2.add(yValue);
                column2.add(zValue);
            }
        }

        class E4JStringProperty extends E4JPropertyContainer {
            protected E4JInputField value;

            public E4JStringProperty(String name) {
                super(name);

                value = new E4JInputField(E4JColors.text);
                column2.add(value);
            }
        }

        class E4JPropertyGroup extends JPanel {
            protected ArrayList<E4JPropertyContainer> properties;
            protected JToggleButton expandButton;
            protected JLabel nameLabel;
            protected String name;
            protected boolean isExpanded;
            protected JPanel mainPanel;
            protected JPanel childrenPanel;
            protected GridBagConstraints gbc;

            public E4JPropertyGroup(String name) {
                super(new BorderLayout());
                properties = new ArrayList<>();
                this.name = name;

                mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBackground(E4JColors.forground2);
                mainPanel.setPreferredSize(new Dimension(30, 30));

                // Expand button
                expandButton = new JToggleButton("🞃");
                expandButton.setForeground(E4JColors.text);
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

                        validateE4JPropertyGroup();
                    }
                });

                // Label
                nameLabel = new JLabel(name);
                nameLabel.setForeground(E4JColors.text);
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
                this.validateE4JPropertyGroup();
            }

            public ArrayList<E4JPropertyContainer> getProperties() {
                return properties;
            }

            public void doExpand() {
                expandButton.doClick();
            }

            public void addProperty(E4JPropertyContainer property) {
                properties.add(property);
            }

            public void removeProperty(String propertyName) {
                for (E4JPropertyContainer property : properties) {
                    if (property.getName().equals(propertyName)) {
                        properties.remove(property);
                        return;
                    }
                }
            }

            public void validateE4JPropertyGroup() {
                if (isExpanded) {
                    for (E4JPropertyContainer property : properties) {
                        childrenPanel.add(property, gbc, properties.indexOf(property));
                        property.setParentWidth(getWidth());
                        property.validate();
                    }
                } else {
                    childrenPanel.removeAll();
                }

                validate();
            }
        }

        var test = new E4JPropertyGroup("Transform");
        test.addProperty(new E4JVec3Property("Translation"));
        test.addProperty(new E4JVec3Property("Scale"));
        test.addProperty(new E4JVec3Property("Rotation"));
        properties.add(test, BorderLayout.NORTH);
        test.validate();
        
        mainFrame.add(properties, BorderLayout.EAST);
        
        // Show window
        test.doExpand();
        mainFrame.setVisible(true);
        test.validateE4JPropertyGroup();
    }
}



