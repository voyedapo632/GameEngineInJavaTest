package softr4j;

import java.awt.Dimension;
import java.awt.Graphics;

public class SwapChain {
    Texture2D frameBuffer;
    Graphics targetGraphics;
    Dimension targetDimensions;
    Graphics requestedTargetGraphics;
    Dimension requestedTargetDimensions;
    Dimension requestedFrameBufferDimensions;
    boolean shouldValidate;
    
    public SwapChain(Graphics targetGraphics, Dimension targetDimensions, Dimension frameBufferDimensions) {
        frameBuffer = new Texture2D(frameBufferDimensions);
        this.targetGraphics = targetGraphics;
        this.targetDimensions = targetDimensions;
        this.targetDimensions = targetDimensions;
        shouldValidate = false;
    }

    public void requestValidation(Graphics targetGraphics, Dimension targetDimensions, Dimension frameBufferDimensions) {
        requestedTargetGraphics = targetGraphics;
        requestedTargetDimensions = targetDimensions;
        requestedFrameBufferDimensions = frameBufferDimensions;
        shouldValidate = true;
    }

    public void present() {
        // Checks for validation
        if (shouldValidate) {
            targetDimensions = requestedTargetDimensions;
            targetGraphics = requestedTargetGraphics;
            frameBuffer.resize(requestedFrameBufferDimensions);
            shouldValidate = false;
        }

        if (frameBuffer == null || targetGraphics == null) {
            return;
        }

        if (frameBuffer.totalSize <= 0 || targetDimensions.width * targetDimensions.height <= 0) {
            return;
        }

        targetGraphics.drawImage(frameBuffer.data, 0, 0, null);
    }

    public Texture2D getFrameBuffer() {
        return frameBuffer;
    }
}
