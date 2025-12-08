package engine4j.core;

public class E4JObject {
    protected String id;
    protected E4JObject parent;

    private E4JObject() {
        this.id = "null";
    }

    public E4JObject(String id) {
        this.id = id;
        this.parent = new E4JObject();
    }

    public E4JObject(String id, E4JObject parent) {
        this.id = id;
        this.parent = parent;
    }

    public E4JObject getParent() {
        return parent;
    }

    public void setParent(E4JObject parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof E4JObject newObj) {
            if (getType().equals(newObj.getType()) && getId().equals(newObj.getId())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() { 
        return String.format("<%s id=\"%s\" parent=\"%s\"/>", getClass().getSimpleName(), id, parent.id);
    }
}
