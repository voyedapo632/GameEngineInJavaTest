/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package ultra3d.framework;

public class U3DComponentSystem implements U3DEventInterface {
    protected U3DScene scene;
    protected U3DSceneManager sceneManager;

    public U3DComponentSystem(U3DScene scene, U3DSceneManager sceneManager) {
        this.scene = scene;
        this.sceneManager = sceneManager;
    }

    @Override
    public void onStart() { }

    @Override
    public void onValidated() { }

    @Override
    public void onUpdateBegin(float deltaTime) { }

    @Override
    public void onUpdateEnd(float deltaTime) { }

    @Override
    public void onEnd() { }
}
