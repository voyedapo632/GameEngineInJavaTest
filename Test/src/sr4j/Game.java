package sr4j;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import engine4j.util.GameWindow;

class ConstBuffer {
    public Matrix4x4 model;
    public Matrix4x4 view;
    public Matrix4x4 projection;
    public Matrix4x4 mvp;
    public Vector4[] triangleUvCoords;
    public Point mousePos;
    public OBJModel objModel;
    public Vector4 lightDir = Vector4.normalize(new Vector4(-50, 50, -20));
    public boolean isSelectionBox = false;
}

// IndexCombo indexCombo = (IndexCombo)in;
// VertexCombo vertexCombo = (VertexCombo)vertexFromIndex((int)indexCombo.position);
// Vector4 textureCoord = constBuffer.triangleUvCoords[(int)indexCombo.texturePosition];
// 
//Vector4 outPos = new Vector4(constBuffer.objModel.vertexes.get(face.vertex));
// Vector4 textureCoord = constBuffer.triangleUvCoords[(int)indexCombo.texturePosition];

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
        ConstBuffer constBuffer = (ConstBuffer)register(0);
        return null;
    }
}

class FragmentShader extends SR4JShader {
    public FragmentShader(SR4JDevice device) {
        super(device, SR4JShader.SHADER_TYPE_FRAGMENT);
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
        outFragColor(Vector4.clamp(col));

        if (getFragPosition().equals(new Vector4(constBuffer.mousePos.x, constBuffer.mousePos.y))) {
            System.out.println("HOVERED");
        }

        if (constBuffer.isSelectionBox) {
            outFragColor(new Vector4(1.0f, 0.0f, 0.0f, 1.0f));
        }

        return in;
    }
}

class VertexCombo {
    public Vector4 position;
    public Vector4 color;

    public VertexCombo(Vector4 position, Vector4 color) {
        this.position = position;
        this.color = color;
    }

    @Override
    public String toString() {
        return "(" + position.toString() + ")" + ", (" + color.toString() + ")";
    }
}

class IndexCombo {
    public long position;
    public long texturePosition;

    public IndexCombo(long position, long texturePosition) {
        this.position = position;
        this.texturePosition = texturePosition;
    }

    @Override
    public String toString() {
        return position + ", " + texturePosition;
    }
}

public class Game extends GameWindow {
    ArrayList<Integer> keys = new ArrayList<>();
    SR4JDevice device;
    SR4JSwapChain swapChain;
    VertexShader vertexShader;
    GemoetryShader gemoetryShader;
    FragmentShader fragmentShader;
    SR4JBuffer vertexBuffer;
    SR4JBuffer indexBuffer;
    SR4JRasterizationState rasterizer;
    Camera3D cam;
    OBJModel objModel;

    int fps = 0;
    int lastFps = 0;
    long lastTime = System.currentTimeMillis();
    VertexCombo[] triangleVerticies = {
        new VertexCombo(new Vector4(-1.0f, -1.0f, -1.0f), new Vector4(0.0f, 0.0f, 0.0f)),
        new VertexCombo(new Vector4(-1.0f, -1.0f, 1.0f),  new Vector4(0.0f, 0.0f, 1.0f)),
        new VertexCombo(new Vector4(-1.0f, 1.0f, -1.0f),  new Vector4(0.0f, 1.0f, 0.0f)),
        new VertexCombo(new Vector4(-1.0f, 1.0f, 1.0f),   new Vector4(0.0f, 1.0f, 1.0f)),
        new VertexCombo(new Vector4(1.0f, -1.0f, -1.0f),  new Vector4(1.0f, 0.0f, 0.0f)),
        new VertexCombo(new Vector4(1.0f, -1.0f, 1.0f),   new Vector4(1.0f, 0.0f, 1.0f)),
        new VertexCombo(new Vector4(1.0f, 1.0f, -1.0f),   new Vector4(1.0f, 1.0f, 0.0f)),
        new VertexCombo(new Vector4(1.0f, 1.0f, 1.0f),    new Vector4(1.0f, 1.0f, 1.0f))
    };

    Vector4[] triangleUvCoords = {
        // Side
        new Vector4(0.0f, 0.5f),
        new Vector4(0.0f, 0.0f),
        new Vector4(0.5f, 0.0f),
        new Vector4(0.5f, 0.5f),

        // Top
        new Vector4(0.5f, 0.5f),
        new Vector4(0.0f, 0.5f),
        new Vector4(0.0f, 1.0f),
        new Vector4(0.5f, 1.0f),

        // Bottom
        new Vector4(0.5f, 0.5f),
        new Vector4(0.5f, 0.0f),
        new Vector4(1.0f, 0.0f),
        new Vector4(1.0f, 0.5f)
    };

    IndexCombo[] indices = {
        new IndexCombo(0, 1), new IndexCombo(2, 0), new IndexCombo(1, 2),
        new IndexCombo(1, 2), new IndexCombo(2, 0), new IndexCombo(3, 3),

        new IndexCombo(4, 2), new IndexCombo(5, 1), new IndexCombo(6, 3),
        new IndexCombo(5, 1), new IndexCombo(7, 0), new IndexCombo(6, 3),

        new IndexCombo(0, 8), new IndexCombo(1, 9), new IndexCombo(5, 10),
        new IndexCombo(0, 8), new IndexCombo(5, 10), new IndexCombo(4, 11),

        new IndexCombo(2, 4), new IndexCombo(6, 5), new IndexCombo(7, 6),
        new IndexCombo(2, 4), new IndexCombo(7, 6), new IndexCombo(3, 7),

        new IndexCombo(0, 1), new IndexCombo(4, 2), new IndexCombo(6, 3),
        new IndexCombo(0, 1), new IndexCombo(6, 3), new IndexCombo(2, 0),

        new IndexCombo(1, 2), new IndexCombo(3, 3), new IndexCombo(7, 0),
        new IndexCombo(1, 2), new IndexCombo(7, 0), new IndexCombo(5, 1)
    };

    Point lastMouse = new Point(0, 0);
    boolean mouseDown = false;

    public Game() {
        super(1200, 700, "3D Software Renderer");

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (!keys.contains(keyCode)) {
                    keys.add(keyCode);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (int i = 0; i < keys.size(); i++) {
                    Integer key = keys.get(i);

                    if (key.equals(e.getKeyCode())) {
                        keys.remove(i);
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    lastMouse = e.getPoint();
                    mouseDown = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDown = false;
            }
        });
    }

    @Override
    protected void onInit() {
        // Setup device
        device = new SR4JDevice(10);

        // Setup swap chain
        swapChain = new SR4JSwapChain(device, new SR4JSurfaceTarget(this));
        swapChain.requestResize(1280, 720);
        device.setRenderTarget(swapChain.getFrameBuffer());
        
        // Create depth stencil view
        device.createDepthStencilView(1280, 720);

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
        
        // Create camera
        cam = new Camera3D();
        
        // Create 3d Model
        objModel = new OBJModel("C:\\Users\\kmhjh\\OneDrive\\Desktop\\untitledgasdasdasdfasdsdfsdfsdffsdfdsfsdfsdfsdfsdfdfsdf.obj");
        objModel.parse();

        // Create the cube image
        BufferedImage source = null;

        try {
            //source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fdsfsdfdss.png"));
            source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fdsfsdfsdsd.png"));
            //source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\dfdsfds.png"));
        } catch (IOException e) {

        }

        if (source != null) {
            cubeTexture = new SR4JTexture2D(source);
        }
    }

    float yawRot = 0.0f;
    SR4JTexture2D cubeTexture = null;

    public void inputCallback() {
        if (mouseDown) {
            if (!getMousePosition().equals(lastMouse)) { 
                int dx = (int)getMousePosition().getX() - (int)lastMouse.getX();
                int dy = (int)getMousePosition().getY() - (int)lastMouse.getY();
                Robot robot = null;

                try {
                    robot = new Robot();
                }  catch (AWTException e) {
                    System.out.println(e);
                }
                
                if (robot != null) {
                    robot.mouseMove(getX() + (int)lastMouse.getX(), getY() + (int)lastMouse.getY());
                }

                lastMouse = getMousePosition();
                //System.out.println("END: " + e.getPoint());
                cam.yaw += dx * cam.cameraSencitivity;
                cam.pitch -= dy * cam.cameraSencitivity;
            }
        }

        if (keys.contains(KeyEvent.VK_W)) {
            cam.moveForward(1);
        } else if (keys.contains(KeyEvent.VK_S)) {
            cam.moveForward(-1);
        }

        if (keys.contains(KeyEvent.VK_A)) {
            cam.moveRight(-1);
        } else if (keys.contains(KeyEvent.VK_D)) {
            cam.moveRight(1);
        }

        if (keys.contains(KeyEvent.VK_Q)) {
            cam.moveUp(-1);
        } else if (keys.contains(KeyEvent.VK_E)) {
            cam.moveUp(1);
        }

        if (keys.contains(KeyEvent.VK_LEFT)) {
            yawRot -= 10;
        } else if (keys.contains(KeyEvent.VK_RIGHT)) {
            yawRot += 10;
        }

        if (keys.contains(KeyEvent.VK_DOWN)) {
            cam.turnUp(-1);
        } else if (keys.contains(KeyEvent.VK_UP)) {
            cam.turnUp(1);
        }
    }

    @Override
    protected void onTick() {
        // Call input
        inputCallback();

        // Clear
        device.clearRenderTarget(new Vector4(0.392f, 0.584f, 0.929f, 1.0f));
        //device.clearRenderTarget(new Vector4(0.0f, 0.0f, 0.0f, 1.0f));
        device.clearDepthStencilView(1.0f);

        // Set viewport and scissor
        device.setViewport(new SR4JViewport(0, 0, swapChain.getFrameBuffer().getWidth(), swapChain.getFrameBuffer().getHeight(), 0.0f, 100.0f));
        device.setScissor(new SR4JScissor(0, 0, getWidth(), getHeight()));
        
        // Update const buffer
        cam.update();

        // Create buffers
        vertexBuffer = new SR4JBuffer(objModel.vertexes.toArray(), objModel.vertexes.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_VERTEX);
        device.bindBuffer(vertexBuffer);
        
        indexBuffer = new SR4JBuffer(objModel.faces.toArray(), objModel.faces.size(), 1, 0, SR4JBuffer.BUFFER_TYPE_INDEX);
        device.bindBuffer(indexBuffer);

        // Create const buffer
        ConstBuffer constBuffer = new ConstBuffer();
        constBuffer.objModel = objModel;
        constBuffer.mousePos = getMousePosition();

        if (constBuffer.mousePos != null) {
            constBuffer.mousePos.x = (int)(constBuffer.mousePos.x * (device.viewport.width / getWidth()));
            constBuffer.mousePos.y = (int)(constBuffer.mousePos.y * (device.viewport.height / getHeight()));
        } else {
            constBuffer.mousePos = new Point(0, 0);
        }

        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < 0; i++) {
                rasterizer.depthTestMode = SR4JRasterizationState.DEPTH_TEST_INVERT;
                constBuffer.isSelectionBox = true;
                constBuffer.model = Matrix4x4.translation(-5.0f * i, 0.0f, -5.0f * j);
                constBuffer.model = Matrix4x4.mul(constBuffer.model, Matrix4x4.scale(1.02f, 1.02f, 1.02f));
                constBuffer.model = Matrix4x4.mul(constBuffer.model, Matrix4x4.rotateY((float)Math.toRadians(yawRot)));
                constBuffer.view = cam.viewMatrix;
                    constBuffer.projection = Matrix4x4.perspective((float)Math.toRadians(60.0), 
                    (float)getWidth() / (float)getHeight(), 0.01f, 200);
                    
                    constBuffer.mvp = Matrix4x4.mul(
                        Matrix4x4.mul(constBuffer.projection, constBuffer.view), 
                        constBuffer.model);
                        
                        constBuffer.triangleUvCoords = triangleUvCoords;
                        device.bindObject(constBuffer, 0);
                        
                        if (cubeTexture != null) {
                    device.bindTexture(cubeTexture, 0);
                    
                    // Draw
                    device.drawIndexed();
                }
            }
        }
        
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                rasterizer.depthTestMode = SR4JRasterizationState.DEPTH_TEST_ENABLE;
                constBuffer.isSelectionBox = false;
                constBuffer.model = Matrix4x4.translation(-10.0f * i, 0.0f, -10.0f * j);
                constBuffer.model = Matrix4x4.mul(constBuffer.model, Matrix4x4.rotateY((float)Math.toRadians(yawRot)));
                constBuffer.view = Matrix4x4.lookAt(
                    cam.cameraPos,
                    Vector4.add(cam.cameraPos, cam.cameraFront),
                    cam.cameraUp
                );
                constBuffer.projection = Matrix4x4.perspective((float)Math.toRadians(60.0), 
                    (float)getWidth() / (float)getHeight(), 0.01f, 200);

                constBuffer.mvp = Matrix4x4.mul(
                    Matrix4x4.mul(constBuffer.projection, constBuffer.view), 
                    constBuffer.model);
            
                constBuffer.triangleUvCoords = triangleUvCoords;
                device.bindObject(constBuffer, 0);

                if (cubeTexture != null) {
                    device.bindTexture(cubeTexture, 0);
                    
                    // Draw
                    device.drawIndexed();
                }
            }
        }

        // Display FPS
        swapChain.getFrameBuffer().getGraphics().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        swapChain.getFrameBuffer().getGraphics().setColor(new Color(192, 128, 128));
        swapChain.getFrameBuffer().getGraphics().drawChars(("SR4J      ").toCharArray(), 0, ("SR4J      ").toCharArray().length, 20, 60);
        swapChain.getFrameBuffer().getGraphics().setColor(Color.WHITE);
        swapChain.getFrameBuffer().getGraphics().drawChars(("" + lastFps).toCharArray(), 0, ("" + lastFps).toCharArray().length, 100, 60);
        swapChain.getFrameBuffer().getGraphics().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        swapChain.getFrameBuffer().getGraphics().drawChars(("FPS").toCharArray(), 0, ("FPS").toCharArray().length, 110 + 12 * ("" + lastFps).toCharArray().length, 52);

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
    
    @Override
    protected void onResized() {
        swapChain.setSurfaceTarget(new SR4JSurfaceTarget(this));
        //swapChain.requestResize(512, 256);
        //device.createDepthStencilView(getWidth(), getHeight());
    }

    @Override
    protected void onDestroy() {
        
    }
}

