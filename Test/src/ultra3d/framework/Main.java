/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package ultra3d.framework;

import java.awt.BorderLayout;

import engine4j.util.GameWindow;

class Window extends GameWindow {
    U3DSceneManager sceneManager;
    String scene1Id;
    U3DScene scene1;

    public Window(int width, int height, String title) {
        super(width, height, title);
    }

    @Override
    protected void onInit() {
        sceneManager = new U3DSceneManager();
        scene1Id = "src\\main\\java\\u3dprojects\\MyProject1\\scenes\\scene1.xml";
        sceneManager.loadScene(scene1Id);
        scene1 = sceneManager.getScene(scene1Id);
        scene1.addComponentSystem(new EditorCameraSystem(scene1, sceneManager, this));
        scene1.addComponentSystem(new StaticMeshComponentSystem(scene1, sceneManager));
        scene1.addComponentSystem(new TransformGizmoSystem(scene1, sceneManager));
        setLayout(new BorderLayout());
        add(scene1.getViewport(), BorderLayout.CENTER);
        scene1.getViewport().requestFocus();
        validate();
        
        // Start
        loadGameAssets();
        scene1.onStart();

        // scene1.getGraphicsEngine().setResolution(1280, 720);
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
    }
}

public class Main {
    public static void main(String[] args) {
        Window gameTest = new Window(1200, 700, "Test");
        gameTest.start((long)(1000.0 / 1024.0)); // 1024 FPS
    }
}