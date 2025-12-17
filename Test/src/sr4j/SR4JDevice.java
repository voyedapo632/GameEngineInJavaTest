package sr4j;

import java.awt.Color;
import java.util.ArrayList;

public class SR4JDevice {
    public Object[] registry;
    public DepthStencilView depthStencilView;
    public Vector4[] positions;
    public Vector4[] colors;
    public Vector4[] textureCoords;
    public int index;
    public SR4JBuffer vertexBuffer;
    public SR4JBuffer indexBuffer;
    public SR4JTexture2D[] textures;
    public SR4JShaderProgram program;
    public SR4JTexture2D renderTarget;
    public SR4JViewport viewport;
    public SR4JScissor scissor;
    public Vector4 fragColor;
    public Vector4 fragPosition;
    public Vector4 textureFragPos;
    public Vector4 texCoord;
    public SR4JRasterizationState rasterizationState;
    public Vector4 defaultColor;

    public SR4JDevice(int registrySize) {
        this.registry = new Object[registrySize];

        defaultColor = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);

        positions = new Vector4[] {
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        };

        colors = new Vector4[] {
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        };

        textureCoords = new Vector4[] {
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        };

        index = 0;
        textures = new SR4JTexture2D[16];
        fragColor = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);
        textureFragPos = new Vector4(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void setDefaultColor(Vector4 color) {
        defaultColor = color;
    }

    public Vector4 getDefaultColor() {
        return defaultColor;
    }

    public void setRasterizationState(SR4JRasterizationState rasterizationState) {
        this.rasterizationState = rasterizationState;
    }

    public void setViewport(SR4JViewport viewport) {
        this.viewport = viewport;
    }

    public void setScissor(SR4JScissor scissor) {
        this.scissor = scissor;
    }

    public void bindObject(Object obj, int index) {
        registry[index] = obj;
    }

    public void createDepthStencilView(int width, int height) {
        depthStencilView = new DepthStencilView(width, height);
    }

    public void clearDepthStencilView(float value) {
        depthStencilView.clear(value);
    }

    public void setRenderTarget(SR4JTexture2D renderTarget) {
        this.renderTarget = renderTarget;
    }

    public void clearRenderTarget(Vector4 color) {
        renderTarget.clear(new Color(color.x, color.y, color.z, color.w).getRGB());
    }

    public void proccessPixel(Vector4 pos) {
        int scrCol = new Color(fragColor.x, fragColor.y, fragColor.z, fragColor.w).getRGB();
        
        if (fragColor.w <= 0) {
            return;
        }
        
        int newRes = (int)rasterizationState.drawResolution + 1;

        for (int i = 0; i < newRes; i++) {
            for (int j = 0; j < newRes; j++) {
                float depthZ = depthStencilView.get((int)pos.x + j, (int)pos.y + i);
                boolean condition = false;
                
                switch (rasterizationState.depthTestMode) {
                    case SR4JRasterizationState.DEPTH_TEST_NONE -> {
                        condition = true;
                    } case SR4JRasterizationState.DEPTH_TEST_ENABLE -> {
                        condition = pos.z < depthZ;
                    } case SR4JRasterizationState.DEPTH_TEST_INVERT -> {
                        condition = pos.z > depthZ;
                    }
                }
                
                if (condition || depthZ == 1.0f) {
                    depthStencilView.set((int)fragPosition.x + j, (int)fragPosition.y + i, 0, 0, pos.z);
                    renderTarget.setPixel((int)fragPosition.x + j, (int)fragPosition.y + i, scrCol);
                }
            }
        }
    }

    public void proccessPixelOld(Vector4 pos, Vector4 col) {
        fragPosition = new Vector4((int)pos.x, (int)pos.y);
        program.callFragmentShaders(col);
        boolean doRener = false;
        int newRes = (int)rasterizationState.drawResolution + 2;

        for (int i = 0; i < newRes; i++) {
            for (int j = 0; j < newRes; j++) {
                if (depthStencilView.get((int)pos.x + j, (int)pos.y + i) == 1.0f || depthStencilView.get((int)pos.x + j, (int)pos.y + i) > pos.z) {
                    depthStencilView.set((int)pos.x + j, (int)pos.y + i, 1, 1, pos.z);
                    doRener = true;
                }
            }
        }

        if (doRener) {
            renderTarget.gfx.setColor(new Color(fragColor.x, fragColor.y, fragColor.z, fragColor.w));
            renderTarget.gfx.fillRect((int)pos.x, (int)pos.y, (int)rasterizationState.drawResolution, (int)rasterizationState.drawResolution);
        }
    }

    public void newLine(Vector4 v1, Vector4 v2, Vector4 c1, Vector4 c2, Vector4 t1, Vector4 t2) {
        int x1 = (int)v1.x, y1 = (int)v1.y;
        int x2 = (int)v2.x, y2 = (int)v2.y;
        int dx = Math.abs(x2 - x1);
        int dy = -Math.abs(y2 - y1);
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        int p = 2 * dy - dx;
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int error = dx + dy;
        
        Vector4 dc = Vector4.sub(c2, c1);
        Vector4 c = new Vector4(c1);
        Vector4 ci = Vector4.div(dc, dx);
        
        Vector4 dt = Vector4.sub(t2, t1);
        Vector4 t = new Vector4(t1);
        Vector4 ti = Vector4.div(dt, len);

        float dz = v2.z - v1.z;
        float zi = dz / dx;
        float z = v1.z;

        while (true) { 
            fragPosition = new Vector4((int)x1, (int)y1, z);
            textureFragPos = new Vector4(t);
            program.callFragmentShaders(c);
            proccessPixel(fragPosition);

            int e2 = 2 * error;

            if (e2 >= dy) {
                if (x1 == x2) break;
                error += dy;
                x1 += sx;
                c = Vector4.add(c, ci);
                z += zi;
            }
            
            if (e2 <= dx) {
                if (y1 == y2) break;
                error += dx;
                y1 += sy;
            }

            // float xid = x2 - x1;
            // float yid = y2 - y1;
            // float iLen = (float)Math.sqrt(xid * xid + yid * yid);
            // float step = iLen / len;
            //  
            // t.x = t1.x + dt.x * step;
            // t.y = t1.y + dt.y * step;
            // t = Vector4.add(t, ti);
            t.x += ti.x;
            t.y += ti.y;
        }
    }

    public void newTriangle2(Vector4 v1, Vector4 v2, Vector4 v3) {
        if (v1.z * v2.z * v3.z <= 0) {
            return;
        }

        if (v1.x < -1 || v1.x > 1) {
            return;
        }

        if (v1.y < -1 || v1.y > 1) {
            return;
        }

        v1 = toSceenCoord(v1);
        v2 = toSceenCoord(v2);
        v3 = toSceenCoord(v3);
        
        int x1 = (int)v1.x, y1 = (int)v1.y;
        int x2 = (int)v2.x, y2 = (int)v2.y;
        int dx = Math.abs(x2 - x1);
        int dy = -Math.abs(y2 - y1);
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        int p = 2 * dy - dx;
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int error = dx + dy;
        
        Vector4 c1 = colors[0];
        Vector4 c2 = colors[1];
        Vector4 c3 = colors[2];
        Vector4 dc = Vector4.sub(c2, c1);
        Vector4 c = new Vector4(c1);
        Vector4 ci = Vector4.div(dc, dx);

        Vector4 t1 = textureCoords[0];
        Vector4 t2 = textureCoords[1];
        Vector4 t3 = textureCoords[2];
        Vector4 dt = Vector4.sub(t2, t1);
        Vector4 t = new Vector4(t1);
        Vector4 ti = Vector4.div(dt, len);

        float dz = v2.z - v1.z;
        float zi = dz / dx;
        float z = v1.z;

        while (true) {
            newLine(new Vector4((int)x1, (int)y1, z), v3, c, c3, t, t3);

            int e2 = 2 * error;

            if (e2 >= dy) {
                if (x1 == x2) break;
                error += dy;
                x1 += sx;
                c = Vector4.add(c, ci);
                z += zi;
            }
            
            if (e2 <= dx) {
                if (y1 == y2) break;
                error += dx;
                y1 += sy;
            }

            // float xid = x2 - x1;
            // float yid = y2 - y1;
            // float iLen = (float)Math.sqrt(xid * xid + yid * yid);
            // float step = iLen / len;
            //  
            // t.x = t1.x + dt.x * step;
            // t.y = t1.y + dt.y * step;
            //t = Vector4.add(t, ti);
            t.x += ti.x;
            t.y += ti.y;
        }
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

    public float mid(float x, float y, float z) {
        float mid = x;

        if (y != max(x, y, z) && y != min(x, y, z)) {
            mid = y;
        }

        if (z != max(x, y, z) && z != min(x, y, z)) {
            mid = z;
        }

        return mid;
    }

    public void drawLine(Vector4 v1, Vector4 v2, Vector4 c1, Vector4 c2, Vector4 t1, Vector4 t2) {
        Vector4 dv = Vector4.sub(v2, v1);
        Vector4 dc = Vector4.sub(c2, c1);
        Vector4 dt = Vector4.sub(t2, t1);
        float len = (float)Math.sqrt(dv.x * dv.x + dv.y * dv.y);
        Vector4 dvi = Vector4.div(dv, len / rasterizationState.drawResolution);
        Vector4 dci = Vector4.div(dc, len / rasterizationState.drawResolution);
        Vector4 dti = Vector4.div(dt, len / rasterizationState.drawResolution);
        Vector4 v = new Vector4(v1);
        Vector4 c = new Vector4(c1);
        Vector4 t = new Vector4(t1);
        t.z = v.z;

        if (len <= 0 || len >= viewport.width) {
            return;
        }

        for (float i = 0; i < len; i += rasterizationState.drawResolution) {
            fragPosition = new Vector4((int)v.x, (int)v.y);
            textureFragPos = new Vector4(t);
            program.callFragmentShaders(c);
            proccessPixel(v);
            
            if (v.x < 0 || v.x > viewport.width) {
                break;
            }

            v.x += dvi.x;
            v.y += dvi.y;
            v.z += dvi.z;
            c.x += dci.x;
            c.y += dci.y;
            c.z += dci.z;
            c.w += dci.w;
            t.x += dti.x;
            t.y += dti.y;
            t.z += dti.z;
        }
    }

    public void drawTriangle(Vector4 v1, Vector4 v2, Vector4 v3) {
        if (v1.z * v2.z * v3.z <= 0) {
            return;
        }

        drawLine(v1, v2, colors[0], colors[1], textureCoords[0], textureCoords[1]);
        drawLine(v2, v3, colors[1], colors[2], textureCoords[1], textureCoords[2]);
        drawLine(v1, v3, colors[0], colors[2], textureCoords[0], textureCoords[2]);
    }

    public void drawWireTriangle(Vector4 v1, Vector4 v2, Vector4 v3) {
        if (v1.z * v2.z * v3.z <= 0) {
            return;
        }

        if (v1.x < -1 || v1.x > 1) {
            return;
        }

        if (v1.y < -1 || v1.y > 1) {
            return;
        }

        v1 = toSceenCoord(v1);
        v2 = toSceenCoord(v2);
        v3 = toSceenCoord(v3);

        Vector4 c1 = colors[0];
        Vector4 c2 = colors[1];
        Vector4 c3 = colors[2];
        Vector4 t1 = textureCoords[0];
        Vector4 t2 = textureCoords[1];
        Vector4 t3 = textureCoords[2];
        
        drawLine(v1, v2, c1, c2, t1, t2);
        drawLine(v2, v3, c2, c3, t2, t3);
        drawLine(v3, v1, c3, c1, t3, t1);
    }

    public void drawFillTriangle(Vector4 v1, Vector4 v2, Vector4 v3) {
        if (v1.z * v2.z * v3.z <= 0) {
            return;
        }

        if (v1.x < -1 || v1.x > 1) {
            return;
        }

        if (v1.y < -1 || v1.y > 1) {
            return;
        }

        v1 = toSceenCoord(v1);
        v2 = toSceenCoord(v2);
        v3 = toSceenCoord(v3);

        Vector4 c1 = colors[0];
        Vector4 c2 = colors[1];
        Vector4 c3 = colors[2];
        Vector4 t1 = textureCoords[0];
        Vector4 t2 = textureCoords[1];
        Vector4 t3 = textureCoords[2];
        Vector4 dv = Vector4.sub(v2, v1);
        Vector4 dc = Vector4.sub(c2, c1);
        Vector4 dt = Vector4.sub(t2, t1);
        float len = (float)Math.sqrt(dv.x * dv.x + dv.y * dv.y);
        Vector4 dvi = Vector4.div(dv, len / rasterizationState.drawResolution);
        Vector4 dci = Vector4.div(dc, len / rasterizationState.drawResolution);
        Vector4 dti = Vector4.div(dt, len / rasterizationState.drawResolution);
        Vector4 v = new Vector4(v1);
        Vector4 c = new Vector4(c1);
        Vector4 t = new Vector4(t1);
        t.z = v.z;

        if (len <= 0 || len >= viewport.width) {
            return;
        }

        for (float i = 0; i < len; i += rasterizationState.drawResolution) {
            drawLine(v, v3, c, c3, t, t3);
            //newLine(v, v3, c, c3, t, t3);
            
            v.x += dvi.x;
            v.y += dvi.y;
            v.z += dvi.z;
            c.x += dci.x;
            c.y += dci.y;
            c.z += dci.z;
            c.w += dci.w;
            t.x += dti.x;
            t.y += dti.y;
            t.z += dti.z;
        }
    }

    public void draw() {

    }

    public Vector4 toSceenCoord(Vector4 v) {
        float halfWidth = viewport.width / 2.0f;
        float halfHeight = viewport.height / 2.0f;
        return new Vector4(viewport.x + halfWidth + v.x * halfWidth, viewport.y + halfHeight - v.y * halfHeight, v.z, v.w);
    }

    public boolean isPointValid(Vector4 point) {
        return point.x >= -1.0f && point.x <= 1.0f && point.y >= -1.0f && point.y <= 1.0f;
    }

    public Vector4 clipPoint(Vector4 point, Vector4 pointOfReference) {
        if (point.x >= -1.0f && point.x <= 1.0f && point.y >= -1.0f && point.y <= 1.0f) {
            return point;
        }

        Vector4 newPoint = new Vector4(point);
        float m = (pointOfReference.y - point.y) / (pointOfReference.x - point.x);
        float m2 = (pointOfReference.x - point.x) / (pointOfReference.y - point.y);

        if (point.x < -1.0f) {
            newPoint.x = -1.0f;
            newPoint.y = m * (-1.0f - point.x ) + point.y;
        } else if (point.x > 1.0f) {
            newPoint.x = 1.0f;
            newPoint.y = m * (1.0f - point.x ) + point.y;
        }

        Vector4 newPoint2 = new Vector4(newPoint);

        if (newPoint.y < -1.0f) {
            newPoint2.x = newPoint.x + (-1.0f - newPoint.y) * m2;
            newPoint2.y = -1.0f;
        } else if (newPoint.x > 1.0f) {
            newPoint2.x = newPoint.x + (1.0f - newPoint.y) * m2;
            newPoint2.y = 1.0f;
        }

        return newPoint2;
    }

    public ArrayList<Vector4> toTriangleFan(ArrayList<Vector4> points) {
        if (points == null || points.size() < 3) {
            return null;
        }

        ArrayList<Vector4> result = new ArrayList<>();

        for (int i = 1; i < points.size() - 1; i++) {
            result.add(points.get(0));
            result.add(points.get(i));
            result.add(points.get(i + 1));
        }

        return result;
    }

    public ArrayList<Vector4> getClipedTriangle(Vector4 v1, Vector4 v2, Vector4 v3) {
        ArrayList<Vector4> clipedPoint = new ArrayList<>();
        
       clipedPoint.add(clipPoint(v1, v2));
       clipedPoint.add(clipPoint(v2, v1));
       clipedPoint.add(clipPoint(v2, v3));
       clipedPoint.add(clipPoint(v3, v2));
       clipedPoint.add(clipPoint(v1, v3));
       clipedPoint.add(clipPoint(v3, v1));

        return toTriangleFan(clipedPoint);
    }

    public void drawIndexed() {
        for (int i = (int)indexBuffer.getOffset(); i < indexBuffer.getSize(); i += indexBuffer.getStride() * 3) {
            Object item1 = indexBuffer.getData()[i];
            Object item2 = indexBuffer.getData()[i + (int)indexBuffer.getStride()];
            Object item3 = indexBuffer.getData()[i + (int)indexBuffer.getStride() * 2];

            index = 0;
            program.callVertexShaders(item1);
            index = 1;
            program.callVertexShaders(item2);
            index = 2;
            program.callVertexShaders(item3);

            Vector4 _v1 = new Vector4(positions[0]);
            Vector4 _v2 = new Vector4(positions[1]);
            Vector4 _v3 = new Vector4(positions[2]);

            // Perspective divide
            if (_v1.z > 0) {
                _v1.x /= _v1.z;
                _v1.y /= _v1.z;
            }
            if (_v2.z > 0) {
                _v2.x /= _v2.z;
                _v2.y /= _v2.z;
            }
            if (_v3.z > 0) {
                _v3.x /= _v3.z;
                _v3.y /= _v3.z;
            }

            ArrayList<Vector4> triangles = getClipedTriangle(_v1, _v2, _v3);

            // newTriangle2(new Vector4(positions[0]), new Vector4(positions[1]), new Vector4(positions[2]));
            drawFillTriangle(_v1, _v2, _v3);

        }
    }

    public void drawIndexedOld() {
        for (int i = (int)indexBuffer.getOffset(); i < indexBuffer.getSize(); i += indexBuffer.getStride() * 3) {
            Object item1 = indexBuffer.getData()[i];
            Object item2 = indexBuffer.getData()[i + (int)indexBuffer.getStride()];
            Object item3 = indexBuffer.getData()[i + (int)indexBuffer.getStride() * 2];

            index = 0;
            program.callVertexShaders(item1);
            index = 1;
            program.callVertexShaders(item2);
            index = 2;
            program.callVertexShaders(item3);

            Vector4 _v1 = new Vector4(positions[0]);
            Vector4 _v2 = new Vector4(positions[1]);
            Vector4 _v3 = new Vector4(positions[2]);

            // Perspective divide
            if (_v1.z > 0) {
                _v1.x /= _v1.z;
                _v1.y /= _v1.z;
            }
            if (_v2.z > 0) {
                _v2.x /= _v2.z;
                _v2.y /= _v2.z;
            }
            if (_v3.z > 0) {
                _v3.x /= _v3.z;
                _v3.y /= _v3.z;
            }

            ArrayList<Vector4> triangles = getClipedTriangle(_v1, _v2, _v3);

            for (int j = 0; j < triangles.size(); j += 3) {
                Vector4 v1 = new Vector4(triangles.get(j));
                Vector4 v2 = new Vector4(triangles.get(j + 1));
                Vector4 v3 = new Vector4(triangles.get(j + 2));

    
                // newTriangle2(new Vector4(positions[0]), new Vector4(positions[1]), new Vector4(positions[2]));
                drawFillTriangle(v1, v2, v3);
            }
        }
    }

    public void bindBuffer(SR4JBuffer buffer) {
        switch (buffer.getType()) {
            case SR4JBuffer.BUFFER_TYPE_VERTEX -> {
                vertexBuffer = buffer;
            } case SR4JBuffer.BUFFER_TYPE_INDEX -> {
                indexBuffer = buffer;
            }
        }
    }

    public void bindTexture(SR4JTexture2D texture, int index) {
        textures[index] = texture;
    }

    public void useProgram(SR4JShaderProgram program) {
        this.program = program;
    }
}
