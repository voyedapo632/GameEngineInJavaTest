package ultra3d.framework;

import java.util.ArrayList;
import java.util.HashMap;

public class U3DComponent extends U3DObject {
    protected HashMap<String, U3DField<?>> u3dFields;
    
    public U3DComponent() {
        super();
        id = getClass().getSimpleName();
        this.u3dFields = new HashMap<>();
    }

    public U3DComponent(String id) {
        super(id);
        this.u3dFields = new HashMap<>();
    }

    public void addComponentField(U3DField<?> f) {
        f.setParent(id);
        u3dFields.put(f.getFieldId(), f);
    }
    
    public void removeComponentField(String fieldId) {
        u3dFields.remove(fieldId);
    }

    public U3DField<?> getComponentField(String fieldId) {
        return u3dFields.get(fieldId);
    }

    public ArrayList<U3DField<?>> getComponentFields() {
        return new ArrayList<>(u3dFields.values());
    }

    public String getComponentId() {
        return id;
    }

    @Override
    public String toString() {
        String result = String.format("<Component id=\"%s\" parentId=\"%s\">\n", id, parentId);

        for (U3DField<?> field : u3dFields.values()) {
            result += "    " + field.toString() + "\n";
        }

        return result + "</Component>";
    }
}
