/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package ultra3d.framework;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import sr4j.Camera3D;
import ultra3d.util.U3DVector3f;

public class EditorCameraSystem extends U3DComponentSystem {
    private final Camera3D cam;
    private final JFrame window;
    private Point lastMouse;
    private boolean mouseDown;
    private boolean updateProperty = false;

    public EditorCameraSystem(U3DScene scene, U3DSceneManager sceneManager, JFrame window) {
        super(scene, sceneManager);
        cam = new Camera3D();
        this.window = window;
        lastMouse = new Point(0, 0);
        mouseDown = false;
    }

    @Override
     public void onStart() {
        scene.getViewport().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (!scene.keys.contains(keyCode)) {
                    scene.keys.add(keyCode);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (scene.keys.contains(KeyEvent.VK_S) && scene.keys.contains(KeyEvent.VK_CONTROL)) {
                    scene.saveScene();
                }

                if (scene.keys.contains(KeyEvent.VK_R) && scene.keys.contains(KeyEvent.VK_CONTROL)) {
                    scene.reload();
                }

                if (scene.keys.contains(KeyEvent.VK_C) && scene.keys.contains(KeyEvent.VK_CONTROL)) {
                    scene.clearCopiedEntities();
                    scene.copySelectedEntities();
                }

                if (scene.keys.contains(KeyEvent.VK_V) && scene.keys.contains(KeyEvent.VK_CONTROL)) {
                    scene.pasteCopiedEntities();
                }

                if (scene.keys.contains(KeyEvent.VK_D) && scene.keys.contains(KeyEvent.VK_CONTROL)) {
                    scene.duplicateSelected();
                }

                if (scene.keys.contains(KeyEvent.VK_X) && scene.keys.contains(KeyEvent.VK_CONTROL)) {
                    scene.cutSelectedEntities();
                }

                if (scene.keys.contains(KeyEvent.VK_DELETE) || scene.keys.contains(KeyEvent.VK_BACK_SPACE)) {
                    scene.deleteSelectedEntities();
                }

                if (scene.keys.contains(KeyEvent.VK_5)) {
                    scene.getGraphicsEngine().debugView = U3DGraphicsEngine.DEBUG_VIEW_FULL;
                }

                if (scene.keys.contains(KeyEvent.VK_6)) {
                    scene.getGraphicsEngine().debugView = U3DGraphicsEngine.DEBUG_VIEW_NORMAL_VISUALIZATION;
                }

                if (scene.keys.contains(KeyEvent.VK_7)) {
                    scene.getGraphicsEngine().debugView = U3DGraphicsEngine.DEBUG_VIEW_NORMAL_VISUALIZATION_WITH_TEXTURE;
                }

                for (int i = 0; i < scene.keys.size(); i++) {
                    Integer key = scene.keys.get(i);

                    if (key.equals(e.getKeyCode())) {
                        scene.keys.remove(i);
                    }
                }

            }
        });

        scene.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ((e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) && !scene.lastHovered.startsWith("TRANSFORM") && !scene.keys.contains(KeyEvent.VK_CONTROL)) {
                    mouseDown = true;
                }
                
                lastMouse = window.getMousePosition();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (updateProperty) {
                    scene.messageBox.appendMessage(U3DScene.MSG_COMPONENT_CHANGED);
                    updateProperty = false;
                }

                mouseDown = false;
            }
        });
    }

    @Override
    public void onUpdateBegin(float deltaTime) {
        if (mouseDown) {
            if (!window.getMousePosition().equals(lastMouse)) {
                scene.cameraMoved = true; 
                int dx = (int)window.getMousePosition().getX() - (int)lastMouse.getX();
                int dy = (int)window.getMousePosition().getY() - (int)lastMouse.getY();
                Robot robot = null;
                
                try {
                    robot = new Robot();
                }  catch (AWTException e) {
                    System.out.println(e);
                }
                
                if (robot != null) {
                    robot.mouseMove(window.getX() + (int)lastMouse.getX(), window.getY() + (int)lastMouse.getY());
                }
                
                lastMouse = window.getMousePosition();
                cam.yaw += dx * cam.cameraSencitivity;
                cam.pitch -= dy * cam.cameraSencitivity;
            }
        }
        
        if (scene.mouseDown) {
            if (window.getMousePosition() != null) {
                if (!window.getMousePosition().equals(lastMouse)) { 
                    scene.cameraMoved = true; 
                    int dx = (int)window.getMousePosition().getX() - (int)lastMouse.getX();
                    int dy = (int)window.getMousePosition().getY() - (int)lastMouse.getY();
                    
                    lastMouse = window.getMousePosition();
                    
                    for (String s : scene.getSelectedEntities()) {
                        U3DEntity entity = scene.getEntity(s);

                        if (entity == null) {
                            continue;
                        }

                        U3DComponent transform = entity.getComponent("Transform");
                        U3DVector3f translation = (U3DVector3f)transform.getComponentField("Translation").getValue();
                        U3DVector3f scale = (U3DVector3f)transform.getComponentField("Scale").getValue();
                        U3DVector3f rotation = (U3DVector3f)transform.getComponentField("Rotation").getValue();

                        switch (scene.lastHovered) {
                            case "TRANSFORM Translate X" -> {
                                translation.x += (dx + dy) * 0.035f;
                                updateProperty = true;
                            }
                            case "TRANSFORM Translate Y" -> {
                                translation.y -= (dx + dy) * 0.035f;
                                updateProperty = true;
                            }
                            case "TRANSFORM Translate Z" -> {
                                translation.z -= (dx + dy) * 0.035f;
                                updateProperty = true;
                            }
                            default -> {
                            }
                        }

                        switch (scene.lastHovered) {
                            case "TRANSFORM Scale X" -> {
                                scale.x += (dx + dy) * 0.035f;
                                if (scene.keys.contains(KeyEvent.VK_SHIFT)) {
                                    scale.y += (dx + dy) * 0.035f;
                                    scale.z += (dx + dy) * 0.035f;
                                }   updateProperty = true;
                            }
                            case "TRANSFORM Scale Y" -> {
                                scale.y -= (dx + dy) * 0.035f;
                                if (scene.keys.contains(KeyEvent.VK_SHIFT)) {
                                    scale.x -= (dx + dy) * 0.035f;
                                    scale.z -= (dx + dy) * 0.035f;
                                }   updateProperty = true;
                            }
                            case "TRANSFORM Scale Z" -> {
                                scale.z -= (dx + dy) * 0.035f;
                                if (scene.keys.contains(KeyEvent.VK_SHIFT)) {
                                    scale.y -= (dx + dy) * 0.035f;
                                    scale.x -= (dx + dy) * 0.035f;
                                }   updateProperty = true;
                            }
                            default -> {
                            }
                        }

                        switch (scene.lastHovered) {
                            case "TRANSFORM Rotation X" -> {
                                rotation.x += (dx + -dy) * 0.35f;
                                updateProperty = true;
                            }
                            case "TRANSFORM Rotation Y" -> {
                                rotation.y -= (dx + dy) * 0.35f;
                                updateProperty = true;
                            }
                            case "TRANSFORM Rotation Z" -> {
                                rotation.z -= (dx + dy) * 0.35f;
                                updateProperty = true;
                            }
                            default -> {
                            }
                        }
                    }
                }
            }
        }

        // Reset camera move event
        if (scene.mouseDown == false) {
            scene.cameraMoved = false;
        }

        if (!scene.keys.contains(KeyEvent.VK_CONTROL)) {
            if (scene.keys.contains(KeyEvent.VK_W)) {
                cam.moveForward(1);
            } else if (scene.keys.contains(KeyEvent.VK_S)) {
                cam.moveForward(-1);
            }

            if (scene.keys.contains(KeyEvent.VK_A)) {
                cam.moveRight(-1);
            } else if (scene.keys.contains(KeyEvent.VK_D)) {
                cam.moveRight(1);
            }

            if (scene.keys.contains(KeyEvent.VK_Q)) {
                cam.moveUp(-1);
            } else if (scene.keys.contains(KeyEvent.VK_E)) {
                cam.moveUp(1);
            }

            if (scene.keys.contains(KeyEvent.VK_DOWN)) {
                cam.turnUp(-5);
            } else if (scene.keys.contains(KeyEvent.VK_UP)) {
                cam.turnUp(5);
            }

            if (scene.keys.contains(KeyEvent.VK_LEFT)) {
                cam.turnRight(-5);
            } else if (scene.keys.contains(KeyEvent.VK_RIGHT)) {
                cam.turnRight(5);
            }

            if (scene.keys.contains(KeyEvent.VK_ESCAPE)) {
                scene.clearSelection();
            }
        }

        if (scene.keys.contains(KeyEvent.VK_CONTROL) && scene.keys.contains(KeyEvent.VK_A)) {
            scene.selectAllEntities();
        }
    }

    @Override
    public void onUpdateEnd(float deltaTime) {
        cam.update();
        scene.getGraphicsEngine().setViewMatrix(cam.viewMatrix);
    }

    @Override
    public void onEnd() {
        System.out.println("Editor Camera System has ended");
    }
}