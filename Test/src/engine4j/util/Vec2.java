package engine4j.util;

public class Vec2 {
    public float x, y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public char[] serialize() {
        return String.format("%f,%f", x, y).toCharArray();
    }

    public static char[] serialize(Vec2 v) {
        return String.format("%f,%f", v.x, v.y).toCharArray();
    }

    public static Vec2 deserialize(char[] arr) {
        String[] data = new String(arr).split(",");
        return new Vec2(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
    }

    public static char[] interpretValue(char[] newValue) {
        String newValueStr = new String(newValue);
        String[] values = new String[] { "", "" };
        int current = 0;

        for (int i = 0; i < newValueStr.length(); i++) {
            char c = newValueStr.charAt(i);

            if (c == ',' && current < 2) {
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

        return String.format("%s,%s", values[0], values[1]).toCharArray();
    }
}