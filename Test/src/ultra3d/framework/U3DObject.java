package ultra3d.framework;

public class U3DObject {
    protected String id;
    protected String parentId;

    public U3DObject() {
        id = "null";
        parentId = "Root";
    }

    public U3DObject(String id) {
        this.id = id;
        parentId = "Root";
    }

    public U3DObject(String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public String getTypeName() {
        return getClass().getSimpleName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParent(String parentId) {
        this.parentId = parentId;
    }
}
