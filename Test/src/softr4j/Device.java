
package softr4j;

import java.awt.Color;

public class Device {
    private ShaderProgram program;
    private Buffer vertexBuffer;
    private Buffer indexBuffer;
    private Buffer constantBuffer;
    private Buffer[] vertexAttributeLayout;
    private Texture2D renderTarget;
    private DepthStencilView depthStencil;
    private Viewport viewport;
    private PrimitiveTopology primitiveTopology;
    private RasterizationState rasterizationState;

    public Device() {
        vertexAttributeLayout = new Buffer[1024];
    }

    public void setFrameBuffer(Texture2D renderTarget) {
        this.renderTarget = renderTarget;
    }

    public void setDepthStencil(DepthStencilView depthStencil) {
        this.depthStencil = depthStencil;
    }

    public void clearRenderTarget(int color) {
        renderTarget.clear(color);
    }

    public void clearDepthStencile(float defaultZ) {
        depthStencil.clear(defaultZ);
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setPrimitiveTopology(PrimitiveTopology primitiveTopology) {
        this.primitiveTopology = primitiveTopology;
    }

    public PrimitiveTopology getPrimitiveTopology() {
        return primitiveTopology;
    }

    public void setRasterizationState(RasterizationState rasterizationState) {
        this.rasterizationState = rasterizationState;
    }

    public RasterizationState getRasterizationState() {
        return rasterizationState;
    }

    public Texture2D getRenderTarget() {
        return renderTarget;
    }

    public void setShaderProgram(ShaderProgram program) {
        this.program = program;
    }

    public ShaderProgram getShaderProgram() {
        return program;
    }

    public void bindBuffer(Buffer buffer) {
        switch (buffer.bindFlag) {
            case BindFlag.VERTEX_BUFFER -> {
                vertexBuffer = buffer;
            } case BindFlag.INDEX_BUFFER -> {
                indexBuffer = buffer;
            } case BindFlag.CONSTANT_BUFFER -> {
                constantBuffer = buffer;
            }
        }
    }

    public Buffer getVertexBuffer() {
        return vertexBuffer;
    }

    public Buffer getIndexBuffer() {
        return indexBuffer;
    }

    public Buffer getConstantBuffer() {
        return constantBuffer;
    }

    //Graphics g;

    private void setPixel(int x, int y, float z, int width, int height, int color) {
        Vector4 newColor = program.onPixelShaderCalled(x, y);
        boolean doRener = false;

        for (int i = 0; i < rasterizationState.drawResolution; i++) {
            for (int j = 0; j < rasterizationState.drawResolution; j++) {
                if (depthStencil.get(x + j, y + i) == 0.0f || depthStencil.get(x + j, y + i) > z) {
                    depthStencil.set(x + j, y + i, 1, 1, z);
                    doRener = true;
                }
            }
        }

        if (doRener) {
            renderTarget.gfx.setColor(new Color(newColor.x, newColor.y, newColor.z));
            renderTarget.gfx.fillRect(x, y, width, height);
        }
    }

    private void setPixel2(int x, int y, float z, int width, int height, int offsetX, int offsetY) {
        
        for (int i = 0; i < rasterizationState.drawResolution; i++) {
            for (int j = 0; j < rasterizationState.drawResolution; j++) {
                if (depthStencil.get(x + j, y + i) == 0.0f || depthStencil.get(x + j, y + i) > z) {
                    Vector4 newColor = Vector4.clamp(program.onPixelShaderCalled(x + j, y + j));

                    int screenColor = new Color(newColor.x, newColor.y, newColor.z).getRGB();

                    depthStencil.set(x + j, y + i, 1, 1, z);
                    renderTarget.setPixel(x + j, y + i, screenColor);
                }
            }
        }
    }

    public void drawLine(Vector4 v1, Vector4 v2) {
        Vector4 dv = Vector4.sub(v2, v1);
        float len = (float)Math.sqrt(dv.x * dv.x + dv.y * dv.y);
        Vector4 vi = Vector4.div(dv, len / rasterizationState.drawResolution);
        Vector4 v = new Vector4(v1.x, v1.y, v1.z, v1.w);

        if (len <= 0) {
            return;
        }

        if (len > 1200) {
            return;
        }

        for (float i = 0; i < len; i += rasterizationState.drawResolution) {
            setPixel2((int)v.x, (int)v.y, v.z, (int)rasterizationState.drawResolution, (int)rasterizationState.drawResolution, (int)v1.x, (int)v1.y);

            v.x += vi.x;
            v.y += vi.y;
            v.z += vi.z;
        }
    }

    public void drawHighResLine(Vector4 v1, Vector4 v2) {
        Vector4 dv = Vector4.sub(v2, v1);
        float len = (float)Math.sqrt(dv.x * dv.x + dv.y * dv.y);
        Vector4 vi = Vector4.div(dv, len);
        Vector4 v = new Vector4(v1.x, v1.y, v1.z, v1.w);

        if (len <= 0) {
            return;
        }

        if (len > 1200) {
            return;
        }

        for (float i = 0; i < len; i++) {
            setPixel2((int)v.x, (int)v.y, v.z, (int)rasterizationState.drawResolution, (int)rasterizationState.drawResolution, (int)v1.x, (int)v1.y);

            v.x += vi.x;
            v.y += vi.y;
            v.z += vi.z;
        }
    }

    public void drawTriangle(Vector4 v1, Vector4 v2, Vector4 v3) {
        drawHighResLine(v1, v2);
        drawHighResLine(v2, v3);
        drawHighResLine(v3, v1);
    }

    public void fillTriangle(Vector4 v1, Vector4 v2, Vector4 v3) {
        Vector4 dv = Vector4.sub(v2, v1);
        float len = (float)Math.sqrt(dv.x * dv.x + dv.y * dv.y);
        Vector4 vi = Vector4.div(dv, len / rasterizationState.drawResolution);
        Vector4 v = new Vector4(v1.x, v1.y, v1.z, v1.w);

        for (float i = 0; i < len; i += rasterizationState.drawResolution) {
            drawLine(v, v3);

            v.x += vi.x;
            v.y += vi.y;
            v.z += vi.z;
        }
    }

    private Vector4 toScreenCoords(Vector4 v) {
        float halfWidth = viewport.width / 2.0f;
        float halfHeight = viewport.height / 2.0f;
        return new Vector4(viewport.x + halfWidth + v.x * halfWidth, viewport.y + halfHeight + v.y * halfHeight, v.z, v.w);
    }

    public void draw() {
        Object[] vertexData = (Object[])vertexBuffer.data;

        for (int i = 0; i < vertexData.length; i += 3) {
            Vector4 v1 = program.onVertexShaderCalled(vertexData[i]);
            Vector4 v2 = program.onVertexShaderCalled(vertexData[i + 1]);
            Vector4 v3 = program.onVertexShaderCalled(vertexData[i + 2]);

            if (v1.z < 0) {
                continue;
            }

            v1.x /= v1.z;
            v1.y /= v1.z;

            v2.x /= v2.z;
            v2.y /= v2.z;

            v3.x /= v3.z;
            v3.y /= v3.z;

            fillTriangle(toScreenCoords(v1), toScreenCoords(v2), toScreenCoords(v3));
            drawTriangle(toScreenCoords(v1), toScreenCoords(v2), toScreenCoords(v3));
        }
    }

    public void drawIndexed() {
        Object[] vertexData = (Object[])vertexBuffer.data;
        Object[] indexData = (Object[])indexBuffer.data;

        for (int i = 0; i < indexData.length; i += 3) {
            Vector4 v1 = program.onVertexShaderCalled(indexData[i]);
            Vector4 v2 = program.onVertexShaderCalled(indexData[i + 1]);
            Vector4 v3 = program.onVertexShaderCalled(indexData[i + 2]);

            if (v1.z < 0) {
                continue;
            }

            v1.x /= v1.z;
            v1.y /= v1.z;

            v2.x /= v2.z;
            v2.y /= v2.z;

            v3.x /= v3.z;
            v3.y /= v3.z;

            drawTriangle(toScreenCoords(v1), toScreenCoords(v2), toScreenCoords(v3)); 
            fillTriangle(toScreenCoords(v1), toScreenCoords(v2), toScreenCoords(v3));
        }

        renderTarget.gfx.setColor(Color.blue);
        renderTarget.gfx.fillRect(0, 0, 100, 100);
    }
}
