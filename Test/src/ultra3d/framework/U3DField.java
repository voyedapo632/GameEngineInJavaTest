package ultra3d.framework;

import ultra3d.ultra3dxml.U3DXmlDocument;
import ultra3d.ultra3dxml.U3DXmlElement;
import ultra3d.util.U3DVector3f;

public class U3DField<T> extends U3DObject {
    public static final String EDIT_ACCESS_CODE = "CODE";
    public static final String EDIT_ACCESS_ANYWHERE = "ANYWHERE";

    public T value;
    public String editAccess;
    public String strType;

    public U3DField(String id, T value) {
        super(id);
        this.value = value;
        editAccess = U3DField.EDIT_ACCESS_ANYWHERE;
        this.strType = value.getClass().getSimpleName();
    }

    public U3DField(String id, T value, String editAccess) {
        super(id);
        this.value = value;
        this.editAccess = editAccess;
        this.strType = value.getClass().getSimpleName();
    }

    public U3DField(String id, T value, String editAccess, String strType) {
        super(id);
        this.value = value;
        this.editAccess = editAccess;
        this.strType = strType;
    }

    public String getType() {
        return strType;
    }
    
    public T getValue() {
        return value;
    }

    public void setValue(Object newValue) {
        value = (T)newValue;
    }

    public String getFieldId() {
        return id;
    }

    public static Object parseValue(String fieldValue, String fieldType) {
        Object newValue;

        switch (fieldType) {
            case "Integer" -> {
                Integer result = null;

                try {
                    result = Integer.valueOf(fieldValue);
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
                
                if (result == null) {
                    result = 0;
                }

                newValue = result;
            } case "Boolean" -> {
                Boolean result = null;

                try {
                    result = Boolean.valueOf(fieldValue);
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }

                if (result == null) {
                    result = false;
                }

                newValue = result;
            } case "Float" -> {
                Float result = null;

                try {
                    result = Float.valueOf(fieldValue);
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }

                if (result == null) {
                    result = 0.0f;
                }

                newValue = result;
            } case "U3DVector3f" -> {
                U3DVector3f result = null;

                try {
                    result = U3DVector3f.parseU3DVector3f(fieldValue);
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }

                if (result == null) {
                    result = new U3DVector3f(0.0f, 0.0f, 0.0f);
                }

                newValue = result;
            } default -> {
                newValue = fieldValue;
            }
        }

        return newValue;
    }

    public static U3DField<?> parseField(String s) {
        U3DXmlDocument document = new U3DXmlDocument(s);
        U3DXmlElement fieldElement = U3DXmlDocument.getElementsByTagName(document, "Field").get(0);
        String fieldId = fieldElement.getAttributeByName("id").getValue();
        String fieldType = fieldElement.getAttributeByName("type").getValue();
        String fieldEditAccess = fieldElement.getAttributeByName("editAccess").getValue();
        String fieldValue = fieldElement.getInnerXml().get(0).getValue();
        return new U3DField<>(fieldId, U3DField.parseValue(fieldValue, fieldType), fieldEditAccess);
    }

    public static U3DField<?> parseField(U3DXmlElement fieldElement) {
        String fieldId = fieldElement.getAttributeByName("id").getValue();
        String fieldType = fieldElement.getAttributeByName("type").getValue();
        String fieldEditAccess = fieldElement.getAttributeByName("editAccess").getValue();
        String fieldValue = fieldElement.getInnerXml().get(0).getValue();
        return new U3DField<>(fieldId, U3DField.parseValue(fieldValue, fieldType), fieldEditAccess);
    }

    @Override
    public String toString() {
        return String.format("<Field id=\"%s\" parentId=\"%s\" type=\"%s\" editAccess=\"%s\">", id, parentId, strType, editAccess) + value.toString() + "</Field>";
    }
}