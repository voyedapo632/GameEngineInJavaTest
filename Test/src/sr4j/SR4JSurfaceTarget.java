package sr4j;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JFrame;

public class SR4JSurfaceTarget {
    protected Graphics gfx;
    protected int width;
    protected int height;

    public SR4JSurfaceTarget(Graphics gfx, int width, int height) {
        this.gfx = gfx;
        this.width = width;
        this.height = height;
    }

    public SR4JSurfaceTarget(JFrame jframe) {
        this.gfx = jframe.getGraphics();
        this.width = jframe.getWidth();
        this.height = jframe.getHeight();
    }

    public SR4JSurfaceTarget(Component component) {
        this.gfx = component.getGraphics();
        this.width = component.getWidth();
        this.height = component.getHeight();
    }

    public Graphics getGraphics() {
        return gfx;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void update(Graphics gfx, int width, int height) {
        this.gfx = gfx;
        this.width = width;
        this.height = height;
    }

    public void update(JFrame jframe) {
        this.gfx = jframe.getGraphics();
        this.width = jframe.getWidth();
        this.height = jframe.getHeight();
    }

    public void update(Component component) {
        this.gfx = component.getGraphics();
        this.width = component.getWidth();
        this.height = component.getHeight();
    }
}
