package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class U3DToolItemLarge extends JButton {
    protected JLabel icon;
    public JLabel textLabel;
    protected int toolItemId;

    public U3DToolItemLarge(JLabel icon, String text, int toolItemId) {
        this.icon = icon;
        this.icon.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        this.icon.setForeground(U3DColors.text);
        this.toolItemId = toolItemId;
        
        textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setPreferredSize(new Dimension(32, 32));
        textLabel.setForeground(U3DColors.text);

        setBackground(null);
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BorderLayout());
        add(icon, BorderLayout.WEST);
        add(textLabel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(icon.getPreferredSize().width + 11 * text.length(), 32));
    }

    public int getToolItemId() {
        return toolItemId;
    }

    public void setToolItemId(int toolItemId) {
        this.toolItemId = toolItemId;
    }
}
