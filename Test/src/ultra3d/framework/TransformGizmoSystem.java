package ultra3d.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import sr4j.Matrix4x4;
import ultra3d.util.U3DVector3f;

public class TransformGizmoSystem extends U3DComponentSystem {
    public TransformGizmoSystem(U3DScene scene, U3DSceneManager sceneManager) {
        super(scene, sceneManager);
    }

    @Override
    public void onStart() {
        scene.getViewport().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_1:
                        scene.selectionMode = U3DScene.SELECTION_MODE_POINTER;
                        break;
                    case KeyEvent.VK_2:
                        scene.selectionMode = U3DScene.SELECTION_MODE_TRANSLATION;
                        break;
                    case KeyEvent.VK_3:
                        scene.selectionMode = U3DScene.SELECTION_MODE_SCALE;
                        break;
                    case KeyEvent.VK_4:
                        scene.selectionMode = U3DScene.SELECTION_MODE_ROTATION;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onUpdateBegin(float deltaTime) {
        if (!scene.getSelectedEntities().isEmpty()) {
            U3DEntity entity = scene.getEntity((String)scene.getSelectedEntities().getLast());
            
            if (entity == null || scene.keys.contains(KeyEvent.VK_CONTROL)) {
                return;
            }

            U3DComponent transform = entity.getComponent("Transform");
            U3DVector3f translation = (U3DVector3f)transform.getComponentField("Translation").getValue();
            U3DVector3f scale = (U3DVector3f)transform.getComponentField("Scale").getValue();
            Matrix4x4 model;

            switch (scene.selectionMode) {
                case U3DScene.SELECTION_MODE_TRANSLATION -> {
                    scene.getGraphicsEngine().setTexture(null);
                    model = Matrix4x4.translation(translation.x  + scale.x, translation.y  - scale.y, translation.z  + scale.z);
                    model = Matrix4x4.mul(model, Matrix4x4.scale(0.2f, 0.2f, 0.2f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateX(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateY(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ(0.0f));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 1.0f));
                    scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("TriangulatedCubeTest.obj"));
                    model = Matrix4x4.translation(translation.x  + scale.x, translation.y  - scale.y, translation.z  + scale.z);
                    model = Matrix4x4.mul(model, Matrix4x4.scale(0.1f, 0.1f, 0.1f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateX(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateY(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ(0.0f));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    // X - Axis
                    if (scene.lastHovered.startsWith("TRANSFORM Translate X")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 0.0f, 0.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Translation Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Translate Y") && !scene.lastHovered.equals("TRANSFORM Translate Z")) {
                            scene.lastHovered = "TRANSFORM Translate X";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Translate X") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }   // Y - Axis
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(90.0)));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    if (scene.lastHovered.startsWith("TRANSFORM Translate Y")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(0.0f, 1.0f, 0.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Translation Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Translate X") && !scene.lastHovered.equals("TRANSFORM Translate Z")) {
                            scene.lastHovered = "TRANSFORM Translate Y";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Translate Y") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }   // Y - Axis
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(90.0)));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateY((float)Math.toRadians(90.0)));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    if (scene.lastHovered.startsWith("TRANSFORM Translate Z")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(0.0f, 0.0f, 1.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Translation Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Translate X") && !scene.lastHovered.equals("TRANSFORM Translate Y")) {
                            scene.lastHovered = "TRANSFORM Translate Z";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Translate Z") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }
                }
                case U3DScene.SELECTION_MODE_SCALE -> {
                    scene.getGraphicsEngine().setTexture(null);
                    model = Matrix4x4.translation(translation.x  + scale.x, translation.y  - scale.y, translation.z  + scale.z);
                    model = Matrix4x4.mul(model, Matrix4x4.scale(0.2f, 0.2f, 0.2f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateX(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateY(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ(0.0f));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 1.0f));
                    scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("TriangulatedCubeTest.obj"));
                    model = Matrix4x4.translation(translation.x  + scale.x, translation.y  - scale.y, translation.z  + scale.z);
                    model = Matrix4x4.mul(model, Matrix4x4.scale(0.1f, 0.1f, 0.1f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateX(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateY(0.0f));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ(0.0f));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    // X - Axis
                    if (scene.lastHovered.startsWith("TRANSFORM Scale X")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 0.0f, 0.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Scale Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Scale Y") && !scene.lastHovered.equals("TRANSFORM Scale Z")) {
                            scene.lastHovered = "TRANSFORM Scale X";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Scale X") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }   // Y - Axis
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(90.0)));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    if (scene.lastHovered.startsWith("TRANSFORM Scale Y")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(0.0f, 1.0f, 0.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Scale Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Scale X") && !scene.lastHovered.equals("TRANSFORM Scale Z")) {
                            scene.lastHovered = "TRANSFORM Scale Y";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Scale Y") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }   // Y - Axis
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(90.0)));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateY((float)Math.toRadians(90.0)));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    if (scene.lastHovered.startsWith("TRANSFORM Scale Z")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(0.0f, 0.0f, 1.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Scale Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Scale X") && !scene.lastHovered.equals("TRANSFORM Scale Y")) {
                            scene.lastHovered = "TRANSFORM Scale Z";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Scale Z") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }
                }
                case U3DScene.SELECTION_MODE_ROTATION -> {
                    model = Matrix4x4.translation(translation.x  + scale.x, translation.y  - scale.y, translation.z  + scale.z);
                    model = Matrix4x4.mul(model, Matrix4x4.scale(3.0f, 3.0f, 3.0f));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    scene.getGraphicsEngine().setTexture(null);
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(-90.0)));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    // X - Axis
                    if (scene.lastHovered.startsWith("TRANSFORM Rotation X")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 0.0f, 0.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Rotation Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Rotation Y") && !scene.lastHovered.equals("TRANSFORM Rotation Z")) {
                            scene.lastHovered = "TRANSFORM Rotation X";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Rotation X") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }   // Y - Axis
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(90.0)));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(180.0)));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    if (scene.lastHovered.startsWith("TRANSFORM Rotation Y")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(0.0f, 1.0f, 0.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Rotation Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Rotation X") && !scene.lastHovered.equals("TRANSFORM Rotation Z")) {
                            scene.lastHovered = "TRANSFORM Rotation Y";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Rotation Y") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }   // Z - Axis
                    model = Matrix4x4.mul(model, Matrix4x4.rotateZ((float)Math.toRadians(90.0)));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateY((float)Math.toRadians(0.0)));
                    model = Matrix4x4.mul(model, Matrix4x4.rotateX((float)Math.toRadians(90.0)));
                    scene.getGraphicsEngine().setModelMatrix(model);
                    if (scene.lastHovered.startsWith("TRANSFORM Rotation Z")) {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(1.0f, 1.0f, 0.0f));
                    } else {
                        scene.getGraphicsEngine().setColor(new U3DVector3f(0.0f, 0.0f, 1.0f));
                    }   scene.getGraphicsEngine().drawOBJModelUnlit(sceneManager.getObjectModel("U3D Rotation Gizmo.obj"));
                    if (scene.getGraphicsEngine().getMessageBox().hasMessage("HOVERED")) {
                        if (!scene.lastHovered.equals("TRANSFORM Rotation X") && !scene.lastHovered.equals("TRANSFORM Rotation Y")) {
                            scene.lastHovered = "TRANSFORM Rotation Z";
                        }
                    } else if (scene.lastHovered.equals("TRANSFORM Rotation Z") && !scene.mouseDown) {
                        scene.lastHovered = "";
                    }
                }
                default -> {
                }
            }
        }
    }

    @Override
    public void onUpdateEnd(float deltaTime) { }

    @Override
    public void onEnd() {
        System.out.println("Static Mesh System has ended");
    }
}
