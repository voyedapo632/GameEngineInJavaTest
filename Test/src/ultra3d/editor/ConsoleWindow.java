package ultra3d.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ultra3d.editor.ui.U3DButton;
import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DDockWindow;
import ultra3d.editor.ui.U3DScrollBarUI;

public class ConsoleWindow extends U3DDockWindow {
    private U3DEditor editor;
    private JPanel bodyPanel;
    private JScrollPane scrollPane;
    private JPanel lines;
    private GridBagConstraints gbc;
    private JPanel header;
    private U3DButton clearButton;

    public ConsoleWindow(U3DEditor editor) {
        super("Console", new JLabel("📟"));

        // Grid bag
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.ipady = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        // Header
        header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setPreferredSize(new Dimension(35, 35));
        header.setBackground(U3DColors.forground2);
        getContentPanel().add(header, BorderLayout.NORTH);

        // Clear button
        clearButton = new U3DButton("⌫ Clear");

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lines.removeAll();
                updateUI();
            }
        });

        header.add(clearButton);

        // Lines
        lines = new JPanel(new GridBagLayout());
        lines.setBackground(U3DColors.background2);
        
        // Body panel
        bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(U3DColors.background2);
        bodyPanel.add(lines, BorderLayout.NORTH);

        // Scroll pane
        scrollPane = new JScrollPane(bodyPanel);
        scrollPane.getVerticalScrollBar().setUI(new U3DScrollBarUI());
        scrollPane.getVerticalScrollBar().setBackground(null);
        scrollPane.getHorizontalScrollBar().setUI(new U3DScrollBarUI());
        scrollPane.setAutoscrolls(true);
        scrollPane.setBackground(U3DColors.background2);
        scrollPane.setBorder(null);
        getContentPanel().add(scrollPane, BorderLayout.CENTER);
    }

    public void writeLine(String message, Color color) {
        JLabel text = new JLabel(message);
        
        text.setForeground(color);
        lines.add(text, gbc, lines.getComponentCount());
        lines.validate();
        lines.updateUI();
        scrollPane.validate();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        updateUI();
    }
}