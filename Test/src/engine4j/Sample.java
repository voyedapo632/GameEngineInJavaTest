package engine4j;

import engine4j.util.GameWindow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Window extends GameWindow {
    public Window(int width, int height, String title) {
        super(width, height, title);
    }

    BufferedImage framebuffer;
    Graphics g;

    protected void onInit() {
        framebuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        g = framebuffer.getGraphics();
    }

    protected void onTick() {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.blue);

        for (int i = 0; i < 10; i++) {
            g.fillRect(110 * i, 0, 100, 100);
        }

        // Display
        getGraphics().drawImage(framebuffer, 0, 0, null);
    }

    protected void onResized() {

    }
}

public class Sample {
    public static void main(String[] args) {
        Window test = new Window(1200, 700, "Test");

        test.start(1000);
        // JFrame window = new JFrame("Test");
// 
        // window.setBounds(0, 0, 1200, 700);
// 
        // window.setVisible(true);
// 
        // BufferedImage framebuffer = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Graphics g = framebuffer.getGraphics();
// 
        // g.setColor(Color.black);
        // g.fillRect(0, 0, window.getWidth(), window.getHeight());
// 
        // window.getGraphics().drawImage(framebuffer, 0, 0, window);
    }
}
