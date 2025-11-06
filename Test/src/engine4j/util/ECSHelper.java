package engine4j.util;

import java.util.Vector;

public final class ECSHelper {
    
    public static char[] serializeEntity(ECSEntity entity) {
        String componentListStr = "";
        
        for (ECSComponent c : entity.components) {
            componentListStr += new String(ECSHelper.serializeComponent(c)) + "~!";
        }
        
        componentListStr = componentListStr.substring(0, componentListStr.length() - 2);
        
        return String.format("id=\"%s\"\nchildInstanceId=\"%s\"\nparent=\"%s\"\ncomponents=[%s]",
        entity.id, entity.childInstanceId, entity.parent, componentListStr).toCharArray();
    }

    public static ECSEntity deserializeEntity(char[] entityinBytes) {
        String[] splitEntity = new String(entityinBytes).trim().split("\n");
        ECSEntity result = new ECSEntity();
        String[] components = null;

        for (String line : splitEntity) {
            switch (line.substring(0, line.indexOf('=')).replace(" ", "")) {
                case "id" -> {
                    result.id = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
                } case "childInstanceId" -> {
                    result.childInstanceId = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
                } case "parent" -> {
                    result.parent = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
                } case "components" -> {
                    String c = line.substring(line.indexOf('[') + 1, line.lastIndexOf(']'));
                    if (!c.isEmpty()) {
                        components = c.split("~!");
                    }
                }
            }
        }

        if (components != null) {
            for (String c : components) {
                result.components.add(ECSHelper.deserializeCompenent(c.toCharArray()));
            }
        }

        return result;
    }
    
    public static char[] serializeComponent(ECSComponent c) {
        return String.format("group=\"%s\"~&type=\"%s\"~&identifier=\"%s\"~&value=\"%s\"~&canSave=\"%b\"~&canEdit=\"%b\"",
                c.group, c.type, c.identifier, new String(c.value), c.canSave, c.canEdit).toCharArray();
    }

    public static ECSComponent deserializeCompenent(char[] compenentinBytes) {
        String[] splitCompenent = new String(compenentinBytes).split("~&");
        ECSComponent result = new ECSComponent();

        for (String line : splitCompenent) {
            switch (line.substring(0, line.indexOf('=')).replace(" ", "")) {
                case "group" -> {
                    result.group = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
                } case "type" -> {
                    result.type = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
                } case "identifier" -> {
                    result.identifier = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
                } case "value" -> {
                    result.value = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"')).toCharArray();
                } case "canSave" -> {
                    result.canSave = Boolean.parseBoolean(line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"')));
                } case "canEdit" -> {
                    result.canEdit = Boolean.parseBoolean(line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"')));
                }
            }
        }

        return result;
    }

    public static ECSComponent getComponent(ECSEntity entity, String identifier) {
        for (ECSComponent c : entity.components) {
            if (c == null) {
                continue;
            }
            
            if (c.identifier.startsWith(identifier)) {
                return c;
            }
        }

        return null;
    }

    public static void addComponentIfNotExist(ECSEntity entity, ECSComponent component) {
        for (ECSComponent c : entity.components) {
            if (c.identifier.startsWith(component.identifier)) {
                return;
            }
        }

        entity.components.add(component);
    }

    public static void removeComponent(ECSEntity entity, String componentId) {
        for (ECSComponent c : entity.components) {
            if (c.identifier.startsWith(componentId)) {
                entity.components.remove(c);
            }
        }
    }

    public static void parseComponentList(Vector<ECSComponent> list) {
        for (ECSComponent c : list) {
            switch (c.type) {
                case "Vec3" -> {
                    c.setParsedValue(Vec3.deserialize(c.value));
                } case "Vec2" -> {
                    c.setParsedValue(Vec2.deserialize(c.value));
                } case "int" -> {
                    c.setParsedValue(Integer.parseInt(new String(c.value)));
                } case "char" -> {
                    c.setParsedValue(c.value[0]);
                } case "String" -> {
                    c.setParsedValue(new String(c.value));
                } case "boolean" -> {
                    c.setParsedValue(Boolean.parseBoolean(new String(c.value)));
                } case "Transform2D" -> {
                    c.setParsedValue(Transform2D.deserialize(c.value));
                }
            }
        }
    }

    public static boolean isCollidePointRect(int x1, int y1, int x2, int y2, int width, int height) {
        if ((x1 >= x2 && x1 <= x2 + width) &&
            (y1 >= y2 && y1 <= y2 + height)) {
            return true;
        }

        return false;
    }
}
