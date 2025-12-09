package ultra3d.framework;

public class U3DScript extends U3DObject implements U3DEventInterface {
    protected U3DSceneManager sceneManager;

    public U3DScript(U3DSceneManager sceneManager) {
        super();
        this.id = getClass().getSimpleName();
        this.sceneManager = sceneManager;
    }

    public String getScriptId() {
        return id;
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

