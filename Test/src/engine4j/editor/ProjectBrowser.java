package engine4j.editor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engine4j.editor.ui.EditorStyle;
import engine4j.util.GameWindow;

public class ProjectBrowser extends GameWindow {
    private JPanel inputPanel;
    private GridBagConstraints gbc;
    private String selectedProject;

    public ProjectBrowser() {
        super(1040, 630, "Project Browser");
        this.initializeComponets();
    }

    private void initializeComponets() {
        selectedProject = "";
        
        JPanel backgrounfPanel = new JPanel(new BorderLayout());
        backgrounfPanel.setBackground(EditorStyle.BACKGROUND);
        
        JPanel sideBar = new JPanel(new BorderLayout());
        sideBar.setPreferredSize(new Dimension(250, 250));
        sideBar.setBackground(EditorStyle.BACKGROUND_ACCENT);
        backgrounfPanel.add(sideBar, BorderLayout.WEST);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(EditorStyle.BACKGROUND);
        backgrounfPanel.add(contentPanel, BorderLayout.CENTER);
        
        JLabel windowLabel = new JLabel("   My Projects");
        windowLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        windowLabel.setPreferredSize(new Dimension(60, 60));
        windowLabel.setForeground(Color.lightGray);
        contentPanel.add(windowLabel, BorderLayout.NORTH);

        JPanel projectPanel = new JPanel(new BorderLayout());
        projectPanel.setBackground(EditorStyle.BACKGROUND);
        contentPanel.add(projectPanel, BorderLayout.CENTER);
        
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(EditorStyle.BACKGROUND);
        projectPanel.add(inputPanel, BorderLayout.NORTH);
        
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 30;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        
        loadProjects();
        this.add(backgrounfPanel, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setPreferredSize(new Dimension(50, 50));
        bottomBar.setBackground(EditorStyle.BACKGROUND_ACCENT);
        this.add(bottomBar, BorderLayout.SOUTH);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(EditorStyle.BACKGROUND_ACCENT);
        cancelButton.setForeground(Color.lightGray);
        bottomBar.add(cancelButton);
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        JButton openButton = new JButton("Open");
        openButton.setBackground(EditorStyle.BLUE_SELECTION);
        openButton.setForeground(Color.white);

        ProjectBrowser currentProjectBrowser = this;

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Editor editor = new Editor(1200, 700, selectedProject, currentProjectBrowser);
                editor.setExtendedState(editor.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                editor.start((long)(1000.0 / 60.0)); // 60 FPS
                stop();
            }
        });

        bottomBar.add(openButton);
    }

    private void loadProjects() {
        File dir = new File("Test\\src\\Projects");
        File[] files = dir.listFiles();

        inputPanel.removeAll();

        for (int i = files.length  - 1; i >= 0; i--) {
            File file = files[i];

            if (file.isDirectory()) {
                JButton Button = new JButton();
                Button.setBackground(EditorStyle.BACKGROUND_ACCENT_2);
                Button.setLayout(new BorderLayout());
                Button.setPreferredSize(new Dimension(60, 60));

                JLabel label = new JLabel("   " + file.getName());
                label.setPreferredSize(new Dimension(40, 40));
                label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
                label.setForeground(Color.lightGray);
                Button.add(label, BorderLayout.NORTH);

                JLabel pathLabel = new JLabel("   " + file.getPath());
                pathLabel.setPreferredSize(new Dimension(40, 40));
                pathLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
                pathLabel.setForeground(Color.lightGray);
                Button.add(pathLabel, BorderLayout.SOUTH);

                if (selectedProject.equals(file.getAbsolutePath())) {
                    Button.setBorder(BorderFactory.createLineBorder(EditorStyle.BLUE_SELECTION, 3));
                } else {
                    Button.setBorder(BorderFactory.createLineBorder(EditorStyle.BACKGROUND, 5));
                }

                Button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedProject = file.getAbsolutePath();
                        loadProjects();
                    }
                });

                inputPanel.add(Button, gbc, 0);

                System.out.println(file.getAbsolutePath());
                inputPanel.validate();
            }
        }
    }

    @Override
    protected void onInit() {
    }

    @Override
    protected void onTick() {
      
    }

    @Override
    protected void onResized() {
    }

    @Override
    protected void onDestroy() {
    }
}
