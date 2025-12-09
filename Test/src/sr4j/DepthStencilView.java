package sr4j;

import java.awt.Dimension;

public class DepthStencilView {
    private float[] data;
    private int width;
    private int height;
    
    public DepthStencilView(int width, int height) {
        data = new float[width * height];
        this.width = width;
        this.height = height;
    }

    public void resize(int width, int height) {
        data = new float[width * height];
        this.width = width;
        this.height = height;
    }

    public void clear(float f) {
        for (int i = 0; i < data.length; i++) {
            data[i] = f;
        }
    }

    public void set(int x, int y, int width, int height, float f) {
        int index = this.width * y + x;

                if (index > 0 && index < data.length) {
                    data[index] = f;
                }
    }

    public float get(int x, int y) {
        int index = width * y + x;

        if (index > 0 && index < data.length) {
            return data[index];
        }

        return 0.0f;
    }
}
