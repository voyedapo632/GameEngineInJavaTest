package engine4j.core;

public class E4JSceneScript extends E4JScript {
    protected E4JScene scene;
    protected E4JSceneManager sceneManager;
    protected E4JRenderContext renderContext;

    public E4JSceneScript(String id) {
        super(id);
    }

    @Override
    public void onStart() { 
        scene = (E4JScene)getParent();
        sceneManager = scene.getSceneManager();
        renderContext = sceneManager.getRenderContext();
    }

    @Override
    public void onUpdate(float deltatime) {

    }

    @Override
    public void onExit() {

    }
}
