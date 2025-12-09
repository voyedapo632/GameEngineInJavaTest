/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package u3dprojects.MyProject1.source;

import java.awt.BorderLayout;

import engine4j.util.GameWindow;
import u3dprojects.MyProject1.source.systems.DefaultCameraSystem;
import u3dprojects.MyProject1.source.systems.StaticMeshRenderSystem;
import ultra3d.framework.U3DScene;
import ultra3d.framework.U3DSceneManager;

class Game extends GameWindow {
    U3DSceneManager sceneManager;
    String scene1Id;
    U3DScene scene1;

    public Game(int width, int height, String title) {
        super(width, height, title);
    }

    @Override
    protected void onInit() {
        sceneManager = new U3DSceneManager();
        scene1Id = "src\\main\\java\\u3dprojects\\MyProject1\\scenes\\scene1.xml";
        sceneManager.loadScene(scene1Id);
        scene1 = sceneManager.getScene(scene1Id);
        scene1.addComponentSystem(new DefaultCameraSystem(scene1, sceneManager, this));
        scene1.addComponentSystem(new StaticMeshRenderSystem(scene1, sceneManager));
        setLayout(new BorderLayout());
        add(scene1.getViewport(), BorderLayout.CENTER);
        scene1.getViewport().requestFocus();
        validate();
        
        // Start
        loadGameAssets();
        scene1.onStart();

        scene1.getGraphicsEngine().setResolution(1280, 720);
    }

    @Override
    protected void onTick() {
        scene1.onUpdateBegin(0);
        scene1.onUpdateEnd(0);
    }

    @Override
    protected void onResized() {
        scene1.getGraphicsEngine().validateEngine();
    }

    @Override
    protected void onDestroy() {
        scene1.onEnd();
    }

    private void loadGameAssets() {
        sceneManager.loadTextureFile("C:\\Users\\kmhjh\\OneDrive\\Pictures\\dfdsfds.png");
        sceneManager.loadTextureFile("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fsdfdfsdfsdf.png");
        sceneManager.loadTextureFile("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fdsfsdfdss.png");
        sceneManager.loadOBJFile("C:\\Users\\kmhjh\\OneDrive\\Desktop\\TriangulatedCubeTest.obj");
        sceneManager.loadOBJFile("C:\\Users\\kmhjh\\OneDrive\\Desktop\\TriangulatedSphere.obj");
        sceneManager.loadOBJFile("C:\\Users\\kmhjh\\OneDrive\\Desktop\\untitledgasdasdasdfasdsdfsdfsdffsdfdsfsdfsdfsdfsdfdfsdf.obj");
        sceneManager.loadOBJFile("C:\\Users\\kmhjh\\OneDrive\\Desktop\\U3D Translation Gizmo.obj");
        sceneManager.loadOBJFile("C:\\Users\\kmhjh\\OneDrive\\Desktop\\U3D Scale Gizmo.obj");
        sceneManager.loadOBJFile("C:\\Users\\kmhjh\\OneDrive\\Desktop\\U3D Rotation Gizmo.obj");
    }
}
