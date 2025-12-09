package ultra3d.editor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class U3DPropertyContainer extends JPanel {
    protected JPanel column1;
    protected JPanel column2;
    protected JLabel nameLabel;
    protected String name;
    protected int parentWidth;

    public void setParentWidth(int width) {
        if (width >= 350) {
            parentWidth = width;
        }
    }

    public int getParentWidth() {
        return parentWidth;
    }

    public U3DPropertyContainer(String name) {
        super(new BorderLayout());
        this.name = name;
        parentWidth = 350;
        setBackground(U3DColors.forground);
        setPreferredSize(new Dimension(30, 30));

        column1 = new JPanel(new GridLayout(1, 3, 2, 2));
        column1.setPreferredSize(new Dimension((int)(parentWidth / 2.0), 30));
        column1.setBackground(null);
        column1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 2, 0, U3DColors.background),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        nameLabel = new JLabel(name);
        nameLabel.setForeground(U3DColors.text);
        column1.add(nameLabel);

        add(column1, BorderLayout.WEST);
        
        column2 = new JPanel(new GridLayout(1, 3, 2, 2));
        column2.setBackground(null);
        column2.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 2, 0, U3DColors.background),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        add(column2, BorderLayout.CENTER);
    }
}