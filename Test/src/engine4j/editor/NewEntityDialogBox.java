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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import engine4j.editor.ui.EditorStyle;
import engine4j.util.GameWindow;

public class NewEntityDialogBox extends GameWindow {
    Editor editor;

    public NewEntityDialogBox(Editor editor) {
        super(500, 300, "New Entity");
        this.editor = editor;
        this.initializeComponets();
    }

    private void initializeComponets() {
        JPanel backgrounfPanel = new JPanel(new BorderLayout());
        backgrounfPanel.setBackground(EditorStyle.BACKGROUND);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(EditorStyle.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        JLabel nameInputLabel = new JLabel("Name: ");
        nameInputLabel.setLayout(new BorderLayout());
        nameInputLabel.setForeground(Color.lightGray);
        nameInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField nameInput = new JTextField();
        nameInput.setPreferredSize(new Dimension(30, 40));
        nameInput.setBackground(EditorStyle.BACKGROUND);
        nameInput.setForeground(Color.lightGray);
        nameInput.setCaretColor(Color.lightGray);
        nameInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        nameInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));

        JLabel classInputLabel = new JLabel("Class: ");
        classInputLabel.setLayout(new BorderLayout());
        classInputLabel.setForeground(Color.lightGray);
        classInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField classInput = new JTextField();
        classInput.setPreferredSize(new Dimension(30, 40));
        classInput.setBackground(EditorStyle.BACKGROUND);
        classInput.setForeground(Color.lightGray);
        classInput.setCaretColor(Color.lightGray);
        classInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        classInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));

        inputPanel.add(classInput, gbc, 0);
        inputPanel.add(classInputLabel, gbc, 0);
        inputPanel.add(nameInput, gbc, 0);
        inputPanel.add(nameInputLabel, gbc, 0);

        backgrounfPanel.add(inputPanel, BorderLayout.NORTH);
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

        JButton createEntityButton = new JButton("Create");
        createEntityButton.setBackground(EditorStyle.BACKGROUND_ACCENT);
        createEntityButton.setForeground(Color.lightGray);

        createEntityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.editorAppendEntity(nameInput.getText(), classInput.getText(), "root");
                stop();
            }
        });

        bottomBar.add(createEntityButton);
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
