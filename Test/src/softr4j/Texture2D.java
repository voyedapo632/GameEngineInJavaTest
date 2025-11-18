package softr4j;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Texture2D {
    Dimension size;
    int totalSize;
    public BufferedImage data;
    public Graphics gfx;

    public Texture2D(Dimension size) {
        this.size = size;
        totalSize = size.width * size.height;
        data = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        gfx = data.getGraphics();
    }

    public void resize(Dimension newSize) {
        size = newSize;
        totalSize = size.width * size.height;
        data = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        gfx = data.getGraphics();
    }

    public void clear(int color) {
        gfx.setColor(new Color(color));
        gfx.fillRect(0, 0, size.width, size.height);
    }

    public void setPixel(int x, int y, int color) {
        if (x < 0 || x >= size.width) {
            return;
        }

        if (y < 0 || y >= size.height) {
            return;
        }

        data.setRGB(x, y, color);
    }

    public int getPixel(int x, int y) {
        if (x < 0 || x >= size.width) {
            return 0;
        }

        if (y < 0 || y >= size.height) {
            return 0;
        }

        return data.getRGB(x, y);
    }
}
