package engine4j.core;

public class E4JAttribute extends E4JObject {
    protected String dataType;
    protected Object value;
    protected int editAccess;
    public static final int EDIT_NONE = 0;
    public static final int EDIT_ANYWARE = 1;

    public E4JAttribute(String id, Object value, int editAccess) {
        super(id);
        this.dataType = value.getClass().getSimpleName();
        this.value = value;
        this.editAccess = editAccess;
    }

    public E4JAttribute(String id, String dataType, Object value, int editAccess) {
        super(id);
        this.dataType = dataType;
        this.value = value;
        this.editAccess = editAccess;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getEditAccess() {
        return editAccess;
    }

    public void setEditAccess(int editAccess) {
        this.editAccess = editAccess;
    }

    @Override
    public String toString() { 
        return String.format("<%s id=\"%s\" parent=\"%s\" dataType=\"%s\" editAccess=\"%d\">%s</%s>", getType(), id, parent.getId(), dataType, editAccess, value.toString(), getType());
    }
}
