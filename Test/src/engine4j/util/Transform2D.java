package engine4j.util;

public class Transform2D {
    public Vec3 translation; 
    public Vec2 scale;
    public Vec2 rotation;

    public Transform2D(Vec3 translation, Vec2 scale, Vec2 rotation) {
        this.translation = translation;
        this.scale = scale;
        this.rotation = rotation;
    }

    public static char[] serialize(Transform2D t) {
        return String.format("%s~%s~%s", 
            new String(Vec3.serialize(t.translation)), 
            new String(Vec2.serialize(t.scale)), 
            new String(Vec2.serialize(t.rotation))).toCharArray();
    }

    public static Transform2D deserialize(char[] arr) {
        String[] data = new String(arr).split("~");
        Vec3 t = Vec3.deserialize(data[0].toCharArray());
        Vec2 s = Vec2.deserialize(data[1].toCharArray());
        Vec2 r = Vec2.deserialize(data[2].toCharArray());
        return new Transform2D(t, s, r);
    }
}
