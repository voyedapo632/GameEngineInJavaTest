package ultra3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import engine4j.util.GameWindow;
import sr4j.Vector4;

public class WindowMain extends GameWindow {
    private BufferedImage frameBuffer;
    private Graphics gfx;
    
    public WindowMain(int width, int height, String title) {
        super(width, height, title);
    }

    @Override
    protected void onInit() {
        frameBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        gfx = frameBuffer.getGraphics();
    }

    @Override
    protected void onResized() {
        frameBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        gfx = frameBuffer.getGraphics();
    }

    int fps = 0;
    int lastFps = 0;
    long lastTime = System.currentTimeMillis();

    @Override
    protected void onTick() {
        // Clear sceen
        gfx.setColor(Color.black);
        gfx.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw
        Vector4[] triangleVerticies = new Vector4[] {
            new Vector4(-0.25f, -0.53f, 10.0f), new Vector4(1.0f, 0.0f, 0.0f),
            new Vector4(0.125f, -1.0f, 20.0f), new Vector4(0.0f, 1.0f, 0.0f),
            new Vector4(0.0f, 0.53f, 30.0f), new Vector4(0.0f, 0.0f, 1.0f)
        };

        for (int i = 0; i < triangleVerticies.length; i += 2 * 3) {
            Vector4 pos1 = new Vector4(triangleVerticies[i]);
            Vector4 col1 = new Vector4(triangleVerticies[i + 1]);
            Vector4 pos2 = new Vector4(triangleVerticies[i + 2]);
            Vector4 col2 = new Vector4(triangleVerticies[i + 2 + 1]);
            Vector4 pos3 = new Vector4(triangleVerticies[i + 2 * 2]);
            Vector4 col3 = new Vector4(triangleVerticies[i + 2 * 2 + 1]);

            points[0] = toScreenCoords(pos1);
            points[1] = toScreenCoords(pos2);
            points[2] = toScreenCoords(pos3);
            colors[0] = col1;
            colors[1] = col2;
            colors[2] = col3;

            barycentricTriangleFill(toScreenCoords(pos1), toScreenCoords(pos2), toScreenCoords(pos3));
            // for (int x = 0; x < getWidth(); x++) {
            //     for (int y = 0; y < getHeight(); y++) {
            //         setPixel3D2(new Vector4(x, y));
            //     }
            // }
            // drawTriangle(toScreenCoords(pos1), toScreenCoords(pos2), toScreenCoords(pos3));
        }
        
        // Present
        present();

        // Update FPS counter
        if (System.currentTimeMillis() - lastTime >= 1000) {
            System.out.println("FPS: " + fps);
            lastFps = fps;
            fps = 0;
            lastTime = System.currentTimeMillis();
        }

        fps++;
    }

    private Vector4[] points = new Vector4[3];
    private Vector4[] colors = new Vector4[3];

    private float difference(Vector4 p1, Vector4 p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    private float triangleArea(Vector4 v1, Vector4 v2, Vector4 v3) {
        return Math.abs((v1.x * (v2.y - v3.y) + v2.x * (v3.y - v1.y) + v3.x * (v1.y - v2.y)) / 2);
    }
 
    private void setPixel3D2(Vector4 pos) {
        float abc = triangleArea(points[0], points[1], points[2]);
        float pab = triangleArea(pos, points[0], points[1]);
        float pbc = triangleArea(pos, points[1], points[2]);
        float pac = triangleArea(pos, points[0], points[2]);

        float u = pab / abc;
        float v = pbc / abc;
        float w = pac / abc;

        if ((u < 0 || u > 1) || (v < 0 || v > 1) || (w < 0 || w > 1) || ((u + v + w) < 0 || (u + v + w) > 1)) {
            return;
        }

        Vector4 col = Vector4.add(Vector4.add(Vector4.mul(colors[0], v), Vector4.mul(colors[1], w)), Vector4.mul(colors[2], u));
        col = Vector4.clamp(col);
        
        int screenCol = new Color(col.x, col.y, col.z, 1.0f).getRGB();
        frameBuffer.setRGB((int)pos.x, (int)pos.y, screenCol);
    }

    public float min(float x, float y, float z) {
        float min = x;

        if (y < min) {
            min = y;
        }

        if (z < min) {
            min = z;
        }

        return min;
    }

    public float max(float x, float y, float z) {
        float max = x;

        if (y > max) {
            max = y;
        }

        if (z > max) {
            max = z;
        }

        return max;
    }

    int res = 3;

    private void setPixel(int x, int y, Vector4 col) {
        Vector4 newCol = Vector4.clamp(col);
        int screenCol = new Color(newCol.x, newCol.y, newCol.z, 1.0f).getRGB();
        
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                int sx = x + i;
                int sy = y + j;

                if ((sx >= 0 && sx < frameBuffer.getWidth()) && (sy >= 0 && sy < frameBuffer.getHeight())) {
                    frameBuffer.setRGB(sx, sy, screenCol);
                }
            }
        }
    }

    private void barycentricTriangleFill(Vector4 v1, Vector4 v2, Vector4 v3) {
        int minX = (int)min(v1.x, v2.x, v3.x);
        int minY = (int)min(v1.y, v2.y, v3.y);
        int maxX = (int)max(v1.x, v2.x, v3.x);
        int maxY = (int)max(v1.y, v2.y, v3.y);

        if (minX < 0) {
            minX = 0;
        }

        if (minY < 0) {
            minX = 0;
        }

        if (minX >= getWidth() || minY >= getHeight()) {
            return;
        }

        if (maxX >= getWidth()) {
            maxX = getWidth() - 1;
        }

        if (maxY >= getHeight()) {
            maxY = getHeight() - 1;
        }

        for (int x = minX; x < maxX; x += res) {
            for (int y = minY; y < maxY; y += res) {
                Vector4 pos = new Vector4(x, y);

                float abc = triangleArea(v1, v2, v3);
                float pab = triangleArea(pos, v1, v2);
                float pbc = triangleArea(pos, v2, v3);
                float pac = triangleArea(pos, v1, v3);

                float u = pab / abc;
                float v = pbc / abc;
                float w = pac / abc;

                // Continue if point is outside of triangle
                if ((u < 0 || u > 1) || (v < 0 || v > 1) || (w < 0 || w > 1) || ((u + v + w) < 0 || (u + v + w) > 1)) {
                    continue;
                }

                // Calculate color
                Vector4 col = Vector4.add(Vector4.add(Vector4.mul(colors[0], v), Vector4.mul(colors[1], w)), Vector4.mul(colors[2], u));
                col = Vector4.clamp(col);

                // Display pixel
                setPixel(x, y, col);
            }
        }
    }

    private void setPixel3D(Vector4 pos) {
        // float acDif = difference(points[0], points[2]);
        float abDif = difference(points[0], points[1]);
        float apDif = difference(points[0], pos);
        float w1 = apDif / abDif;
        //float w2 = apDif / abDif;
        //float w2 = apDif / abDif;
        Vector4 newPos = Vector4.add(points[0], Vector4.mul(Vector4.sub(points[1], points[0]), w1));
        float npDif = difference(newPos, pos);
        float ncDif = difference(newPos, points[2]);
        float w2 = npDif / ncDif;
        Vector4 col = Vector4.add(colors[0], Vector4.mul(Vector4.sub(colors[1], colors[0]), w1));
        col = Vector4.add(col, Vector4.mul(Vector4.sub(colors[2], col), w2));
        col = Vector4.clamp(col);

        int screenCol = new Color(col.x, col.y, col.z, 1.0f).getRGB();
        frameBuffer.setRGB((int)pos.x, (int)pos.y, screenCol);
    }

    private Vector4 toScreenCoords(Vector4 pos) {
        float halfWidth = (float)getWidth() / 2.0f;
        float halfHeight = (float)getHeight() / 2.0f;
        
        return new Vector4(halfWidth + pos.x * halfWidth, halfHeight + -pos.y * halfHeight, pos.z, pos.w);
    }

    private void drawLine(Vector4 p1, Vector4 p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        float dz = p2.z - p1.z;
        float len = (float)Math.sqrt(dx * dx + dy * dy);
        float xInc = dx / len;
        float yInc = dy / len;
        float zInc = dz / len;
        Vector4 p = new Vector4(p1);

        for (float i = 0; i < len; i++) {
            setPixel3D(p);
            p.x += xInc;
            p.y += yInc;
            p.z += zInc;
        }
    }

    private void drawTriangle(Vector4 p1, Vector4 p2, Vector4 p3) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        float dz = p2.z - p1.z;
        float len = (float)Math.sqrt(dx * dx + dy * dy);
        float xInc = dx / len;
        float yInc = dy / len;
        float zInc = dz / len;
        Vector4 p = new Vector4(p1);

        for (float i = 0; i < len; i++) {
            drawLine(p, p3);
            p.x += xInc;
            p.y += yInc;
            p.z += zInc;
        }
    }

    private void plotPoint(int x, int y) {
        gfx.fillRect(getWidth() / 2 + x, getHeight() / 2 - y, 1, 1);
    }

    private void setPixel(Vector4 pos, Vector4 col) {
        int screenCol = new Color(col.x, col.y, col.z, 0.0f).getRGB();
        frameBuffer.setRGB((int)pos.x, (int)pos.y, screenCol);
    }

    private void present() {
        getGraphics().drawImage(frameBuffer, 0, 0, this);
    }
    
    public static void main(String[] args) {
        WindowMain window = new WindowMain(1200, 700, "Test");
        window.start(1000 / 1024);
    }
}
