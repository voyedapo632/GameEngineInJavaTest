package ultra3d.framework;

import java.util.Collection;

import ultra3d.util.U3DVector3f;

public class U3DPhysicsComponentSystem extends U3DComponentSystem {
    private Collection<U3DEntity> entities;

    public U3DPhysicsComponentSystem(U3DScene scene, U3DSceneManager sceneManager) {
        super(scene, sceneManager);
    }

    @Override
     public void onStart() {
        entities = scene.queryEntities(new String[] { "Transform", "Physics" });
    }

    @Override
    public void onValidated() {
        entities = scene.queryEntities(new String[] { "Transform", "Physics" });
    }

    @Override
    public void onUpdateBegin(float deltaTime) {
        entities.forEach(entity -> {
            if (scene.playMode == U3DScene.PLAY_MODE_PLAYING) {
                U3DComponent transform = entity.getComponent("Transform");
                U3DVector3f translation = (U3DVector3f)transform.getComponentField("Translation").getValue();
                
                U3DComponent physics = entity.getComponent("Physics");
                U3DField<Float> gravity = (U3DField<Float>)physics.getComponentField("Gravity");

                translation.y -= gravity.getValue();
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
