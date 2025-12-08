package engine4j.core;

public class E4JVector2 {
    float x, y;

    public E4JVector2() {
        this.x = 0;
        this.y = 0;
    }

    public E4JVector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "" + x + ", " + y;
    }

    public static E4JVector2 parseE4JVector2(String s) { 
        String clearedStr = "";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if ((c >= '0' && c <= '9') || c == '-' || c == '.' || c == ',') {
                clearedStr += c;
            }
        }

        String[] strValues = clearedStr.split(",");
        float value1 = 0.0f, value2 = 0.0f;

        if (strValues[0].length() >= 1) {
            try {
                value1 = Float.parseFloat(strValues[0]);
            } catch (NumberFormatException e) { }
        }

        if (strValues[1].length() >= 1) {
            try {
                value2 = Float.parseFloat(strValues[1]);
            } catch (NumberFormatException e) { }
        }

        return new E4JVector2(value1, value2); 
    }
}
