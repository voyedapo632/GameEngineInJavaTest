/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package ultra3d.framework;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import sr4j.Matrix4x4;
import ultra3d.util.U3DVector3f;

public class StaticMeshComponentSystem extends U3DComponentSystem {
    private Collection<U3DEntity> entities;

    public StaticMeshComponentSystem(U3DScene scene, U3DSceneManager sceneManager) {
        super(scene, sceneManager);
    }

    @Override
     public void onStart() {
        entities = scene.queryEntities(new String[] { "Transform", "Static Mesh", "Texture" });

        // Add click listener
        scene.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                

                scene.mouseDown = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && !scene.cameraMoved) {
                    if (!scene.keys.contains(KeyEvent.VK_SHIFT) && !scene.lastHovered.startsWith("TRANSFORM") && !scene.keys.contains(KeyEvent.VK_CONTROL)) {
                        scene.clearSelection();
                    }
                    
                    if (!scene.getSelectedEntities().contains(scene.lastHovered)) {
                        if (!scene.lastHovered.startsWith("TRANSFORM") && scene.entities.containsKey(scene.lastHovered)) {
                            scene.addSelection(scene.lastHovered);
                        }
                    } else {
                        scene.removeSelection(scene.lastHovered);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    if (!scene.lastHovered.startsWith("TRANSFORM") && scene.entities.containsKey(scene.lastHovered)) {
                        scene.addSelection(scene.lastHovered);
                    }
                }
                
                if (scene.lastHovered.startsWith("TRANSFORM")) {
                    scene.lastHovered = "";
                }

                scene.mouseDown = false;
            }
        });
    }

    @Override
    public void onValidated() {
        entities = scene.queryEntities(new String[] { "Transform", "Static Mesh", "Texture", "Entity Misc"});
    }

    @Override
    public void onUpdateBegin(float deltaTime) {
        entities.forEach(entity -> {
            U3DComponent entityMisc = entity.getComponent("Entity Misc");
            Boolean isVisible = (Boolean)entityMisc.getComponentField("Is Visible").getValue();

            if (isVisible) {
                U3DComponent transform = entity.getComponent("Transform");
                U3DVector3f translation = (U3DVector3f)transform.getComponentField("Translation").getValue();
                U3DVector3f scale = (U3DVector3f)transform.getComponentField("Scale").getValue();
                U3DVector3f rotation = (U3DVector3f)transform.getComponentField("Rotation").getValue();

                U3DComponent staticMesh = entity.getComponent("Static Mesh");
                String source = (String)staticMesh.getComponentField("Source").getValue();

                U3DComponent texture = entity.getComponent("Texture");
                String albedo = (String)texture.getComponentField("Albedo").getValue();

                Matrix4x4 model = Matrix4x4.translation(translation.x, translation.y, translation.z);
                
                model = Matrix4x4.mul(model, Matrix4x4.rotateX((float)Math.toRadians(rotation.x)));
                model = Matrix4x4.mul(model, Matrix4x4.rotateY((float)Math.toRadians(rotation.y)));
                model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(rotation.z)));
                model = Matrix4x4.mul(model, Matrix4x4.scale(scale.x, scale.y, scale.z));

                scene.getGraphicsEngine().setModelMatrix(model);
                scene.getGraphicsEngine().setTexture(sceneManager.getTexture(albedo));
                
                if (scene.lastHovered.equals(entity.getEntityId())) {
                    scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 0.5f, 0.0f));
                    scene.getGraphicsEngine().drawOBJModelSelected(sceneManager.getObjectModel(source));

                    if (scene.getKeys().contains(KeyEvent.VK_CONTROL) && scene.mouseDown) {
                        scene.addSelection(entity.getEntityId());
                    }
                } else if (scene.getSelectedEntities().contains(entity.getEntityId())) {
                    scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 0.75f, 0.0f));
                    scene.getGraphicsEngine().drawOBJModelSelected(sceneManager.getObjectModel(source));
                } else {
                    scene.getGraphicsEngine().drawOBJModel(sceneManager.getObjectModel(source));
                }

                if (!scene.lastHovered.startsWith("TRANSFORM")) {
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        scene.lastHovered = entity.getEntityId();
                    } else if (scene.lastHovered.equals( entity.getEntityId())) {
                        scene.lastHovered = "";
                    }
                }
            }
        });
    }

    @Override
    public void onUpdateEnd(float deltaTime) {
    }

    @Override
    public void onEnd() {
        System.out.println("Static Mesh System has ended");
    }
}
