/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package u3dprojects.MyProject1.source.systems;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import sr4j.Camera3D;
import ultra3d.framework.U3DComponentSystem;
import ultra3d.framework.U3DGraphicsEngine;
import ultra3d.framework.U3DScene;
import ultra3d.framework.U3DSceneManager;

public class DefaultCameraSystem extends U3DComponentSystem {
    private final Camera3D cam;
    private final JFrame window;
    private Point lastMouse;

    public DefaultCameraSystem(U3DScene scene, U3DSceneManager sceneManager, JFrame window) {
        super(scene, sceneManager);
        cam = new Camera3D();
        this.window = window;
        lastMouse = new Point(0, 0);
    }

    @Override
     public void onStart() {
        scene.getViewport().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (!scene.getKeys().contains(keyCode)) {
                    scene.getKeys().add(keyCode);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (scene.getKeys().contains(KeyEvent.VK_R) && scene.getKeys().contains(KeyEvent.VK_CONTROL)) {
                    scene.reload();
                }

                if (scene.getKeys().contains(KeyEvent.VK_5)) {
                    scene.getGraphicsEngine().debugView = U3DGraphicsEngine.DEBUG_VIEW_FULL;
                }

                if (scene.getKeys().contains(KeyEvent.VK_6)) {
                    scene.getGraphicsEngine().debugView = U3DGraphicsEngine.DEBUG_VIEW_NORMAL_VISUALIZATION;
                }

                if (scene.getKeys().contains(KeyEvent.VK_7)) {
                    scene.getGraphicsEngine().debugView = U3DGraphicsEngine.DEBUG_VIEW_NORMAL_VISUALIZATION_WITH_TEXTURE;
                }

                for (int i = 0; i < scene.getKeys().size(); i++) {
                    Integer key = scene.getKeys().get(i);

                    if (key.equals(e.getKeyCode())) {
                        scene.getKeys().remove(i);
                    }
                }

            }
        });

        scene.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    scene.mouseDown = true;
                }
                
                lastMouse = window.getMousePosition();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                scene.mouseDown = false;
            }
        });
    }

    @Override
    public void onUpdateBegin(float deltaTime) {
        if (scene.mouseDown) {
            if (!window.getMousePosition().equals(lastMouse)) { 
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
                //System.out.println("END: " + e.getPoint());
                cam.yaw += dx * cam.cameraSencitivity;
                cam.pitch -= dy * cam.cameraSencitivity;
            }
        }

        if (!scene.getKeys().contains(KeyEvent.VK_CONTROL)) {
            if (scene.getKeys().contains(KeyEvent.VK_W)) {
                cam.moveForward(1);
            } else if (scene.getKeys().contains(KeyEvent.VK_S)) {
                cam.moveForward(-1);
            }

            if (scene.getKeys().contains(KeyEvent.VK_A)) {
                cam.moveRight(-1);
            } else if (scene.getKeys().contains(KeyEvent.VK_D)) {
                cam.moveRight(1);
            }

            if (scene.getKeys().contains(KeyEvent.VK_Q)) {
                cam.moveUp(-1);
            } else if (scene.getKeys().contains(KeyEvent.VK_E)) {
                cam.moveUp(1);
            }

            if (scene.getKeys().contains(KeyEvent.VK_DOWN)) {
                cam.turnUp(-1);
            } else if (scene.getKeys().contains(KeyEvent.VK_UP)) {
                cam.turnUp(1);
            }

            if (scene.getKeys().contains(KeyEvent.VK_ESCAPE)) {
                scene.clearSelection();
            }
        }

        if (scene.getKeys().contains(KeyEvent.VK_CONTROL) && scene.getKeys().contains(KeyEvent.VK_A)) {
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
