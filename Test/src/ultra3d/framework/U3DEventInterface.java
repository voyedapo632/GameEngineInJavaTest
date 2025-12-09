package ultra3d.framework;

public interface U3DEventInterface {
    public void onStart();
    public void onValidated();
    public void onUpdateBegin(float deltaTime);
    public void onUpdateEnd(float deltaTime);
    public void onEnd();
}