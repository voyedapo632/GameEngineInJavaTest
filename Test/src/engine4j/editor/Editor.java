package engine4j.editor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;

import engine4j.editor.ui.CmdPanel;
import engine4j.editor.ui.CustomTab;
import engine4j.editor.ui.EditorStyle;
import engine4j.editor.ui.TabLayout;
import engine4j.editor.ui.TabManager;
import engine4j.editor.ui.TextEditor;
import engine4j.editor.ui.ToolButton;
import engine4j.util.ECSComponent;
import engine4j.util.ECSEntity;
import engine4j.util.ECSHelper;
import engine4j.util.GameWindow;
import engine4j.util.SimpleRect;
import engine4j.util.Vec3;
import engine4j.Framework;
import engine4j.util.Vec2;

import javax.swing.JTextArea;
import javax.swing.border.Border;

class ContentBrowser extends TabLayout {
    JPanel contentBrowserPanel;
    JPanel sidePanel;
    JPanel mainList;
    JPanel mainList2;
    JPanel childViewPanel;
    JLabel childViewPanelLabel;
    JPanel childViewPanelContent;
    private GridBagConstraints gbc;
    private GridBagConstraints gbc2;
    private String projectPath;
    private String selectedProject = "";
    private Editor editor;

    public ContentBrowser(Editor editor, Dimension size, TabManager _mainManager, String _projectPath) {
        super(size, _mainManager);
        this.editor = editor;
        projectPath = _projectPath;
        selectedProject = projectPath;
        contentBrowserPanel = new JPanel(new BorderLayout());
        contentBrowserPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        this.addTab(new CustomTab("Content Browser", new JLabel("🖿"), contentBrowserPanel));

        // Child view panel
        childViewPanel = new JPanel(new BorderLayout());
        childViewPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        contentBrowserPanel.add(childViewPanel, BorderLayout.CENTER);

        // Label
        childViewPanelLabel = new JLabel("  " + _projectPath);
        childViewPanelLabel.setPreferredSize(new Dimension(40, 40));
        childViewPanelLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        childViewPanelLabel.setForeground(Color.lightGray);
        childViewPanel.add(childViewPanelLabel, BorderLayout.NORTH);
        
        // Content
        childViewPanelContent = new JPanel(new BorderLayout());
        childViewPanelContent.setBackground(EditorStyle.BACKGROUND);
        childViewPanelContent.setBorder(BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT_2, 3));
        childViewPanel.add(childViewPanelContent, BorderLayout.CENTER);

        // Main list
        mainList2 = new JPanel(new GridBagLayout());
        mainList2.setBackground(EditorStyle.BACKGROUND);
        childViewPanelContent.add(mainList2, BorderLayout.NORTH);

        // Side panel
        sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, 200));
        sidePanel.setBackground(EditorStyle.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 3));
        contentBrowserPanel.add(sidePanel, BorderLayout.WEST);

        // Main list
        mainList = new JPanel(new GridBagLayout());
        mainList.setBackground(EditorStyle.BACKGROUND);
        sidePanel.add(mainList, BorderLayout.NORTH);

        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc2 = new GridBagConstraints();
        gbc2.ipady = 1;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1;
        gbc2.weighty = 1;
        gbc2.anchor = GridBagConstraints.NORTH;

        loadDirectories();
    }

    private void loadDirectories() {
        File dir = new File(projectPath);
        File[] files = dir.listFiles();

        mainList.removeAll();
        int count = 0;

        for (int i = files.length  - 1; i >= 0; i--) {
            File file = files[i];
            
            if (file.isDirectory()) {
                JButton Button = new JButton();
                
                if (count % 2 == 0) {
                    Button.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
                } else {
                    Button.setBackground(EditorStyle.BACKGROUND_ACCENT);
                }

                count++;
                
                Button.setLayout(new BorderLayout());
                Button.setPreferredSize(new Dimension(25, 25));

                JLabel label = new JLabel("  🖿  " + file.getName());
                label.setPreferredSize(new Dimension(25, 25));
                label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                label.setForeground(Color.lightGray);
                Button.add(label, BorderLayout.NORTH);

                if (selectedProject.equals(file.getPath())) {
                    Button.setBorder(BorderFactory.createLineBorder(EditorStyle.BLUE_SELECTION, 1));
                } else {
                    Button.setBorder(BorderFactory.createLineBorder(EditorStyle.BACKGROUND, 1));
                }

                Button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedProject = file.getPath();
                        loadDirectories();
                    }
                });

                mainList.add(Button, gbc, 0);

                mainList.validate();
            }
        }

        updateList();
    }

    private void updateList() {
        int start = selectedProject.indexOf("Projects");
        
        if (start == -1) {
            start = 0;
        }

        childViewPanelLabel.setText("   " + selectedProject.substring(start, selectedProject.length()));

        File dir = new File(selectedProject);
        File[] files = dir.listFiles();

        mainList2.removeAll();

        int count = 0;

        for (int i = files.length  - 1; i >= 0; i--) {
            File file = files[i];

            {
                JButton Button = new JButton();
                //Button.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
                Button.setLayout(new BorderLayout());
                Button.setPreferredSize(new Dimension(25, 25));

                if (count % 2 == 0) {
                    Button.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
                } else {
                    Button.setBackground(EditorStyle.BACKGROUND_ACCENT);
                }

                count++;

                String icon = "🖿";

                if (!file.isDirectory()) {
                    icon = "🗎";
                }

                JLabel label = new JLabel("  " + icon + "  " + file.getName());
                label.setBackground(null);
                label.setPreferredSize(new Dimension(25, 25));
                label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                label.setForeground(Color.lightGray);

                Button.add(label, BorderLayout.CENTER);
                Button.setBorder(BorderFactory.createLineBorder(EditorStyle.BACKGROUND, 1));

                Button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (file.isDirectory()) {
                            selectedProject = file.getPath();
                            loadDirectories();
                            return;
                        }

                        if (file.getName().startsWith("level")) {
                            editor.terminalEditor.println(">> game.set level \"" + file.getName() + "\"");
                            TextEditor textEditor = new TextEditor(file.getPath(), true);
                            editor.tabLayout.addTab(new CustomTab(file.getName(), new JLabel("🗎"), textEditor));
                        } else {
                            TextEditor textEditor = new TextEditor(file.getPath(), true);
                            CustomTab tab = new CustomTab(file.getName(), new JLabel("🗎"), textEditor);
                            editor.tabLayout.addTab(tab);
                            editor.tabLayout.activeTab = file.getName();
                            editor.tabLayout.validateTabs();
                        }
                    }
                });

                mainList2.add(Button, gbc, 0);
                mainList2.validate();
            }
        }
    }
}

class PropertyInput extends JPanel {
    public JLabel label;
    public JTextField valueField;
    public ECSComponent component;
    public Editor editor;
    public ECSEntity entity;

    public PropertyInput(String name, ECSComponent component, ECSEntity entity, Editor editor) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(25, 25));
        this.component = component;
        this.entity = entity;
        this.editor = editor;
        this.setBackground(EditorStyle.BACKGROUND);

        label = new JLabel(name);
        label.setForeground(Color.lightGray);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(2, 2, 2, 2, EditorStyle.BACKGROUND)
        ));
        label.setPreferredSize(new Dimension(150, 25));
        valueField = new JTextField(new String(component.value));
        valueField.setForeground(Color.lightGray);
        valueField.setCaretColor(Color.lightGray);
        valueField.setBackground(EditorStyle.BACKGROUND);
        valueField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(2, 2, 2, 2, EditorStyle.BACKGROUND)
        ));

        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                update();
                System.out.println("Propery was updated!");
            }
        };

        valueField.addActionListener(action);

        this.add(label, BorderLayout.WEST);
        this.add(valueField, BorderLayout.CENTER);

        JButton removeComponentButton = new JButton("-");
        removeComponentButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        removeComponentButton.setPreferredSize(new Dimension(30, label.getHeight()));
        removeComponentButton.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
        removeComponentButton.setForeground(Color.lightGray);
        removeComponentButton.setBorder(null);

        removeComponentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                editor.shouldRemoveComponent = true;
                editor.currentComponent = component.identifier;
                editor.currentEntity = entity;
                editor.updateProperties();
            }
        });

        this.add(removeComponentButton, BorderLayout.EAST);
    }

    public void update() {
        //valueInput = valueField.getText().toCharArray();
        editor.framework.changeHistory.setSize(editor.framework.changeHistoryPosition + 1);
        editor.framework.changeHistory.add("property:" + entity.id + "/" + component.identifier + "/" + new String(component.value));
        editor.terminalEditor.println("~~from~~" + "property:" + entity.id + "/" + component.identifier + "/" + new String(component.value));
        
        char[] newValue = valueField.getText().toCharArray();
        
        switch (component.type) {
            case "Vec3" -> {
                newValue = Vec3.interpretValue(newValue);
            } case "Vec2" -> {
                newValue = Vec2.interpretValue(newValue);
            } case "int" -> {
                String newInt = "";

                for (int i = 0; i < newValue.length; i++) {
                    if (newValue[i] >= '0' && newValue[i] <= '9') {
                        newInt += newValue[i];
                    }
                }

                newValue = newInt.toCharArray();
            } case "boolean" -> {
                if (new String(newValue).toLowerCase().contains("t") || new String(newValue).toLowerCase().contains("1")) {
                    newValue = "true".toCharArray();
                } else {
                    newValue = "false".toCharArray();
                }
            }
        }
        
        component.value = newValue;
        valueField.setText(new String(component.value));
        valueField.validate();
        valueField.getParent().validate();

        editor.framework.changeHistory.add("property:" + entity.id + "/" + component.identifier + "/" + new String(component.value));
        editor.terminalEditor.println("~~to~~" + "property:" + entity.id + "/" + component.identifier + "/" + new String(component.value));
        editor.framework.changeHistoryPosition = editor.framework.changeHistory.size() - 1;
    }
}
        
class DropPanel extends JPanel {
    public GridBagConstraints gbc;
    public JLabel name;
    public JPanel titleBar;
    public JToggleButton toggle;
    public JPanel contentPanel;

    public GridBagConstraints getGbc() {
        return gbc;
    }

    public DropPanel(Editor editor, ECSEntity entity, String _name) {
        name = new JLabel(_name);
        name.setForeground(Color.lightGray);
        setLayout(new BorderLayout());
        
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        // Title bar
        titleBar = new JPanel(new BorderLayout());
        titleBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titleBar.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
        toggle = new JToggleButton("🞃  ");
        toggle.setUI(new MetalButtonUI() {
            @Override
            protected Color getSelectColor() {
                return EditorStyle.BACKGROUND_ACCENT_2;
            }
        });
        toggle.setForeground(Color.lightGray);
        toggle.setBorder(null);
        toggle.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
        titleBar.add(name, BorderLayout.CENTER);
        titleBar.add(toggle, BorderLayout.WEST);

        JButton addComponentButton = new JButton("+");
        addComponentButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        addComponentButton.setPreferredSize(new Dimension(30, toggle.getHeight()));
        addComponentButton.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
        addComponentButton.setForeground(Color.lightGray);
        addComponentButton.setBorder(null);

        addComponentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                NewComponentDialogBox dialogBox = new NewComponentDialogBox(editor, entity.id, _name);
                dialogBox.start((long)(1000.0 / 60.0)); // 60 FPS
            }
        });

        titleBar.add(addComponentButton, BorderLayout.EAST);


        add(titleBar, BorderLayout.CENTER);

        // Toggle
        toggle.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent)
            {
                int state = itemEvent.getStateChange();

                if (state == ItemEvent.SELECTED) {
                    add(contentPanel, BorderLayout.SOUTH);
                    toggle.setText("🞁  ");
                }
                else {
                    remove(contentPanel);
                    toggle.setText("🞃  ");
                }

                validate();
            }
        });

        // Content panel
        contentPanel = new JPanel(new GridBagLayout());
        // contentPanel.add(new JLabel("Hello"), gbc, 0);
        // contentPanel.add(new JLabel("Hello"), gbc, 0);
        // contentPanel.add(new JLabel("Hello"), gbc, 0);
        contentPanel.setBorder(new MatteBorder(0, 0, 1, 0, EditorStyle.BACKGROUND_ACCENT));
    }
}

class MyCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Color getBackgroundNonSelectionColor() {
        return (null);
    }

    @Override
    public Color getBackgroundSelectionColor() {
        return EditorStyle.BACKGROUND_ACCENT_2;
    }

    @Override
    public Color getTextNonSelectionColor() {
        return Color.lightGray;
    }
    
    @Override
    public Color getTextSelectionColor() {
        return Color.white;
    }

    @Override
    public Color getBackground() {
        return (null);
    }

    @Override
    public Component getTreeCellRendererComponent(
        final JTree tree, final Object value, 
        final boolean sel, final boolean expanded, 
        final boolean leaf, final int row, 
        final boolean hasFocus
        ) {
        final Component ret = super.getTreeCellRendererComponent(tree, value, sel, 
            expanded, leaf, row, hasFocus);

        final DefaultMutableTreeNode node = ((DefaultMutableTreeNode) (value));
        this.setText(value.toString());
        return ret;
    }
}

public class Editor extends GameWindow {
    public Framework framework;
    public JPanel viewportPanel;
    public JPanel outlinerPanel;
    public DefaultMutableTreeNode spritesRoot;
    public JTree outlinerTree;
    public TabManager tabManager;
    public JPanel mainList;
    public JScrollPane propertiesScroll;
    public JPanel propertiesPanel;
    public TabLayout properties;
    public JPanel toolBar;
    public String projectPath;
    public String defaultLevelName;
    public CmdPanel terminalEditor;
    public ProjectBrowser projectBrowserWindow;
    public TabLayout tabLayout;
    public boolean updateFocus;
    public boolean shouldRemoveComponent = false;
    public String currentComponent = "";
    public ECSEntity currentEntity;
    public JLabel modifyAnEntityLabel;
    public ToolButton playTestButton;

    public Editor(int width, int height, String _projectPath, ProjectBrowser projectBrowserWindow) {
        super(width, height, "Engine4J (Editor)");
        this.projectBrowserWindow = projectBrowserWindow;
        projectPath = _projectPath;
        tabManager = new TabManager();
        defaultLevelName = "level1.txt";
        updateFocus = false;
        this.initializeComponets();
    }

    private void initializeComponets() {
        // Viewport
        viewportPanel = new JPanel();

        viewportPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateFocus = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateFocus = true;
            }
        });

        viewportPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                framework.keyCodeDown = keyCode;
                if (!framework.keyCodeDownCodes.contains(keyCode)) {
                    framework.keyCodeDownCodes.add(keyCode);
                }

                if (framework.keyCodeDownCodes.contains(KeyEvent.VK_CONTROL) &&
                    framework.keyCodeDownCodes.contains(KeyEvent.VK_SHIFT) &&
                    framework.keyCodeDownCodes.contains(KeyEvent.VK_Z)) {
                    if (framework.changeHistoryPosition < framework.changeHistory.size()) {
                        framework.changeHistoryPosition++;
                    }
                    System.out.println(framework.changeHistory.get(framework.changeHistoryPosition));

                }else if (framework.keyCodeDownCodes.contains(KeyEvent.VK_CONTROL) &&
                    framework.keyCodeDownCodes.contains(KeyEvent.VK_Z)) {
                    if (framework.changeHistoryPosition > 0) {
                        framework.changeHistoryPosition--;
                    }
                    System.out.println(framework.changeHistory.get(framework.changeHistoryPosition));

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (int i = 0; i < framework.keyCodeDownCodes.getSize(); i++) {
                    Integer key = framework.keyCodeDownCodes.get(i);

                    if (key.equals(e.getKeyCode())) {
                        framework.keyCodeDownCodes.remove(i);
                    }
                }
            }
        });
        
        viewportPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {                
                viewportPanel.setVisible(true);
                viewportPanel.requestFocus();

                if (!framework.multiSelect && framework.contextState == Framework.CONTEXT_EDITOR) {
                    updateSelection();
                    framework.selectedActors.clear();
                    updateProperties();
                }

                framework.mouseClicked = true;
                framework.mousePosition.x = e.getX();
                framework.mousePosition.y = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                framework.mousePosition.x = e.getX();
                framework.mousePosition.y = e.getY();
                System.out.println("Test!");
            }
        });

        // Mener bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(EditorStyle.BACKGROUND);
        menuBar.setBorder(new EmptyBorder(1, 1, 1, 1));
        menuBar.setForeground(Color.lightGray);
        
        JMenu file = new JMenu("File");
        file.setForeground(Color.lightGray);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        
        file.add(saveMenuItem);
        file.add(exitMenuItem);
        
        JMenu edit = new JMenu("Edit");
        edit.setForeground(Color.lightGray);
        JMenu view = new JMenu("View");
        view.setForeground(Color.lightGray);
        JMenu selection = new JMenu("Selection");
        selection.setForeground(Color.lightGray);
        JMenu debug = new JMenu("Debug");
        JMenuItem playMenuItem = new JMenuItem("▶︎ Play");
        JMenuItem stopMenuItem = new JMenuItem("⏹ Stop");
        
        debug.add(playMenuItem);
        debug.add(stopMenuItem);
        debug.setForeground(Color.lightGray);
        JMenu help = new JMenu("Help");
        help.setForeground(Color.lightGray);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(view);
        menuBar.add(selection);
        menuBar.add(debug);
        menuBar.add(help);
        
        this.setJMenuBar(menuBar);

        // Tool bar
        toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBackground(EditorStyle.BACKGROUND);

        ToolButton undoButton = new ToolButton("↩");
        toolBar.add(undoButton);

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (framework.changeHistoryPosition > 0) {
                    framework.changeHistoryPosition--;
                }

                System.out.println(framework.changeHistory.get(framework.changeHistoryPosition));
            }
        });

        ToolButton redoButton = new ToolButton("↪");

        toolBar.add(redoButton);

        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (framework.changeHistoryPosition < framework.changeHistory.size() - 1) {
                    framework.changeHistoryPosition++;
                }
                
                System.out.println(framework.changeHistory.get(framework.changeHistoryPosition));
            }
        });

        ToolButton refreshButton = new ToolButton("↻ Refresh");

        toolBar.add(refreshButton);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // framework.loadLevel(projectPath + "\\Levels" + "\\" + defaultLevelName);
                // validateOutliner();
                // updateProperties();
                terminalEditor.println(CmdPanel.CMD_GAME_REFRESH);
            }
        });

        ToolButton saveButton = new ToolButton("💾 Save");

        toolBar.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminalEditor.println(CmdPanel.CMD_GAME_SAVE);
                validateOutliner();
            }
        });

        this.add(toolBar, BorderLayout.NORTH);

        // Play test button
        playTestButton = new ToolButton("▶︎ Play");
        playTestButton.setForeground(new Color(165, 225, 120));

        toolBar.add(playTestButton);

        playTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (framework.contextState.startsWith(Framework.CONTEXT_EDITOR)) {
                    framework.reloadScripts();
                    framework.contextState = Framework.CONTEXT_GAME;
                    playTestButton.setText("⏹ Stop");
                    playTestButton.setForeground(new Color(225, 140, 120));
                    framework.saveLevel();
                    terminalEditor.println("~~Game Session Has Started...");
                } else {
                    framework.contextState = Framework.CONTEXT_EDITOR;
                    playTestButton.setText("▶︎ Play");
                    playTestButton.setForeground(new Color(165, 225, 120));
                    terminalEditor.println("~~Game Session Has Ended");
                    framework.switchToLevel(defaultLevelName, 25, Color.black);
                }

                viewportPanel.requestFocus();
            }
        });

       //this.add(viewportPanel, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new BorderLayout());

        bottomBar.setBackground(EditorStyle.BACKGROUND_ACCENT);
        bottomBar.setPreferredSize(new Dimension(25, 25));
        tabManager.add(bottomBar, BorderLayout.SOUTH);

        // Tab Layout
        tabLayout = new TabLayout(new Dimension(350, 600), tabManager);
        tabLayout.addTab(new CustomTab("World Viewport", new JLabel("🗔"), viewportPanel));
        tabLayout.dockAt(tabManager, BorderLayout.CENTER);

        // TextEditor textEditor = new TextEditor(projectPath + "\\Levels" + "\\" + defaultLevelName, true);
        // tabLayout.addTab(new CustomTab("level1.txt", new JLabel("🗎"), textEditor));

        // Outliner
        outlinerPanel = new JPanel();
        outlinerPanel.setBackground(EditorStyle.BACKGROUND);
        outlinerPanel.setLayout(new BorderLayout());
        TabLayout outliner = new TabLayout(new Dimension(350, 300), tabManager);
        outliner.addTab(new CustomTab("Outliner", new JLabel("Ψ"), outlinerPanel));
        outliner.dockAt(tabManager, BorderLayout.EAST);

        // Properties window
        propertiesPanel = new JPanel();
        propertiesPanel.setBorder(null);
        propertiesPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        propertiesPanel.setLayout(new BorderLayout());
        //propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
        
        properties = new TabLayout(new Dimension(300, 470), tabManager);
        properties.addTab(new CustomTab("Properties", new JLabel("𝓲"), propertiesPanel));
        
        JPanel scrollContent = new JPanel();
        
        scrollContent.setLayout(new GridLayout(0, 1, 0,10));
        scrollContent.setBackground(EditorStyle.BACKGROUND_ACCENT);

        JButton b = new JButton("b");
        b.setPreferredSize(new Dimension(10, 50));
        scrollContent.add(b);

        for (int i = 0; i < 20; i++) {
            JButton a = new JButton("a");
            scrollContent.add(a);
        }
        
        ////propertiesScroll.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
        //
        //DropPanel translate = new DropPanel("Translation:");
        //DropPanel scale = new DropPanel("Scale:");
        //DropPanel rotation = new DropPanel("Rotation:");
        //
        //scrollContent.add(translate);
        //scrollContent.add(scale);
        //scrollContent.add(rotation);
        //
        // JPanel panel = new JPanel();
// 
        // panel.setPreferredSize(new Dimension(300, 1));
        // panel.setLayout(new FlowLayout(FlowLayout.LEFT));
// 
        // for (int i = 0; i < 100; i++) {
        //     JButton btn = new JButton("Button!");
        //     btn.setPreferredSize(new Dimension(250, 30));
        //     panel.add(btn);
        // }

        JPanel result = new JPanel();
    //result.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

    // Create the layout
    //GroupLayout layout = new GroupLayout( result );
    //result.setLayout( new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS) );
    //layout.setAutoCreateGaps( true );

    // Create the components we will put in the form
    //JLabel ipAddressLabel = new JLabel( "IP Address:" );
    //result.add(ipAddressLabel);
    //JTextField ipAddressTextField = new JTextField( 20 );
    //result.add(ipAddressTextField);
    //JLabel subnetLabel = new JLabel( "Subnet:" );
    //result.add(subnetLabel);
    //JTextField subnetTextField = new JTextField( 20 );
    //result.add(subnetTextField);
    //JLabel gatewayLabel = new JLabel( "Gateway:" );
    //result.add(gatewayLabel);
    //JTextField gatewayTextField = new JTextField( 20 );
    //result.add(gatewayTextField);

        mainList = new JPanel(new GridBagLayout());
        mainList.setBackground(EditorStyle.BACKGROUND_ACCENT);
        mainList.setBorder(null);
        
        JPanel testPanel = new JPanel(new BorderLayout());
        testPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        testPanel.add(mainList, BorderLayout.NORTH);

        propertiesScroll = new JScrollPane(testPanel);
        propertiesScroll.setBorder(null);
        propertiesPanel.add(propertiesScroll);
        
        properties.dockAt(outliner, BorderLayout.SOUTH);
        
        // ContentBrowser window
        // JPanel contentBrowserPanel = new JPanel();
        // contentBrowserPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        // TabLayout contentBrowser = new TabLayout(new Dimension(350, 270), tabManager);
        // contentBrowser.addTab(new CustomTab("Content Browser", new JLabel("🖿"), contentBrowserPanel));
        // tabLayout.add(contentBrowser, BorderLayout.SOUTH);
        ContentBrowser contentBrowser = new ContentBrowser(this, new Dimension(350, 270), tabManager, projectPath);
        contentBrowser.dockAt(tabLayout, BorderLayout.SOUTH);

        // Terminal
        JPanel terminalPanel = new JPanel();
        terminalPanel.setBackground(EditorStyle.BACKGROUND);
        terminalPanel.setLayout(new BorderLayout());

        terminalEditor = new CmdPanel(this);
        terminalPanel.add(terminalEditor, BorderLayout.CENTER);
        
        TabLayout terminal = new TabLayout(new Dimension(600, 600), tabManager);
        terminal.addTab(new CustomTab("Console", new JLabel(">_"), terminalPanel));
        //contentBrowser.addTab(new CustomTab("Console", new JLabel(">_"), terminalPanel));
        terminal.dockAt(contentBrowser, BorderLayout.EAST);
        terminalEditor.println("~~Hello, World!");
        //terminalEditor.println("██╗  ██╗███████╗██╗   ██╗    ██╗   ██╗ ██████╗ ██╗   ██╗");
        //terminalEditor.println("██║  ██║██╔════╝╚██╗ ██╔╝    ╚██╗ ██╔╝██╔═══██╗██║   ██║");
        //terminalEditor.println("███████║█████╗   ╚████╔╝      ╚████╔╝ ██║   ██║██║   ██║");
        //terminalEditor.println("██╔══██║██╔══╝    ╚██╔╝        ╚██╔╝  ██║   ██║██║   ██║");
        //terminalEditor.println("██║  ██║███████╗   ██║          ██║   ╚██████╔╝╚██████╔╝");

        // Actors window
        JPanel actorsPanel = new JPanel(new GridBagLayout());
        actorsPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);

        // New sprite button
        ToolButton newSpriteButton = new ToolButton("+ New Entity");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        
        toolBar.add(newSpriteButton);
        

        Editor thisEditor = this;

        newSpriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sprite sprite = new Sprite("sprite" + (framework.sprites.size() + 1), 
                //                                  VectorSet.vector2(0, 102 * (framework.sprites.size() - 2)),
                //                                  VectorSet.vector2(100, 100), 
                //                                  Color.blue);
                // sprite.setContext(framework);
                // framework.sprites.add(sprite);
                // framework.selectedActors.clear();
                // framework.selectedActors.add(sprite.id);

                NewEntityDialogBox entityDialogBox = new NewEntityDialogBox(thisEditor);

                entityDialogBox.start((long)(1000.0 / 60.0)); // 60 FPS
            }
        });

        TabLayout actors = new TabLayout(new Dimension(300, 270), tabManager);
        actors.addTab(new CustomTab("Actors", new JLabel("🗗"), actorsPanel));
        //actors.dockAt(tabLayout, BorderLayout.WEST);

        // Outliner tree
        spritesRoot = new DefaultMutableTreeNode("Sprites");

        this.add(tabManager, BorderLayout.CENTER);

        // Modify and entity label
        modifyAnEntityLabel = new JLabel("Select an entity to modify.", SwingConstants.CENTER);
        modifyAnEntityLabel.setPreferredSize(new Dimension(40, 40));
        modifyAnEntityLabel.setBackground(EditorStyle.BACKGROUND);
        modifyAnEntityLabel.setForeground(Color.lightGray);
        propertiesPanel.add(modifyAnEntityLabel, BorderLayout.NORTH);
    }
    
    public void validateOutliner() {
        spritesRoot.removeAllChildren();

        for (int i = 0; i < framework.entitys.getSize(); i++) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(framework.entitys.get(i).id);
            spritesRoot.add(node);
        }

        outlinerTree = new JTree(spritesRoot);
        outlinerTree.setCellRenderer(new MyCellRenderer());
        outlinerTree.setForeground(Color.lightGray);

        outlinerTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = 
                    (DefaultMutableTreeNode)outlinerTree.getLastSelectedPathComponent(); 
                String name = (String)selectedNode.getUserObject();
                framework.selectedActors.clear();
                framework.selectedActors.add(name);
                System.out.println(framework.selectedActors);
                updateProperties();
            }
        });

        outlinerTree.setBackground(EditorStyle.BACKGROUND);

        JScrollPane outlinerScoll = new JScrollPane(outlinerTree);
        outlinerScoll.setBackground(EditorStyle.BACKGROUND);
        outlinerScoll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(0, 0, 0, 0))
        ));
        outlinerScoll.setForeground(EditorStyle.BACKGROUND_ACCENT);
        outlinerPanel.removeAll();
        outlinerPanel.add(outlinerScoll, BorderLayout.CENTER);
        outlinerPanel.validate();
    }

    public void updateProperties() {
        if (framework.selectedActors.isEmpty()) {
            propertiesPanel.add(modifyAnEntityLabel, BorderLayout.NORTH);
        } else {
            propertiesPanel.remove(modifyAnEntityLabel);
        }

        if (shouldRemoveComponent) {
            ECSHelper.removeComponent(currentEntity, currentComponent);
            shouldRemoveComponent = false;
        }

        mainList.removeAll();

        for (int i = 0; i < framework.entitys.getSize(); i++) {
            ECSEntity e = framework.entitys.get(i);

            if (framework.selectedActors.contains(e.id)) {
                Vector<String> groupNames = new Vector<String>();
                Vector<DropPanel> groupPanels = new Vector<DropPanel>();
                //DropPanel p = new DropPanel("Missilanious");
                
                for (ECSComponent c : e.components) {
                    if (!groupNames.contains(c.group)) {
                        groupNames.add(c.group);
                        groupPanels.add(new DropPanel(this, e, c.group));
                    }
                    // PropertyInput property = new PropertyInput(c.identifier, c);
                    // p.contentPanel.add(property, p.gbc, 0);
                }
                
                groupPanels = new Vector<DropPanel>(groupPanels.reversed());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.insets = new Insets(0,0,0,0);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1;
                gbc.weighty = 1;
                gbc.anchor = GridBagConstraints.NORTH;

                for (DropPanel gp : groupPanels) {
                    for (ECSComponent c : e.components) {
                        if (c.group.startsWith(gp.name.getText()) && c.canEdit) {
                            PropertyInput property = new PropertyInput(c.identifier, c, e, this);
                            gp.contentPanel.add(property, gp.gbc, 0);
                            gp.contentPanel.validate();
                        }
                    }
                    
                    mainList.add(gp, gbc, 0);
                    gp.toggle.doClick();
                }
                
                JLabel selectedLabel = new JLabel("  " + e.id);
                selectedLabel.setLayout(new BorderLayout());
                selectedLabel.setBackground(EditorStyle.BACKGROUND_ACCENT);
                selectedLabel.setForeground(Color.lightGray);
                selectedLabel.setPreferredSize(new Dimension(40, 40));
                mainList.add(selectedLabel, gbc, 0);

                JButton addComponentButton = new JButton("+");
                addComponentButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 23));
                addComponentButton.setPreferredSize(new Dimension(40, 40));
                addComponentButton.setBackground(EditorStyle.BACKGROUND_ACCENT);
                addComponentButton.setForeground(Color.lightGray);
                addComponentButton.setBorder(null);

                selectedLabel.add(addComponentButton, BorderLayout.EAST);

                Editor thisEditor = this;

                addComponentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        NewComponentDialogBox dialogBox = new NewComponentDialogBox(thisEditor, e.id);
                        dialogBox.start((long)(1000.0 / 60.0)); // 60 FPS
                    }
                });

                mainList.validate();
                mainList.updateUI();
                propertiesScroll.updateUI();
                propertiesScroll.setBorder(null);
                propertiesScroll.validate();
                propertiesPanel.validate();
                propertiesPanel.updateUI();

            }
        }

        propertiesPanel.validate();
        propertiesPanel.updateUI();
    }

    public void changeLevel(String name) {
        viewportPanel.setVisible(false);

        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
        }

        defaultLevelName = name;
        framework.loadLevel(projectPath + "\\Levels" + "\\" + defaultLevelName);
        validateOutliner();
        framework.selectedActors.clear();
        updateProperties();
        viewportPanel.setVisible(true);
        viewportPanel.requestFocus();
    }

    boolean doTrans = false;
    int fadeTransLevel = 0;
    int transInc = 255;
    String nextLevel = "";
    Color transColor = Color.black;

    public void transitionLevel(String name, int transInc, Color transColor) {
        if (!doTrans) {
            nextLevel = name;
            doTrans = true;
            this.transInc = transInc;
            this.transColor = transColor;
        }
    }

    public void editorAppendEntity(String nameInput, String classInput, String parent) {
        ECSEntity entity = new ECSEntity(nameInput,
                        classInput, parent, framework);

        framework.appendEntity(entity);
        framework.selectedActors.clear();
        framework.selectedActors.add(entity.id);

        validateOutliner();
        updateProperties();
        terminalEditor.println(String.format("New entity [%s, %s, %s] was add!", 
        entity.id, entity.childInstanceId, entity.parent));
    }

    @Override
    protected void onInit() {
        viewportPanel.requestFocus();
        System.out.println("Editor started!");
        framework = new Framework(viewportPanel.getGraphics(), viewportPanel.getSize(), viewportPanel);
        framework.projectPath = projectPath;
        framework.defaultLevelName = defaultLevelName;
        framework.contextState = Framework.CONTEXT_EDITOR;
        framework.entityInstanceConverters.add(new SimpleRect());
        framework.entityInstanceConverters.add(new ECSEntity());
        framework.loadLevel(projectPath + "\\Levels" + "\\" + defaultLevelName);
        validateOutliner();
    }

    private void updateInput() {
        if (viewportPanel == null) {
            return;
        }

        if (framework.entitys == null) {
            return;
        }

        if (framework.entitys.getSize() == 0) {
            return;
        }

        
        if (framework.keyCodeDownCodes.contains(KeyEvent.VK_ESCAPE)) {
            if (framework.contextState.startsWith(Framework.CONTEXT_EDITOR)) {
                updateSelection();
                framework.selectedActors.clear();
                updateProperties();
            } else {
                playTestButton.doClick();
            }
        }
        
        float speed = 10.0f;

        if (framework.contextState.startsWith(Framework.CONTEXT_EDITOR)) {
            if (framework.keyCodeDownCodes.contains(KeyEvent.VK_SHIFT) ||
                framework.keyCodeDownCodes.contains(KeyEvent.VK_CONTROL)) {
                framework.multiSelect = true;
            } else {
                framework.multiSelect = false;
            }
            
            if (framework.keyCodeDownCodes.contains(KeyEvent.VK_W)) {
                framework.cameraPos.y -= speed;
            } else if (framework.keyCodeDownCodes.contains(KeyEvent.VK_S)) {
                framework.cameraPos.y += speed;
            }
            
            if (framework.keyCodeDownCodes.contains(KeyEvent.VK_A)) {
                framework.cameraPos.x -= speed;
            } else if (framework.keyCodeDownCodes.contains(KeyEvent.VK_D)) {
                framework.cameraPos.x += speed;
            }

            if (framework.keyCodeDownCodes.contains(KeyEvent.VK_UP)) {
                framework.cameraPos.z += 0.03;
            } else if (framework.keyCodeDownCodes.contains(KeyEvent.VK_DOWN)) {
                framework.cameraPos.z -= 0.03;
            }
        }
    }

    @Override
    protected void onTick() {
        updateInput();
        framework.mousePosition = viewportPanel.getMousePosition();
        int lastCount = framework.selectedActors.size();
        
        if (viewportPanel.isVisible() && framework.gfx != null) {
            framework.render();
            
            if (doTrans) {
                if (fadeTransLevel < 255 - transInc) {
                    fadeTransLevel += transInc;
                } else {
                    viewportPanel.setBackground(transColor);
                    changeLevel(nextLevel);
                    doTrans = false;
                }
            }
// 
            if (!doTrans) {
                if (fadeTransLevel >= transInc) {
                    fadeTransLevel -= transInc;
                }
            }
// 
            framework.gfx.setColor(new Color(transColor.getRed(), transColor.getGreen(), transColor.getBlue(), fadeTransLevel));
            framework.gfx.fillRect(0, 0, viewportPanel.getWidth(), viewportPanel.getHeight());

            if (viewportPanel.hasFocus()) {
                framework.gfx.setColor(EditorStyle.FOCUS_SELECTION);
                framework.gfx.drawRect(0, 0, viewportPanel.getWidth() - 2, viewportPanel.getHeight() - 1);
            }

            if (lastCount != framework.selectedActors.size()) {
                updateProperties();
            }
            
            framework.swapBuffers(viewportPanel);
            while (viewportPanel.getGraphics() == null || viewportPanel.getWidth() < 1 || viewportPanel.getHeight() < 1);
            framework.update(viewportPanel.getGraphics(), viewportPanel.getSize());
        }
        
        // System.out.println("Game has ticked!");
    }

    public void updateSelection() {
        //framework.changeHistory.setSize(framework.changeHistoryPosition + 1);
        // String selectedBefore = "";
        // 
        // for (int i = 0; i < framework.selectedActors.size(); i++) {
        //     selectedBefore += framework.selectedActors.get(i) + "/";
        // }
        // 
        // if (selectedBefore.endsWith("/")) {
        //     selectedBefore = selectedBefore.substring(0, selectedBefore.length() - 1);
        // }
        // 
        // if (!selectedBefore.isEmpty()) {
        //     framework.changeHistory.add("reselect:" + selectedBefore);
        //     framework.changeHistoryPosition = framework.changeHistory.size();
        // }
    }

    @Override
    protected void onResized() {
        System.out.println(viewportPanel.getSize());
        framework.update(viewportPanel.getGraphics(), viewportPanel.getSize());
    }

    @Override
    protected void onDestroy() {
        System.out.println("Game has ended!");
        framework.cleanup();
        projectBrowserWindow.start((long)(1000.0 / 60.0)); // 60 FPS
        //framework.saveLevel();
    }
}