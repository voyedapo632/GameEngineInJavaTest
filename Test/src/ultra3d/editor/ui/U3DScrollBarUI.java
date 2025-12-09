package ultra3d.editor.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Scrollbar;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class U3DScrollBarUI extends BasicScrollBarUI {
    @Override
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        c.setBackground(null);
        //c.setSize(10, c.getHeight());
        //c.setBounds(c.getX() + 10, c.getY(), c.getWidth(), c.getHeight());
    }

    @Override
    public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(U3DColors.forground4);

        if (scrollbar.getOrientation() == Scrollbar.VERTICAL) {
            g.fillRoundRect(thumbBounds.x + 4, thumbBounds.y, 8, thumbBounds.height, 6, 8);
        } else {
            g.fillRoundRect(thumbBounds.x, thumbBounds.y + 4, thumbBounds.width, 8, 6, 8);
        }
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton btn = new JButton();
        btn.setVisible(false);
        return btn;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton btn = new JButton();
        btn.setVisible(false);
        return btn;
    }
}