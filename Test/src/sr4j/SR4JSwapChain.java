package sr4j;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SR4JSwapChain {
    protected SR4JTexture2D frameBuffer;
    protected SR4JSurfaceTarget surfaceTarget;
    protected SR4JDevice device;
    private boolean shouldResize;
    private int requestedWidth;
    private int requestedHeight;

    public SR4JSwapChain(SR4JDevice device, SR4JSurfaceTarget surfaceTarget) {
        this.device = device;
        this.surfaceTarget = surfaceTarget;
        int width = surfaceTarget.getWidth();
        int height = surfaceTarget.getHeight();

        if (width <= 0) {
            width = 1920;
        }

        if (height <= 0) {
            height = 1920;
        }

        frameBuffer = new SR4JTexture2D(width, height);
        shouldResize = false;
    }

    public SR4JDevice getDevice() {
        return device;
    }

    public SR4JTexture2D getFrameBuffer() {
        return frameBuffer;
    }

    public SR4JSurfaceTarget getSurfaceTarget() {
        return surfaceTarget;
    }

    public void requestResize(int newWidth, int newHeight) {
        shouldResize = true;
        requestedWidth = newWidth;
        requestedHeight = newHeight;
    }

    public void setSurfaceTarget(SR4JSurfaceTarget surfaceTarget) {
        this.surfaceTarget = surfaceTarget;
    }

    public void present() {
        if (shouldResize) {
            frameBuffer.resize(requestedWidth, requestedHeight);
            shouldResize = false;
        }

        //surfaceTarget.getGraphics()
        Graphics2D g = (Graphics2D)surfaceTarget.getGraphics();
        
        if (g != null) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g.drawImage(frameBuffer.getBufferedImage(), 0, 0, surfaceTarget.width, surfaceTarget.height, null);
        }
    }
}
