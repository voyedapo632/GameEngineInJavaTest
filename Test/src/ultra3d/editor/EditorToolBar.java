package ultra3d.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;

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
        
        setBackground(U3DColors.background);
        setPreferredSize(new Dimension(40, 40));

        // Save button
        saveButton = new U3DToolItemLarge(new JLabel(" 💾"), " ", 0);
        add(saveButton);

        add(new U3DToolItemSeperator(32, U3DColors.forground));

        // Cut
        cutButton = new U3DToolItemLarge(new JLabel(" ✂️"), "Cut", 0);
        add(cutButton);

        // Copy
        copyButton = new U3DToolItemLarge(new JLabel("🔗"), "Copy", 0);
        add(copyButton);

        // Paste
        pasteButton = new U3DToolItemLarge(new JLabel("📋"), "Paste ", 0);
        add(pasteButton);

        add(new U3DToolItemSeperator(32, U3DColors.forground));
        
        // Play
        playButton = new U3DToolItemLarge(new JLabel(" ▶︎"), "Play ", 0);
        playButton.icon.setForeground(U3DColors.simpleGreenOld);
        add(playButton);

        // Pause
        pauseButton = new U3DToolItemLarge(new JLabel("❚❚"), "Pause ", 0);
        add(pauseButton);

        // Stop
        stopButton = new U3DToolItemLarge(new JLabel("◼"), "Stop ", 0);
        stopButton.icon.setForeground(U3DColors.lightRed);
        add(stopButton);

        add(new U3DToolItemSeperator(32, U3DColors.forground));

        // Settings
        settingsButton = new U3DButton("⚙️ Settings");
        settingsButton.setPreferredSize(new Dimension(70, 30));
        add(settingsButton);
    }
}
