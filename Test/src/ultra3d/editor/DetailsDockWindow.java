package ultra3d.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ultra3d.editor.ui.U3DCheckBoxProperty;
import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DComponentPropertyGroup;
import ultra3d.editor.ui.U3DDockWindow;
import ultra3d.editor.ui.U3DPropertyViewPanel;
import ultra3d.editor.ui.U3DStringProperty;
import ultra3d.editor.ui.U3DVec3Property;
import ultra3d.framework.U3DComponent;
import ultra3d.framework.U3DEntity;
import ultra3d.framework.U3DField;
import ultra3d.framework.U3DScene;
import ultra3d.util.U3DVector3f;

public class DetailsDockWindow extends U3DDockWindow {
    private U3DEditor editor;
    public U3DPropertyViewPanel propertiesPanel;
    private JLabel selectEntitiesLabel;
    public U3DScene lastScene;

    public DetailsDockWindow(U3DEditor editor) {
        super("Details", new JLabel("✎"));
        this.editor = editor;
        
        selectEntitiesLabel = new JLabel("Selected an entity to view details", JLabel.CENTER);
        selectEntitiesLabel.setForeground(U3DColors.text2);
        selectEntitiesLabel.setPreferredSize(new Dimension(60, 60));

        propertiesPanel = new U3DPropertyViewPanel();
        validateDetailsPanel(null);
    }

    public final void validateDetailsPanel(U3DScene scene) {
        lastScene = scene;
        
        if (scene == null || scene.getSelectedEntities().isEmpty()) {
            getContentPanel().remove(propertiesPanel);
            getContentPanel().add(selectEntitiesLabel, BorderLayout.NORTH);
        } else {
            getContentPanel().remove(selectEntitiesLabel);
            getContentPanel().add(propertiesPanel, BorderLayout.CENTER);
            propertiesPanel.removePropertyGroups();
            propertiesPanel.contentPanel.removeAll();
            
            for (String id : scene.getSelectedEntities()) {
                U3DEntity entity = scene.getEntity(id);

                if (entity == null) {
                    continue;
                }

                JPanel header = new JPanel(new BorderLayout());
                header.setPreferredSize(new Dimension(30, 30));
                header.setBackground(U3DColors.forground);

                JLabel headerText = new JLabel("  " + entity.getEntityId());
                headerText.setForeground(U3DColors.text);
                header.add(headerText, BorderLayout.WEST);

                propertiesPanel.contentPanel.add(header, propertiesPanel.gbc, propertiesPanel.contentPanel.getComponentCount());
                
                for (int i = 0; i < entity.getComponents().size(); i++) {
                    U3DComponent component = (U3DComponent)entity.getComponents().toArray()[i];
                    U3DComponentPropertyGroup propertyGroup = new U3DComponentPropertyGroup(component.getComponentId(), component, this);

                    for (U3DField<?> field : component.getComponentFields()) {
                        if (field.getValue() instanceof U3DVector3f) {
                            U3DVec3Property p = new U3DVec3Property(field, field.getFieldId());
                            propertyGroup.addProperty(p);
                        } else if (field.getValue() instanceof String) {
                            U3DStringProperty p = new U3DStringProperty(field, field.getFieldId());
                            propertyGroup.addProperty(p);
                        } else if (field.getValue() instanceof Boolean) {
                            U3DCheckBoxProperty p = new U3DCheckBoxProperty(field, field.getFieldId());
                            propertyGroup.addProperty(p);
                        }
                    }

                    propertyGroup.doExpand();
                    propertiesPanel.addPropertyGroup(propertyGroup);
                }
            }
        }
    }
}
