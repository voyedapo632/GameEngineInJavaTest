package sr4j;

public class OBJFace {
    public int vertex;
    public int texture;
    public int normal;

    public OBJFace(int vertex, int texture, int normal) {
        this.vertex = vertex;
        this.texture = texture;
        this.normal = normal;
    }

    public static final OBJFace parseFace(String s) {
        String[] splitS = s.split("/");
        int value1 = Integer.parseInt(splitS[0]) - 1;
        int value2 = Integer.parseInt(splitS[1]) - 1;
        int value3 = Integer.parseInt(splitS[2]) - 1;
        return new OBJFace(value1, value2, value3);
    }
}
