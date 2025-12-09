package sr4j;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SR4JTexture2D {
    protected BufferedImage buffer;
    protected Graphics gfx;

    public SR4JTexture2D(BufferedImage buffer) {
        this.buffer = buffer;
        gfx = buffer.getGraphics();
    }

    public SR4JTexture2D() {
        buffer = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        gfx = buffer.getGraphics();
    }

    public SR4JTexture2D(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        gfx = buffer.getGraphics();
    }

    public void resize(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        gfx = buffer.getGraphics();
    }

    public int getWidth() {
        return buffer.getWidth();
    }

    public int getHeight() {
        return buffer.getHeight();
    }

    public BufferedImage getBufferedImage() {
        return buffer;
    }

    public Graphics getGraphics() {
        return gfx;
    }

    public void setPixel(int x, int y, int color) {
        if (x < 0 || x >= getWidth()) {
            return;
        }

        if (y < 0 || y >= getHeight()) {
            return;
        }

        buffer.setRGB(x, y, color);
    }

    public int getPixel(int x, int y) {
        if (x < 0 || x >= getWidth()) {
            return 0;
        }

        if (y < 0 || y >= getHeight()) {
            return 0;
        }

        return buffer.getRGB(x, y);
    }

    public void clear(int color) {
        gfx.setColor(new Color(color));
        gfx.fillRect(0, 0, getWidth(), getHeight());
    }

    public void fillRect(int x, int y, int width, int height, int color) {
        if (x + width < 0 || x > getWidth()) {
            return;
        }

        if (y + height < 0 || y > getHeight()) {
            return;
        }

        for (int _y = y; _y < y + height; _y++) {
            for (int _x = x; _x < x + width; _x++) {
                setPixel(_x, _y, color);
            }
        }
    }
}
