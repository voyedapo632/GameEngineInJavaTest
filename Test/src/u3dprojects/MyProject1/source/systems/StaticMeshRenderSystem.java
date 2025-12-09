/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package u3dprojects.MyProject1.source.systems;

import java.util.Collection;

import sr4j.Matrix4x4;
import ultra3d.framework.U3DComponent;
import ultra3d.framework.U3DComponentSystem;
import ultra3d.framework.U3DEntity;
import ultra3d.framework.U3DScene;
import ultra3d.framework.U3DSceneManager;
import ultra3d.util.U3DVector3f;

public class StaticMeshRenderSystem extends U3DComponentSystem {
    private Collection<U3DEntity> entities;

    public StaticMeshRenderSystem(U3DScene scene, U3DSceneManager sceneManager) {
        super(scene, sceneManager);
    }

    @Override
     public void onStart() {
        entities = scene.queryEntities(new String[] { "Transform", "Static Mesh", "Texture" });
    }

    @Override
    public void onValidated() {
        entities = scene.queryEntities(new String[] { "Transform", "Static Mesh", "Texture" });
    }

    @Override
    public void onUpdateBegin(float deltaTime) {
        entities.forEach(entity -> {
            U3DComponent transform = entity.getComponent("Transform");
            U3DVector3f translation = (U3DVector3f)transform.getComponentField("Translation").getValue();
            U3DVector3f scale = (U3DVector3f)transform.getComponentField("Scale").getValue();
            U3DVector3f rotation = (U3DVector3f)transform.getComponentField("Rotation").getValue();

            U3DComponent staticMesh = entity.getComponent("Static Mesh");
            String source = (String)staticMesh.getComponentField("Source").getValue();

            U3DComponent texture = entity.getComponent("Texture");
            String albedo = (String)texture.getComponentField("Albedo").getValue();

            Matrix4x4 model = Matrix4x4.translation(translation.x, translation.y, translation.z);
            
            model = Matrix4x4.mul(model, Matrix4x4.rotateX(rotation.x));
            model = Matrix4x4.mul(model, Matrix4x4.rotateY(rotation.y));
            model = Matrix4x4.mul(model, Matrix4x4.rotateZ(rotation.z));
            model = Matrix4x4.mul(model, Matrix4x4.scale(scale.x, scale.y, scale.z));

            scene.getGraphicsEngine().setModelMatrix(model);
            scene.getGraphicsEngine().setTexture(sceneManager.getTexture(albedo));
            scene.getGraphicsEngine().drawOBJModel(sceneManager.getObjectModel(source));
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

