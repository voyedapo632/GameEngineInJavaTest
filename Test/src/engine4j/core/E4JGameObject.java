package engine4j.core;

import engine4j.util.SafeList;

public class E4JGameObject extends E4JObject {
    protected String name;
    protected SafeList<E4JComponent> components;
    protected SafeList<E4JSystem> systems;

    public E4JGameObject(String id, String name) {
        super(id);
        this.name = name;
        components = new SafeList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addComponent(E4JComponent component) {
        if (!components.contains(component)) {
            component.setParent(this);
            components.add(component);
        }
    }

    public E4JComponent getComponent(String id) {
        for (int i = 0; i < components.getSize(); i++) {
            E4JComponent component = components.get(i);

            if (component.getId().equals(id)) {
                return component;
            }
        }

        return null;
    }

    public void removeComponent(String id) {
        for (int i = 0; i < components.getSize(); i++) {
            E4JComponent component = components.get(i);

            if (component != null) {
                if (component.getId().equals(id)) {
                    components.remove(i);
                    return;
                }
            }
        }
    }

    public SafeList<E4JComponent> getComponents() {
        return components;
    }

    public boolean hasComponent(String componentId) {
        for (int i = 0; i < components.getSize(); i++) {
            E4JComponent component = components.get(i);

            if (component.getId().equals(componentId)) {
                return true;
            }
        }

        return false;
    }

    public void addSystem(E4JSystem system) {
        
    }

    public void removeSystem() {
        
    }

    @Override
    public String toString() { 
        String result = String.format("<%s id=\"%s\" parent=\"%s\" name=\"%s\">\n", getType(), id, parent.getId(), name);

        for (int i = 0; i < components.getSize(); i++) {
            String componentsResults = "";

            for (String line : components.get(i).toString().split("\n")) {
                componentsResults += "    " + line + '\n';
            }

            result += componentsResults;
        }

        return result + String.format("</%s>", getType());
    }

    public void intitializeComponents() {
        E4JComponent gameObjectComponent = new E4JComponent("Game Object");

        gameObjectComponent.addAttribute(new E4JAttribute("Is Visible", true, E4JAttribute.EDIT_ANYWARE));
        addComponent(gameObjectComponent);
    }

    // Looks for the system of each component if one exists
    public void scanForComponentSystems() {

    }

    public void validateComponentSystems() {

    }


    public void start() { }
    public void update(float deltaTime) { }
    public void exit() { }
}
