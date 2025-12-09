package ultra3d.ultra3dxml;

public class U3DXmlAttribute {
    private String name;
    private String value;

    public U3DXmlAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static U3DXmlAttribute parseAttribute(U3DXmlToken token) {
        if (!token.getType().equals(U3DXmlToken.ATTRIBUTE)) {
            return null;
        }

        String parsedName = token.getValue();
        String parsedValue = token.getValue();

        parsedName = parsedName.substring(0, parsedName.indexOf("=")).replace(" ", "");
        parsedValue = parsedValue.substring(parsedValue.indexOf("\"") + 1, parsedValue.lastIndexOf("\""));

        return new U3DXmlAttribute(parsedName, parsedValue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s=\"%s\"", name, value);
    }
}