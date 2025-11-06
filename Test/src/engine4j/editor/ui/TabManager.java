package engine4j.editor.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

public class TabManager extends JPanel {
    public TabManager() {
        this.setLayout(new BorderLayout());

        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                validate();
            }
        });
    }
}
