package ultra3d.util;

public class U3DVector3f {
    public Float x, y, z;

    public U3DVector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static final U3DVector3f parseU3DVector3f(String value) {
        if (value != null) {
            value = value.replace(" ", "");
            String[] splitV = new String[] { "", "", "" };
            int index = 0;

            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);

                if ("-.0123456789".contains(c + "")) {
                    splitV[index] += c;
                } else if (c == ',') {
                    if (index < 3) {
                        index++;
                    }
                }
            }

            return new U3DVector3f(Float.parseFloat(splitV[0]), Float.parseFloat(splitV[1]), Float.parseFloat(splitV[2]));
        }

        return null;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
