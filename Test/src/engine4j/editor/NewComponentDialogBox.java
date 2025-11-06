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
import engine4j.editor.Editor;

public class NewComponentDialogBox extends GameWindow {
    Editor editor;

    public NewComponentDialogBox(Editor editor, String sprite) {
        super(300, 425, "New Component");
        this.editor = editor;
        this.initializeComponets2(sprite);
    }

    public NewComponentDialogBox(Editor editor, String sprite, String group) {
        super(300, 330, "New Component");
        this.editor = editor;
        this.initializeComponets3(sprite, group);
    }

    private void initializeComponets1() {
        JPanel backgrounfPanel = new JPanel(new BorderLayout());
        backgrounfPanel.setBackground(EditorStyle.BACKGROUND);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(EditorStyle.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        JLabel valueInputLabel = new JLabel("Value: ");
        valueInputLabel.setLayout(new BorderLayout());
        valueInputLabel.setForeground(Color.lightGray);
        valueInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField valueInput = new JTextField();
        valueInput.setPreferredSize(new Dimension(30, 40));
        valueInput.setBackground(EditorStyle.BACKGROUND);
        valueInput.setForeground(Color.lightGray);
        valueInput.setCaretColor(Color.lightGray);
        valueInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        valueInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(valueInput, gbc, 0);
        inputPanel.add(valueInputLabel, gbc, 0);

        JLabel idInputLabel = new JLabel("ID: ");
        idInputLabel.setLayout(new BorderLayout());
        idInputLabel.setForeground(Color.lightGray);
        idInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField idInput = new JTextField();
        idInput.setPreferredSize(new Dimension(30, 40));
        idInput.setBackground(EditorStyle.BACKGROUND);
        idInput.setForeground(Color.lightGray);
        idInput.setCaretColor(Color.lightGray);
        idInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        idInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(idInput, gbc, 0);
        inputPanel.add(idInputLabel, gbc, 0);

        JLabel typeInputLabel = new JLabel("Type: ");
        typeInputLabel.setLayout(new BorderLayout());
        typeInputLabel.setForeground(Color.lightGray);
        typeInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField typeInput = new JTextField();
        typeInput.setPreferredSize(new Dimension(30, 40));
        typeInput.setBackground(EditorStyle.BACKGROUND);
        typeInput.setForeground(Color.lightGray);
        typeInput.setCaretColor(Color.lightGray);
        typeInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        typeInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(typeInput, gbc, 0);
        inputPanel.add(typeInputLabel, gbc, 0);

        JLabel groupInputLabel = new JLabel("Group: ");
        groupInputLabel.setLayout(new BorderLayout());
        groupInputLabel.setForeground(Color.lightGray);
        groupInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField groupInput = new JTextField();
        groupInput.setPreferredSize(new Dimension(30, 40));
        groupInput.setBackground(EditorStyle.BACKGROUND);
        groupInput.setForeground(Color.lightGray);
        groupInput.setCaretColor(Color.lightGray);
        groupInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        groupInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(groupInput, gbc, 0);
        inputPanel.add(groupInputLabel, gbc, 0);

        JLabel spriteInputLabel = new JLabel("Sprite-ID: ");
        spriteInputLabel.setLayout(new BorderLayout());
        spriteInputLabel.setForeground(Color.lightGray);
        spriteInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField spriteInput = new JTextField();
        spriteInput.setPreferredSize(new Dimension(30, 40));
        spriteInput.setBackground(EditorStyle.BACKGROUND);
        spriteInput.setForeground(Color.lightGray);
        spriteInput.setCaretColor(Color.lightGray);
        spriteInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        spriteInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(spriteInput, gbc, 0);
        inputPanel.add(spriteInputLabel, gbc, 0);

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
                String cmd = String.format(">> game.new component %s [\"%s\",\"%s\",\"%s\",\"%s\"]", 
                    spriteInput.getText(), groupInput.getText(), typeInput.getText(), 
                        idInput.getText(), valueInput.getText());
                editor.terminalEditor.println(cmd);
                stop();
            }
        });

        bottomBar.add(createEntityButton);
    }

    private void initializeComponets2(String sprite) {
        JPanel backgrounfPanel = new JPanel(new BorderLayout());
        backgrounfPanel.setBackground(EditorStyle.BACKGROUND);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(EditorStyle.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        JLabel valueInputLabel = new JLabel("Value: ");
        valueInputLabel.setLayout(new BorderLayout());
        valueInputLabel.setForeground(Color.lightGray);
        valueInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField valueInput = new JTextField();
        valueInput.setPreferredSize(new Dimension(30, 40));
        valueInput.setBackground(EditorStyle.BACKGROUND);
        valueInput.setForeground(Color.lightGray);
        valueInput.setCaretColor(Color.lightGray);
        valueInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        valueInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(valueInput, gbc, 0);
        inputPanel.add(valueInputLabel, gbc, 0);

        JLabel idInputLabel = new JLabel("ID: ");
        idInputLabel.setLayout(new BorderLayout());
        idInputLabel.setForeground(Color.lightGray);
        idInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField idInput = new JTextField();
        idInput.setPreferredSize(new Dimension(30, 40));
        idInput.setBackground(EditorStyle.BACKGROUND);
        idInput.setForeground(Color.lightGray);
        idInput.setCaretColor(Color.lightGray);
        idInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        idInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(idInput, gbc, 0);
        inputPanel.add(idInputLabel, gbc, 0);

        JLabel typeInputLabel = new JLabel("Type: ");
        typeInputLabel.setLayout(new BorderLayout());
        typeInputLabel.setForeground(Color.lightGray);
        typeInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField typeInput = new JTextField();
        typeInput.setPreferredSize(new Dimension(30, 40));
        typeInput.setBackground(EditorStyle.BACKGROUND);
        typeInput.setForeground(Color.lightGray);
        typeInput.setCaretColor(Color.lightGray);
        typeInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        typeInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(typeInput, gbc, 0);
        inputPanel.add(typeInputLabel, gbc, 0);

        JLabel groupInputLabel = new JLabel("Group: ");
        groupInputLabel.setLayout(new BorderLayout());
        groupInputLabel.setForeground(Color.lightGray);
        groupInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField groupInput = new JTextField();
        groupInput.setPreferredSize(new Dimension(30, 40));
        groupInput.setBackground(EditorStyle.BACKGROUND);
        groupInput.setForeground(Color.lightGray);
        groupInput.setCaretColor(Color.lightGray);
        groupInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        groupInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(groupInput, gbc, 0);
        inputPanel.add(groupInputLabel, gbc, 0);

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
                String cmd = String.format(">> game.new component %s [\"%s\",\"%s\",\"%s\",\"%s\"]", 
                    sprite, groupInput.getText(), typeInput.getText(), 
                        idInput.getText(), valueInput.getText());
                editor.terminalEditor.println(cmd);
                stop();
            }
        });

        bottomBar.add(createEntityButton);
    }

    private void initializeComponets3(String sprite, String group) {
        JPanel backgrounfPanel = new JPanel(new BorderLayout());
        backgrounfPanel.setBackground(EditorStyle.BACKGROUND);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(EditorStyle.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        JLabel valueInputLabel = new JLabel("Value: ");
        valueInputLabel.setLayout(new BorderLayout());
        valueInputLabel.setForeground(Color.lightGray);
        valueInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField valueInput = new JTextField();
        valueInput.setPreferredSize(new Dimension(30, 40));
        valueInput.setBackground(EditorStyle.BACKGROUND);
        valueInput.setForeground(Color.lightGray);
        valueInput.setCaretColor(Color.lightGray);
        valueInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        valueInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(valueInput, gbc, 0);
        inputPanel.add(valueInputLabel, gbc, 0);

        JLabel idInputLabel = new JLabel("ID: ");
        idInputLabel.setLayout(new BorderLayout());
        idInputLabel.setForeground(Color.lightGray);
        idInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField idInput = new JTextField();
        idInput.setPreferredSize(new Dimension(30, 40));
        idInput.setBackground(EditorStyle.BACKGROUND);
        idInput.setForeground(Color.lightGray);
        idInput.setCaretColor(Color.lightGray);
        idInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        idInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(idInput, gbc, 0);
        inputPanel.add(idInputLabel, gbc, 0);

        JLabel typeInputLabel = new JLabel("Type: ");
        typeInputLabel.setLayout(new BorderLayout());
        typeInputLabel.setForeground(Color.lightGray);
        typeInputLabel.setPreferredSize(new Dimension(30, 40));

        JTextField typeInput = new JTextField();
        typeInput.setPreferredSize(new Dimension(30, 40));
        typeInput.setBackground(EditorStyle.BACKGROUND);
        typeInput.setForeground(Color.lightGray);
        typeInput.setCaretColor(Color.lightGray);
        typeInput.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        typeInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        inputPanel.add(typeInput, gbc, 0);
        inputPanel.add(typeInputLabel, gbc, 0);

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
                String cmd = String.format(">> game.new component %s [\"%s\",\"%s\",\"%s\",\"%s\"]", 
                    sprite, group, typeInput.getText(), 
                        idInput.getText(), valueInput.getText());
                editor.terminalEditor.println(cmd);
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
