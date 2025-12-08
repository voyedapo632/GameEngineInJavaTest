package engine4j.core;

public class E4JSystem extends E4JObject {
    public E4JSystem() {
        super("Default System");
    }

    public E4JSystem(String id) {
        super(id);
    }

    public E4JSystem checkForValidation(E4JGameObject caller) {
        if (caller.hasComponent("Static Mesh")) {
            return this;
        }

        return null;
    }

    public void onCreate() { }
    public void onStartRunning() { }
    public void onUpdate(float deltaTime) { }
    public void onStopRunning() { }
    public void onDestroy() { }
}
