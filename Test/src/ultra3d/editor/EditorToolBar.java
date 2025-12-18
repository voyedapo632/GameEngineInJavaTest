package ultra3d.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ultra3d.editor.ui.U3DButton;
import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DToolItemLarge;
import ultra3d.editor.ui.U3DToolItemSeperator;

public class EditorToolBar extends JPanel {
    private U3DEditor editor;
    private U3DToolItemLarge saveButton;
    private U3DToolItemLarge cutButton;
    private U3DToolItemLarge copyButton;
    private U3DToolItemLarge pasteButton;
    private U3DButton settingsButton;
    private U3DToolItemLarge playButton;
    private U3DToolItemLarge pauseButton;
    private U3DToolItemLarge stopButton;

    public EditorToolBar(U3DEditor editor) {
        super(new FlowLayout(FlowLayout.LEFT, 4, 4));
        this.editor = editor;
        
        setBackground(U3DColors.forground);
        setPreferredSize(new Dimension(40, 40));

        // Save button
        saveButton = new U3DToolItemLarge(new JLabel(" 💾"), " ", 0);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.sceneManager.getLastActiveScene().saveScene();
            }
        });
        add(saveButton);

        add(new U3DToolItemSeperator(32, U3DColors.background));

        // Cut
        cutButton = new U3DToolItemLarge(new JLabel(" ✂️"), "Cut", 0);
        cutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.sceneManager.getLastActiveScene().cutSelectedEntities();
            }
        });
        add(cutButton);

        // Copy
        copyButton = new U3DToolItemLarge(new JLabel("🔗"), "Copy", 0);
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.sceneManager.getLastActiveScene().clearCopiedEntities();
                editor.sceneManager.getLastActiveScene().copySelectedEntities();
            }
        });
        add(copyButton);

        // Paste
        pasteButton = new U3DToolItemLarge(new JLabel("📋"), "Paste ", 0);
        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.sceneManager.getLastActiveScene().pasteCopiedEntities();
            }
        });
        add(pasteButton);

        add(new U3DToolItemSeperator(32, U3DColors.background));
        
        // Play
        playButton = new U3DToolItemLarge(new JLabel(" ▶︎"), "Play ", 0);
        playButton.icon.setForeground(U3DColors.simpleGreenOld);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.startPlayTest();
            }
        });

        add(playButton);

        // Pause
        pauseButton = new U3DToolItemLarge(new JLabel("❚❚"), "Pause ", 0);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.pausePlayTest();
            }
        });
        add(pauseButton);

        // Stop
        stopButton = new U3DToolItemLarge(new JLabel("◼"), "Stop ", 0);
        stopButton.icon.setForeground(U3DColors.lightRed);

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.endPlayTest();
            }
        });

        add(stopButton);

        add(new U3DToolItemSeperator(32, U3DColors.background));

        // Settings
        settingsButton = new U3DButton("⚙️ Settings");
        settingsButton.setPreferredSize(new Dimension(70, 30));
        add(settingsButton);
    }
}
