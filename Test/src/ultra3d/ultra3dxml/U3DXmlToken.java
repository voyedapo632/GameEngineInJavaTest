package ultra3d.ultra3dxml;

public class U3DXmlToken {
    private String value;
    private String type;
    public static final String ELEMENT = "ELEMENT";
    public static final String END_ELEMENT = "END_ELEMENT";
    public static final String CONSTANT = "CONSTANT";
    public static final String ATTRIBUTE = "ATTRIBUTE";
    public static final String COMMENT = "COMMENT";

    public U3DXmlToken(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s : %s", this.value, this.type);
    }
}