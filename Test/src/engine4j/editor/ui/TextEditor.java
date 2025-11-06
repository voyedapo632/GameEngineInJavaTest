package engine4j.editor.ui;

import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class TextEditor extends JPanel {
    public String source;
    public String text;
    public JTextPane textArea;
    public JScrollPane scrollPane;
    public JPanel toolBar;

    public TextEditor(boolean isEditable) {
        this.setLayout(new BorderLayout());
        
        textArea = new JTextPane();
        textArea.setBackground(EditorStyle.BACKGROUND);
        textArea.setForeground(Color.lightGray);
        textArea.setCaretColor(Color.lightGray);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        textArea.setBorder(null);
        textArea.setEditable(isEditable);

        scrollPane = new JScrollPane(textArea, 
                                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        
        this.add(scrollPane, BorderLayout.CENTER);

        // Toolbar
        toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBackground(EditorStyle.BACKGROUND_ACCENT);
        toolBar.setPreferredSize(new Dimension(35, 35));

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(EditorStyle.BACKGROUND_ACCENT);
        saveButton.setForeground(Color.lightGray);
        toolBar.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        this.add(toolBar, BorderLayout.NORTH);
    }

    public TextEditor(String _source, boolean isEditable) {
        this.setLayout(new BorderLayout());
        
        textArea = new JTextPane();
        
        textArea.setBackground(EditorStyle.BACKGROUND);
        textArea.setForeground(Color.lightGray);
        textArea.setCaretColor(Color.lightGray);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        textArea.setBorder(null);
        textArea.setEditable(isEditable);
        this.open(_source);

        scrollPane = new JScrollPane(textArea, 
                                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
        
        this.add(scrollPane, BorderLayout.CENTER);

        // Toolbar
        toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBackground(EditorStyle.BACKGROUND_ACCENT);
        toolBar.setPreferredSize(new Dimension(35, 35));

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(EditorStyle.BACKGROUND_ACCENT);
        saveButton.setForeground(Color.lightGray);
        toolBar.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        this.add(toolBar, BorderLayout.NORTH);
    }

    public void open(String _source) {
        source = _source;
        text = readFile(source);
        this.validateTextEditor();
    }

    public void save() {
        FileWriter fr;
        try {
            fr = new FileWriter(source);
            fr.write(textArea.getText());
            fr.flush();
            fr.close();
        } catch (IOException ex) {
            System.out.println("TEST!");
        }
    }

    public void validateTextEditor() {
        textArea.setText(text);
        textArea.validate();
        if (scrollPane != null) {
            scrollPane.validate();
        }
    }

    private String readFile(String path) {
        FileReader fr;
        try {
            fr = new FileReader(path);
            String txt = "";
            int i;

            try {
                // Using read method
                while ((i = fr.read()) != -1) {
                    txt += (char)i;
                }
            } catch (IOException ex) {
            }

            return txt;
        } catch (FileNotFoundException ex) {
        }

        return "File path \"" + path + "\" not found."; 
    }
}
