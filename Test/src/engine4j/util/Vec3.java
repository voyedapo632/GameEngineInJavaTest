package engine4j.util;

public class Vec3 {
    public float x, y, z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public char[] serialize() {
        return String.format("%f,%f,%f", x, y, z).toCharArray();
    }

    public static char[] serialize(Vec3 v) {
        return String.format("%f,%f,%f", v.x, v.y, v.z).toCharArray();
    }

    public static Vec3 deserialize(char[] arr) {
        String[] data = new String(arr).split(",");
        return new Vec3(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]));
    }
    
    public static char[] interpretValue(char[] newValue) {
        String newValueStr = new String(newValue);
        String[] values = new String[] { "", "", "" };
        int current = 0;

        for (int i = 0; i < newValueStr.length(); i++) {
            char c = newValueStr.charAt(i);

            if (c == ',' && current < 3) {
                current++;
            }

            if ((c >= '0' && c <= '9') || c == '.' || c == '-') {
                values[current] += c;
            }
        }

        for (String v : values) {
            if (v.isEmpty() || !v.contains("0123456789")) {
                v = "0.0";
            }
        }

        for (String v : values) {
            v = "" + Float.parseFloat(v);
        }

        return String.format("%s,%s,%s", values[0], values[1], values[2]).toCharArray();
    }
}
