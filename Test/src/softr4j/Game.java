package softr4j;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game extends GameWindow {
    Device device;
    SwapChain swapChain;
    long lastTime = System.currentTimeMillis();
    int fps = 0;
    Camera3D cam = new Camera3D();

    VERTEX_COMBO[] triangleVerticies = {
        new VERTEX_COMBO(new Vector4(-1.0f, -1.0f, -1.0f), new Vector4(0.0f, 0.0f, 0.0f)),
        new VERTEX_COMBO(new Vector4(-1.0f, -1.0f, 1.0f),  new Vector4(0.0f, 0.0f, 1.0f)),
        new VERTEX_COMBO(new Vector4(-1.0f, 1.0f, -1.0f),  new Vector4(0.0f, 1.0f, 0.0f)),
        new VERTEX_COMBO(new Vector4(-1.0f, 1.0f, 1.0f),   new Vector4(0.0f, 1.0f, 1.0f)),
        new VERTEX_COMBO(new Vector4(1.0f, -1.0f, -1.0f),  new Vector4(1.0f, 0.0f, 0.0f)),
        new VERTEX_COMBO(new Vector4(1.0f, -1.0f, 1.0f),   new Vector4(1.0f, 0.0f, 1.0f)),
        new VERTEX_COMBO(new Vector4(1.0f, 1.0f, -1.0f),   new Vector4(1.0f, 1.0f, 0.0f)),
        new VERTEX_COMBO(new Vector4(1.0f, 1.0f, 1.0f),    new Vector4(1.0f, 1.0f, 1.0f))
    };

    Vector4 triangleUvCoords[] =
    {
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

    INDEX_COMBO[] indices = {
        new INDEX_COMBO(0, 1), new INDEX_COMBO(2, 0), new INDEX_COMBO(1, 2),
        new INDEX_COMBO(1, 2), new INDEX_COMBO(2, 0), new INDEX_COMBO(3, 3),

        new INDEX_COMBO(4, 2), new INDEX_COMBO(5, 1), new INDEX_COMBO(6, 3),
        new INDEX_COMBO(5, 1), new INDEX_COMBO(7, 0), new INDEX_COMBO(6, 3),

        new INDEX_COMBO(0, 8), new INDEX_COMBO(1, 9), new INDEX_COMBO(5, 10),
        new INDEX_COMBO(0, 8), new INDEX_COMBO(5, 10), new INDEX_COMBO(4, 11),

        new INDEX_COMBO(2, 4), new INDEX_COMBO(6, 5), new INDEX_COMBO(7, 6),
        new INDEX_COMBO(2, 4), new INDEX_COMBO(7, 6), new INDEX_COMBO(3, 7),

        new INDEX_COMBO(0, 1), new INDEX_COMBO(4, 2), new INDEX_COMBO(6, 3),
        new INDEX_COMBO(0, 1), new INDEX_COMBO(6, 3), new INDEX_COMBO(2, 0),

        new INDEX_COMBO(1, 2), new INDEX_COMBO(3, 3), new INDEX_COMBO(7, 0),
        new INDEX_COMBO(1, 2), new INDEX_COMBO(7, 0), new INDEX_COMBO(5, 1)
    };

    class ShadersClass extends ShaderProgram {
        public class ConstBufferInput {
            public Matrix4x4 model;
            public Matrix4x4 view;
            public Matrix4x4 projection;
            public Matrix4x4 mvp;
        }

        public ConstBufferInput constBuffer = new ConstBufferInput();

        @Override
        public Vector4 onVertexShaderCalled(Object input) {
            Vector4 in = triangleVerticies[((INDEX_COMBO)input).vertex].pos;
            Vector4 pos = new Vector4(in.x, in.y, in.z, 1.0f);

            pos = Vector4.mul(pos, constBuffer.mvp);

            return pos;
        }

        @Override
        public void onGeometryShaderCalled(Object[] input) {

        }

        @Override
        public Vector4 onPixelShaderCalled(int x, int y) {
            return new Vector4(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    ShadersClass shaderProgram = new ShadersClass();

    ArrayList<Integer> keys = new ArrayList<>();

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
    }

    @Override
    protected void onInit() {
        // Create device
        device = new Device();

        // Create swap chain
        swapChain = new SwapChain(getGraphics(), getSize(), getSize());
        device.setFrameBuffer(swapChain.getFrameBuffer());
        device.setShaderProgram(shaderProgram);
    }

    float x = 0.0f;
    @Override
    protected void onTick() {
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
            cam.turnRight(-1);
        } else if (keys.contains(KeyEvent.VK_RIGHT)) {
            cam.turnRight(1);
        }

        if (keys.contains(KeyEvent.VK_DOWN)) {
            cam.turnUp(-1);
        } else if (keys.contains(KeyEvent.VK_UP)) {
            cam.turnUp(1);
        }

        // Clear the screen
        device.clearRenderTarget(Color.BLUE.getRGB());

        // Set viewport
        Viewport softr4jViewport = new Viewport(0, getHeight(), getWidth(), -getHeight(), 0, 0);
        device.setViewport(softr4jViewport);

        // Set primitive type
        device.setPrimitiveTopology(PrimitiveTopology.TRIANGLES);

        // Set rasterization state
        RasterizationState rasterizer = new RasterizationState(RasterizationState.FILL, true, true, 8.0f);
        device.setRasterizationState(rasterizer);

        //swapChain.getFrameBuffer().data.getGraphics().setColor(Color.white);
        //swapChain.getFrameBuffer().data.getGraphics().fillRect(450, 200, 300, 300);

        //for (int i = 0; i < 200; i++) {
        //    for (int j = 0; j < 200; j++) {
        //        for (int k = 0; k < 10; k++) {
        //            double a = Math.sin(20.0);
        //            double b = Math.cos(20.0);
        //        }
        //    }
        //}
        
        // Setup vertex buffer
        softr4j.Buffer vertexBuffer = new softr4j.Buffer(BindFlag.VERTEX_BUFFER, triangleVerticies, triangleVerticies.length, 0, 0);
        device.bindBuffer(vertexBuffer);

        // Setup index buffer
        softr4j.Buffer indexBuffer = new softr4j.Buffer(BindFlag.INDEX_BUFFER, indices, indices.length, 0, 0);
        device.bindBuffer(indexBuffer);

        //Vector4 direction = new Vector4((float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch)),
        //                                   (float)Math.sin(Math.toRadians(pitch)),
        //                                   (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch)), 0.0f);
//
        //cameraFront = Vector4.normalize(direction);
        cam.update();
        // Update const buffer
        shaderProgram.constBuffer.model = Matrix4x4.translation(0.0f, 0.0f, 0.0f);
        shaderProgram.constBuffer.model = Matrix4x4.mul(shaderProgram.constBuffer.model, Matrix4x4.reotateX(x));
        shaderProgram.constBuffer.model = Matrix4x4.mul(shaderProgram.constBuffer.model, Matrix4x4.reotateY(x));
        shaderProgram.constBuffer.view = Matrix4x4.lookAt(
            cam.cameraPos,
            Vector4.add(cam.cameraPos, cam.cameraFront),
            cam.cameraUp
        );
        shaderProgram.constBuffer.projection = Matrix4x4.perspective((float)Math.toRadians(60.0), 
            (float)getWidth() / (float)getHeight(), 0.1f, 200);
        shaderProgram.constBuffer.mvp = Matrix4x4.mul(
            Matrix4x4.mul(shaderProgram.constBuffer.projection, shaderProgram.constBuffer.view), 
            shaderProgram.constBuffer.model);

        //shaderProgram.constBuffer.model = Matrix4x4.mul(shaderProgram.constBuffer.model, Matrix4x4.translation(x, 0.0f, 3.0f));

        // Preform draw call
        device.drawIndexed();
        
        // Present to the window
        swapChain.present();
        
        //x += 0.003;

        if (System.currentTimeMillis() - lastTime >= 1000) {
            System.out.println(fps);
            fps = 0;
            lastTime = System.currentTimeMillis();
        }

        fps++;
    }
    
    @Override
    protected void onResized() {
        swapChain.requestValidation(getGraphics(), getSize(), getSize());
    }

    @Override
    protected void onDestroy() {
        
    }
}
