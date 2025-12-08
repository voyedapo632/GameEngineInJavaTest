package engine4j.core;

public class E4JVector3 {
    float x, y, z;

    public E4JVector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public E4JVector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() { 
        return "" + x + ", " + y + ", " + z;
    }

    public static E4JVector3 parseE4JVector3(String s) { 
        String clearedStr = "";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if ((c >= '0' && c <= '9') || c == '-' || c == '.' || c == ',') {
                clearedStr += c;
            }
        }

        String[] strValues = clearedStr.split(",");
        float value1 = 0.0f, value2 = 0.0f, value3 = 0.0f;

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

        if (strValues[2].length() >= 1) {
            try {
                value3 = Float.parseFloat(strValues[2]);
            } catch (NumberFormatException e) { }
        }

        return new E4JVector3(value1, value2, value3);
    }
}
