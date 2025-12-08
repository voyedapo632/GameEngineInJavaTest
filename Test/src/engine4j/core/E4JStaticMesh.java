package engine4j.core;

public class E4JStaticMesh extends E4JGameObject {
    public E4JStaticMesh(String id, String name) {
        super(id, name);
    }

    @Override
    public void intitializeComponents() {
        E4JComponent staticMeshComponent = new E4JComponent("Static Mesh");
        
        staticMeshComponent.addAttribute(new E4JAttribute("Mesh Source", "Default Mesh.obj", E4JAttribute.EDIT_ANYWARE));
        staticMeshComponent.addAttribute(new E4JAttribute("Texture Source", "Default Texture.png", E4JAttribute.EDIT_ANYWARE));
        staticMeshComponent.addAttribute(new E4JAttribute("Color", new E4JVector3(), E4JAttribute.EDIT_ANYWARE));
        addComponent(staticMeshComponent);

        // Initialize base components
        super.intitializeComponents();
    }

    @Override
    public void start() { }

    @Override
    public void update(float deltaTime) { }
}
