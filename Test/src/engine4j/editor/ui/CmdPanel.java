package engine4j.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

import engine4j.editor.Editor;
import engine4j.util.ECSComponent;
import engine4j.util.ECSEntity;
import engine4j.util.ECSHelper;

public class CmdPanel extends TextEditor {
    public static final String CMD_GAME_REFRESH = ">> game.refresh";
    public static final String CMD_GAME_SAVE = ">> game.save";

    JTextField commandBar;
    Editor context;
    
    public CmdPanel(Editor context) {
        super(false);
        this.context = context;
        this.remove(this.toolBar);
        this.textArea.setAutoscrolls(true);
        commandBar = new JTextField();
        commandBar.setPreferredSize(new Dimension(30, 40));
        commandBar.setBackground(EditorStyle.BACKGROUND);
        commandBar.setForeground(Color.lightGray);
        commandBar.setCaretColor(Color.lightGray);
        commandBar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        commandBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EditorStyle.BACKGROUND_ACCENT, 1),
            BorderFactory.createMatteBorder(10, 10, 10, 10, EditorStyle.BACKGROUND)
        ));
            
        commandBar.setText(">> ");
        
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                println(commandBar.getText());
                commandBar.setText(">> ");
            }
        };
        
        commandBar.addActionListener(action);
        this.add(commandBar, BorderLayout.SOUTH);
    }
        
    public void print(String buffer) {
        this.textArea.setText(this.textArea.getText() + buffer);
    }
    
    public void println() {
        this.textArea.setText(this.textArea.getText() + '\n');
    }

    private String getSubStr(String str) {
        return str.substring(str.indexOf("\"") + 1, str.lastIndexOf("\""));
    }

    public void println(String buffer) {
        this.textArea.setText(this.textArea.getText() + buffer + '\n');

        if (buffer.startsWith(">> ")) {
            String clearedBuffer = buffer.replace(">> ", "");
            String[] args = clearedBuffer.split(" ");

            switch (args[0]) {
                case "game.set" -> {
                    switch (args[1]) {
                        case "level" -> {
                            context.transitionLevel(getSubStr(clearedBuffer), 55, Color.black);
                            this.println("~~Game level was set to \"" + getSubStr(clearedBuffer) + "\"");
                        }
                    }
                } case "game.new" -> {
                    switch (args[1]) {
                        case "entity" -> {
                            String[] entityArgs = clearedBuffer.substring(clearedBuffer.indexOf("[") + 1, 
                                clearedBuffer.lastIndexOf("]")).split(",");
                            
                            context.editorAppendEntity(getSubStr(entityArgs[0]), getSubStr(entityArgs[1]), getSubStr(entityArgs[2]));
                        } case "component" -> {
                            String[] componentArgs = clearedBuffer.substring(clearedBuffer.indexOf("[") + 1, 
                                clearedBuffer.lastIndexOf("]")).split(",");
                            
                            for (int i = 0; i < context.framework.entitys.getSize(); i++) {
                                ECSEntity e = context.framework.entitys.get(i);
                                
                                if (e.id.startsWith(args[2])) {
                                    ECSHelper.addComponentIfNotExist(e, new ECSComponent(getSubStr(componentArgs[0]), getSubStr(componentArgs[1]), getSubStr(componentArgs[2]), getSubStr(componentArgs[3]).toCharArray(), true, true));
                                    break;
                                }
                            }
                            
                            context.updateProperties();
                            this.println(String.format("~~Component \"%s\" of type \"%s\" was added to Entity \"%s\"", getSubStr(componentArgs[2]), getSubStr(componentArgs[1]), args[2]));
                        }
                    }
                } case "game.save" -> {
                    context.framework.saveLevel();
                    this.println("~~Game was saved");
                } case "game.refresh" -> {
                    context.transitionLevel(context.defaultLevelName, 55, Color.black);
                    this.println("~~Game was refreshed");
                }
            }
        }
    }
}
