package engine4j.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class E4JRenderContext {
    protected BufferedImage activeImage;
    protected Color activeColor;
    protected E4JVector3 cameraPosition;
    protected E4JVector3 cameraRotation;
    protected E4JVector3 translation;
    protected E4JVector3 scale;
    protected E4JVector3 rotation;

    public void start() { }
    public void startRender() { }
    public void clearBuffers() { }
    public void setModelTranslation(E4JVector3 v) { translation = v; }
    public void setModelScale(E4JVector3 v) { scale = v; }
    public void setModelRotation(E4JVector3 v) { rotation = v; }
    public void setCameraPosition(E4JVector3 v) { cameraPosition = v; }
    public void setCameraRotation(E4JVector3 v) { cameraRotation = v; }
    public void setDrawColor(Color color) { activeColor = color; }
    public void drawRect(int x, int y, int width, int height) { }
    public void drawRect(float x, float y, float width, float height) { }
    public void drawFillRect(int x, int y, int width, int height) { }
    public void drawFillRect(float x, float y, float width, float height) { }
    public void setFont(Font font) { }
    public void drawText(String text) { }
    public void drawImage(Image img) { }
    public void userImage(BufferedImage img) { activeImage = img; }
    public void drawTexture() { }
    public void endRender() { }
}
