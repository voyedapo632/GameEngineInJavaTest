package ultra3d.framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JPanel;

import sr4j.Matrix4x4;
import sr4j.OBJFace;
import sr4j.OBJModel;
import sr4j.SR4JBuffer;
import sr4j.SR4JDevice;
import sr4j.SR4JRasterizationState;
import sr4j.SR4JScissor;
import sr4j.SR4JShader;
import sr4j.SR4JShaderProgram;
import sr4j.SR4JSurfaceTarget;
import sr4j.SR4JSwapChain;
import sr4j.SR4JTexture2D;
import sr4j.SR4JViewport;
import sr4j.Vector4;
import ultra3d.util.U3DVector3f;

class ConstBuffer {
    public Matrix4x4 model;
    public Matrix4x4 view;
    public Matrix4x4 projection;
    public Matrix4x4 mvp;
    public Object[] triangleUvCoords;
    public Point mousePos;
    public OBJModel objModel;
    public Vector4 lightDir = Vector4.normalize(new Vector4(-50, 50, -20));
    public boolean isSelectionBox = false;
    public U3DMessageBox messageBox;
    public boolean isLit = true;
    public int debugView = U3DGraphicsEngine.DEBUG_VIEW_FULL;
}

class VertexShader extends SR4JShader {
    public VertexShader(SR4JDevice device) {
        super(device, SR4JShader.SHADER_TYPE_VERTEX);
    }

    @Override
    public Object main(Object in) {
        OBJFace face = (OBJFace)in;
        ConstBuffer constBuffer = (ConstBuffer)register(0);

        Vector4 outPos = (Vector4)vertexFromIndex(face.vertex);
        outPos.w = 1.0f;
        outPos = Vector4.mul(outPos, constBuffer.mvp);
        // 
        outPosition(outPos);
        outColor(new Vector4(constBuffer.objModel.normals.get(face.normal)));
        outTextureCoord(new Vector4(constBuffer.objModel.textureVertexes.get(face.texture)));

        return face;
    }
}

class GemoetryShader extends SR4JShader {
    public GemoetryShader(SR4JDevice device) {
        super(device, SR4JShader.SHADER_TYPE_GEOMETRY);
    }

    @Override
    public Object main(Object in) {
        return null;
    }
}

class FragmentShader extends SR4JShader {
    public FragmentShader(SR4JDevice device) {
        super(device, SR4JShader.SHADER_TYPE_FRAGMENT);
    }

    public boolean isCollidePoint(Vector4 vec, Point p) {
        return (p.x >= vec.x && p.x <= vec.x + vec.z) && (p.y >= vec.y && p.y <= vec.y + vec.w);
    }

    @Override
    public Object main(Object in) {
        ConstBuffer constBuffer = (ConstBuffer)register(0);
        Vector4 tex = fromTexture2D(getTextureFragPosition(), 0);
        Vector4 col = new Vector4((Vector4)in);
        col = Vector4.add(col, Vector4.mul(Vector4.sub(tex, col), 0.5f));
        col.w = 1.0f;

        float light = Vector4.dotProduct(constBuffer.lightDir, col);

        outFragColor(Vector4.clamp(Vector4.mul(tex, light + 0.7f)));

        
        if (isCollidePoint(new Vector4(getFragPosition().x, getFragPosition().y, device.rasterizationState.drawResolution + 2, device.rasterizationState.drawResolution + 2), constBuffer.mousePos)) {
            constBuffer.messageBox.appendMessage("HOVERED");
        }
        
        if (constBuffer.debugView == U3DGraphicsEngine.DEBUG_VIEW_NORMAL_VISUALIZATION_WITH_TEXTURE) {
            outFragColor(Vector4.clamp(col));
        }else if (constBuffer.debugView == U3DGraphicsEngine.DEBUG_VIEW_NORMAL_VISUALIZATION) {
            outFragColor(Vector4.clamp((Vector4)in));
        }

        if (constBuffer.isSelectionBox || !constBuffer.isLit) {
            outFragColor(device.defaultColor);
        }


        return in;
    }
}

public class U3DGraphicsEngine {
    public static final int DEBUG_VIEW_FULL = 0;
    public static final int DEBUG_VIEW_NORMAL_VISUALIZATION = 1;
    public static final int DEBUG_VIEW_NORMAL_VISUALIZATION_WITH_TEXTURE = 2;

    public JPanel viewportPanel;
    public SR4JDevice device;
    public SR4JSwapChain swapChain;
    public SR4JRasterizationState rasterizer;
    private final VertexShader vertexShader;
    private final GemoetryShader gemoetryShader;
    private final FragmentShader fragmentShader;
    public Matrix4x4 modelMatrix;
    public Matrix4x4 viewMatrix;
    public Matrix4x4 projectionMatrix;
    public U3DMessageBox messageBox;
    public int fps = 0;
    public int lastFps = 0;
    public long lastTime = System.currentTimeMillis();
    public int debugView = U3DGraphicsEngine.DEBUG_VIEW_FULL;

    public U3DGraphicsEngine(JPanel viewportPanel) {
        messageBox = new U3DMessageBox();

        // Setup viewport
        this.viewportPanel = viewportPanel;

        // Setup matrices
        modelMatrix = Matrix4x4.identity();
        viewMatrix = Matrix4x4.identity();
        projectionMatrix = Matrix4x4.identity();

        // Setup device
        device = new SR4JDevice(10);

        // Setup swap chain
        swapChain = new SR4JSwapChain(device, new SR4JSurfaceTarget(viewportPanel));
        swapChain.requestResize(520, 340);
        device.setRenderTarget(swapChain.getFrameBuffer());
        
        // Create depth stencil view
        device.createDepthStencilView(520, 340);

        // Rasterization state
        rasterizer = new SR4JRasterizationState(
            SR4JRasterizationState.FILL, 
            true, 
            true, 
            2.0f,
            SR4JRasterizationState.DEPTH_TEST_ENABLE
        );
        device.setRasterizationState(rasterizer);

        // Create shaders
        SR4JShaderProgram shaderProgram = new SR4JShaderProgram(device);

        vertexShader = new VertexShader(device);
        shaderProgram.bindShader(vertexShader);
        
        gemoetryShader = new GemoetryShader(device);
        shaderProgram.bindShader(gemoetryShader);
        
        fragmentShader = new FragmentShader(device);
        shaderProgram.bindShader(fragmentShader);

        // Use the shader program
        device.useProgram(shaderProgram);
    }

    public U3DMessageBox getMessageBox() {
        return messageBox;
    }

    public Matrix4x4 getModelMatrix() {
        return modelMatrix;
    }

    public Matrix4x4 getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4x4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setModelMatrix(Matrix4x4 matrix) {
        modelMatrix = matrix;
    }
    
    public void setViewMatrix(Matrix4x4 matrix) {
        viewMatrix = matrix;
    }

    public SR4JTexture2D getFrameBuffer() {
        return swapChain.getFrameBuffer();
    }

    public SR4JSurfaceTarget getSurfaceTarget() {
        return swapChain.getSurfaceTarget();
    }

    public void setTexture(SR4JTexture2D texture) {
        device.bindTexture(texture, 0);
    }

    public void setColor(U3DVector3f color) {
        device.setDefaultColor(new Vector4(color.x, color.y, color.z, 1.0f));
    }

    public void setColor(Color color) {
        device.setDefaultColor(new Vector4(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f));
    }

    public void setColor(Vector4 color) {
        device.setDefaultColor(color);
    }

    public void beginRender() {
        // Clear
        device.clearRenderTarget(new Vector4(0.764f, 0.878f, 0.933f, 1.0f));
        // device.clearRenderTarget(new Vector4(0.392f, 0.584f, 0.929f, 1.0f));
        // device.clearRenderTarget(new Vector4(0.0f, 0.0f, 0.0f, 1.0f));
        device.clearDepthStencilView(1.0f);

        // Set viewport and scissor
        device.setViewport(new SR4JViewport(0, 0, swapChain.getFrameBuffer().getWidth(), swapChain.getFrameBuffer().getHeight(), 0.0f, 100.0f));
        device.setScissor(new SR4JScissor(0, 0, swapChain.getFrameBuffer().getWidth(), swapChain.getFrameBuffer().getHeight()));

        // Setup projection matrix
        projectionMatrix = Matrix4x4.perspective((float)Math.toRadians(60.0), 
            (float)viewportPanel.getWidth() / (float)viewportPanel.getHeight(), 0.01f, 200);
    }

    public void drawOBJModel(OBJModel obj) {
        if (obj == null) {
            return;
        }

        messageBox.clear();

        // Create buffers
        SR4JBuffer vertexBuffer = new SR4JBuffer(obj.vertexes.toArray(), obj.vertexes.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_VERTEX);
        device.bindBuffer(vertexBuffer);
        
        SR4JBuffer indexBuffer = new SR4JBuffer(obj.faces.toArray(), obj.faces.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_INDEX);
        device.bindBuffer(indexBuffer);

        // Create const buffer
        ConstBuffer constBuffer = new ConstBuffer();

        constBuffer.debugView = debugView;
        constBuffer.objModel = obj;
        constBuffer.mousePos = viewportPanel.getMousePosition();
        constBuffer.messageBox = messageBox;
        constBuffer.isSelectionBox = false;
        constBuffer.isLit = true;

        if (constBuffer.mousePos != null) {
            constBuffer.mousePos.x = (int)(constBuffer.mousePos.x * (device.viewport.width / viewportPanel.getWidth()));
            constBuffer.mousePos.y = (int)(constBuffer.mousePos.y * (device.viewport.height / viewportPanel.getHeight()));
        } else {
            constBuffer.mousePos = new Point(0, 0);
        }

        constBuffer.model = modelMatrix;
        constBuffer.view = viewMatrix;
        constBuffer.projection = projectionMatrix;
        constBuffer.mvp = Matrix4x4.mul(Matrix4x4.mul(constBuffer.projection, constBuffer.view), constBuffer.model);
        constBuffer.triangleUvCoords = obj.textureVertexes.toArray();
        device.bindObject(constBuffer, 0);

        // Draw
        device.drawIndexed();
    }

    public void drawOBJModelSelected(OBJModel obj) {
        if (obj == null) {
            return;
        }

        Matrix4x4 selectedModelMatrix;

        selectedModelMatrix = Matrix4x4.mul(modelMatrix, Matrix4x4.scale(1.04f, 1.04f, 1.04f));
        messageBox.clear();
        int oldDepthTestMode = rasterizer.depthTestMode;
        rasterizer.depthTestMode = SR4JRasterizationState.DEPTH_TEST_INVERT;
        
        // Create buffers
        SR4JBuffer vertexBuffer = new SR4JBuffer(obj.vertexes.toArray(), obj.vertexes.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_VERTEX);
        device.bindBuffer(vertexBuffer);
        
        SR4JBuffer indexBuffer = new SR4JBuffer(obj.faces.toArray(), obj.faces.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_INDEX);
        device.bindBuffer(indexBuffer);

        // Create const buffer
        ConstBuffer constBuffer = new ConstBuffer();

        constBuffer.debugView = debugView;
        constBuffer.objModel = obj;
        constBuffer.mousePos = viewportPanel.getMousePosition();
        constBuffer.messageBox = messageBox;
        constBuffer.isSelectionBox = true;
        constBuffer.isLit = true;

        if (constBuffer.mousePos != null) {
            constBuffer.mousePos.x = (int)(constBuffer.mousePos.x * (device.viewport.width / viewportPanel.getWidth()));
            constBuffer.mousePos.y = (int)(constBuffer.mousePos.y * (device.viewport.height / viewportPanel.getHeight()));
        } else {
            constBuffer.mousePos = new Point(0, 0);
        }

        constBuffer.model = selectedModelMatrix;
        constBuffer.view = viewMatrix;
        constBuffer.projection = projectionMatrix;
        constBuffer.mvp = Matrix4x4.mul(Matrix4x4.mul(constBuffer.projection, constBuffer.view), constBuffer.model);
        constBuffer.triangleUvCoords = obj.textureVertexes.toArray();
        device.bindObject(constBuffer, 0);

        // Draw
        device.drawIndexed();

        // Draw OBJ
        rasterizer.depthTestMode = oldDepthTestMode;
        drawOBJModel(obj);
    }

    public void drawOBJModelUnlit(OBJModel obj) {
        if (obj == null) {
            return;
        }
        
        messageBox.clear();

        // Create buffers
        SR4JBuffer vertexBuffer = new SR4JBuffer(obj.vertexes.toArray(), obj.vertexes.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_VERTEX);
        device.bindBuffer(vertexBuffer);
        
        SR4JBuffer indexBuffer = new SR4JBuffer(obj.faces.toArray(), obj.faces.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_INDEX);
        device.bindBuffer(indexBuffer);

        // Create const buffer
        ConstBuffer constBuffer = new ConstBuffer();

        constBuffer.debugView = debugView;
        constBuffer.objModel = obj;
        constBuffer.mousePos = viewportPanel.getMousePosition();
        constBuffer.messageBox = messageBox;
        constBuffer.isSelectionBox = false;
        constBuffer.isLit = false;

        if (constBuffer.mousePos != null) {
            constBuffer.mousePos.x = (int)(constBuffer.mousePos.x * (device.viewport.width / viewportPanel.getWidth()));
            constBuffer.mousePos.y = (int)(constBuffer.mousePos.y * (device.viewport.height / viewportPanel.getHeight()));
        } else {
            constBuffer.mousePos = new Point(0, 0);
        }

        constBuffer.model = modelMatrix;
        constBuffer.view = viewMatrix;
        constBuffer.projection = projectionMatrix;
        constBuffer.mvp = Matrix4x4.mul(Matrix4x4.mul(constBuffer.projection, constBuffer.view), constBuffer.model);
        constBuffer.triangleUvCoords = obj.textureVertexes.toArray();
        device.bindObject(constBuffer, 0);

        // Draw
        device.drawIndexed();
    }

    public void endRender() {
        // Display FPS
        getFrameBuffer().getGraphics().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        getFrameBuffer().getGraphics().setColor(new Color(192, 128, 128));
        getFrameBuffer().getGraphics().drawChars(("SR4J      ").toCharArray(), 0, ("SR4J      ").toCharArray().length, 20, 60);
        getFrameBuffer().getGraphics().setColor(Color.WHITE);
        getFrameBuffer().getGraphics().drawChars(("" + lastFps).toCharArray(), 0, ("" + lastFps).toCharArray().length, 100, 60);
        getFrameBuffer().getGraphics().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        getFrameBuffer().getGraphics().drawChars(("FPS").toCharArray(), 0, ("FPS").toCharArray().length, 110 + 12 * ("" + lastFps).toCharArray().length, 52);

        // Viewport focus indicator
        if (viewportPanel.hasFocus()) {
            getFrameBuffer().getGraphics().setColor(new Color(1.0f, 0.75f, 0.0f));
            getFrameBuffer().getGraphics().drawRect(0, 0, swapChain.getFrameBuffer().getWidth() - 1, swapChain.getFrameBuffer().getHeight() - 1);
        }

        // Present
        swapChain.present();

        // Update FPS counter
        if (System.currentTimeMillis() - lastTime >= 1000) {
            lastFps = fps;
            fps = 0;
            lastTime = System.currentTimeMillis();
        }

        fps++;
    }

    public void setResolution(int width, int height) {
        swapChain.requestResize(width, height);
        device.createDepthStencilView(width, height);
    }

    public void validateEngine() {
        swapChain.setSurfaceTarget(new SR4JSurfaceTarget(viewportPanel));
    }
}
