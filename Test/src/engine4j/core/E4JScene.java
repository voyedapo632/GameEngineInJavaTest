package engine4j.core;

import javax.swing.JPanel;

import engine4j.util.SafeList;
import softr4j.Camera3D;

public class E4JScene extends E4JObject {
    // Flags
    public static final int STATE_PAUSED = 0;
    public static final int STATE_RUNNING = 1;

    protected E4JSceneManager sceneManager;
    protected E4JSceneScript sceneScript;
    protected SafeList<E4JGameObject> gameObjects;
    protected Camera3D camera;
    protected JPanel viewportPanel;
    protected int state;

    public E4JScene(String id, E4JSceneManager sceneManager) {
        super(id);

        this.sceneManager = sceneManager;
        sceneScript = null;
        gameObjects = new SafeList<>();
        camera = new Camera3D();
        state = E4JScene.STATE_PAUSED;
    }

    public void setScript(E4JSceneScript sceneScript) {
        this.sceneScript = sceneScript;
        sceneScript.setParent(this);
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setSceneManager(E4JSceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public E4JSceneManager getSceneManager() {
        return sceneManager;
    }

    public SafeList<E4JGameObject> getGameObjects() {
        return gameObjects;
    }

    public Camera3D getCamera() {
        return camera;
    }

    // Game objects may have the same name but not the same id
    public void addGameObject(E4JGameObject gameObject) {
        if (!gameObjects.contains(gameObject)) {
            if (gameObject.parent.getId().equals("null")) {
                gameObject.setParent(this);
            }
            
            gameObjects.add(gameObject);
        }
    }

    public E4JGameObject getGameObjectById(String gameObjectId) {
        for (int i = 0; i < gameObjects.getSize(); i++) {
            E4JGameObject gameObject = gameObjects.get(i);

            if (gameObject != null) {
                if (gameObject.getId().equals(gameObjectId)) {
                    return gameObject;
                }
            }
        }

        return null;
    }

    public SafeList<E4JGameObject> getGameObjectsByName(String gameObjectName) {
        SafeList<E4JGameObject> gotGameObjects = new SafeList<>();

        for (int i = 0; i < gameObjects.getSize(); i++) {
            E4JGameObject gameObject = gameObjects.get(i);

            if (gameObject != null) {
                if (gameObject.getName().equals(gameObjectName)) {
                    gotGameObjects.add(gameObject);
                }
            }
        }

        return null;
    }

    public void removeGameObject(E4JGameObject gameObject) {
        int index = gameObjects.indexOf(gameObject);

        if (index >= 0) {
            gameObjects.remove(index);
        }
    }

    public void removeGameObject(String gameObjectId) {
        for (int i = 0; i < gameObjects.getSize(); i++) {
            E4JGameObject gameObject = gameObjects.get(i);

            if (gameObject != null) {
                if (gameObject.getId().equals(gameObjectId)) {
                    gameObjects.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public String toString() { 
        String result = String.format("<%s id=\"%s\" parent=\"%s\">\n", getType(), id, parent.getId());

        for (int i = 0; i < gameObjects.getSize(); i++) {
            String componentsResults = "";

            for (String line : gameObjects.get(i).toString().split("\n")) {
                componentsResults += "    " + line + '\n';
            }

            result += componentsResults;
        }

        return result + String.format("</%s>", getType());
    }

    public void start() {
        if (sceneScript != null) {
            sceneScript.onStart();
        }

        for (int i = 0; i < gameObjects.getSize(); i++) {
            E4JGameObject g = gameObjects.get(i);

            if (g != null) {
                g.start();
            }
        }
    }

    public void update(float deltaTime) {
        if (sceneScript != null) {
            sceneScript.onUpdate(deltaTime);
        }

        for (int i = 0; i < gameObjects.getSize(); i++) {
            E4JGameObject g = gameObjects.get(i);

            if (g != null) {
                g.update(deltaTime);
            }
        }
    }

    public void exit() {
        if (sceneScript != null) {
            sceneScript.onExit();
        }

        for (int i = 0; i < gameObjects.getSize(); i++) {
            E4JGameObject g = gameObjects.get(i);

            if (g != null) {
                g.exit();
            }
        }
    }
}
