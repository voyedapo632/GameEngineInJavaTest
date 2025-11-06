package engine4j.util;

public class ECSComponent {
    public String group;
    public String type;
    public String identifier;
    public char[] value;
    public boolean canSave;
    public boolean canEdit; // Shows in editor if true
    private Object parsedValue;
    private double primitiveParsedValue;
    private boolean primitiveBoolParsedValue;

    public ECSComponent() {
        type = "String";
        identifier = "unspecified";
        value = "null".toCharArray();
        parsedValue = null;
        canSave = true;
        canEdit = false;
    }

    public ECSComponent(String group, String type, String identifier, char[] value, boolean canSave, boolean canEdit) {
        this.type = type;
        this.identifier = identifier;
        this.value = value;
        this.canSave = canSave;
        this.canEdit = canEdit;
        this.group = group;
    }

    public void setParsedValue(Object parsedValue) {
        this.parsedValue = parsedValue;
    }

    public void setParsedValue(double parsedValue) {
        this.primitiveParsedValue = parsedValue;
    }

    public void setParsedValue(int parsedValue) {
        this.primitiveParsedValue = (double)parsedValue;
    }

    public void setParsedValue(char parsedValue) {
        this.primitiveParsedValue = (double)parsedValue;
    }

    public void setParsedValue(long parsedValue) {
        this.primitiveParsedValue = (double)parsedValue;
    }

    public void setParsedValue(boolean parsedValue) {
        this.primitiveBoolParsedValue = parsedValue;
    }

    public Object getParsedObjectValue() {
        return parsedValue;
    }

    public double getPrimitiveParsedValue() {
        return primitiveParsedValue;
    }

    public boolean getBooleanParsedValue() {
        return primitiveBoolParsedValue;
    }
}
