package softr4j;

import java.awt.Dimension;

public class DepthStencilView {
    private float[] data;
    Dimension size;
    
    public DepthStencilView(Dimension size) {
        data = new float[(int)size.getWidth() * (int)size.getHeight()];
        this.size = size;
    }

    public void resize(Dimension newSize) {
        data = new float[(int)newSize.getWidth() * (int)newSize.getHeight()];
        this.size = newSize;
    }

    public void clear(float f) {
        for (int i = 0; i < data.length; i++) {
            data[i] = f;
        }
    }

    public void set(int x, int y, int width, int height, float f) {
        for (int _x = x; _x < x + width; _x++) {
            for (int _y = y; _y < y + height; _y++) {
                int index = size.width * _y + _x;

                if (index > 0 && index < data.length) {
                    data[index] = f;
                }
            }
        }
    }

    public float get(int x, int y) {
        int index = size.width * y + x;

        if (index > 0 && index < data.length) {
            return data[index];
        }

        return 0.0f;
    }
}
