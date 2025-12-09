package ultra3d.framework;

import java.util.ArrayList;
import java.util.HashMap;

public class U3DEntity extends U3DObject {
    protected HashMap<String, U3DComponent> components;

    public U3DEntity(String id, String parentId) {
        super(id, parentId);
        components = new HashMap<>();
    }

    public U3DEntity copyEntity() {
        U3DEntity newEntity = new U3DEntity(new String(id.toCharArray()), new String(parentId.toCharArray()));
        HashMap<String, U3DComponent> newComponents = new HashMap<>();

        for (U3DComponent component : components.values()) {
            U3DComponent newComponent = new U3DComponent(new String(component.getComponentId().toCharArray()));

            for (U3DField<?> field : component.getComponentFields()) {
                U3DField<Object> newField = new U3DField<>(new String(field.getFieldId().toCharArray()), U3DField.parseValue(new String(field.getValue().toString().toCharArray()), field.getType()));
                newComponent.addComponentField(newField);
            }

            newComponents.put(newComponent.getComponentId(), newComponent);
        }

        newEntity.components = newComponents;
        return newEntity;
    }
    
    public String getEntityId() {
        return id;
    }

    public void addComponent(U3DComponent c) {
        c.setParent(id);
        components.put(c.getComponentId(), c);
    }

    public U3DComponent getComponent(String id) {
        return components.get(id);
    }

    public ArrayList<U3DComponent> getComponents() {
        return new ArrayList<>(components.values());
    }

    public boolean hasComponent(String id) {
        return components.containsKey(id);
    }

    public boolean hasComponents(String[] componentIds) {
        if (componentIds == null) {
            return false;
        }

        if (componentIds.length == 0) {
            return false;
        }

        int count = 0;

        for (String componentId : componentIds) {
            if (hasComponent(componentId)) {
                count++;
            }
        }

        return count == componentIds.length;
    }

    public void removeComponent(String id) {
        components.remove(id);
    }

    @Override
    public String toString() {
        String result = String.format("<Entity id=\"%s\" parentId=\"%s\">\n", id, parentId);

        for (U3DComponent component : components.values()) {
            String[] data = component.toString().split("\n");

            for (String line : data) {
                result += "    " + line + "\n";
            }
        }

        return result + "</Entity>";
    }
}
