package engine4j.MarkDownParser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import engine4j.util.EasyFiles;
import engine4j.util.GameWindow;
import engine4j.util.SafeList;
import softr4j.BindFlag;
import softr4j.Camera3D;
import softr4j.DepthStencilView;
import softr4j.Device;
import softr4j.INDEX_COMBO;
import softr4j.Matrix4x4;
import softr4j.PrimitiveTopology;
import softr4j.RasterizationState;
import softr4j.ShaderProgram;
import softr4j.SwapChain;
import softr4j.Texture2D;
import softr4j.VERTEX_COMBO;
import softr4j.Vector4;
import softr4j.Viewport;

// static final String TINT = "TInt";
// static final String TFLOAT = "TFloat";
// static final String TDOUBLE = "TDouble";
// static final String TSTRING = "TString";
// static final String TVECTOR2 = "TVector2";
// static final String TVECTOR3 = "TVector3";
// static final String TGAMESCRIPT = "TGameScript";
// static final String TARRAY = "TArray";
// static final String TMESH = "TMesh";
// static final String TTEXTURE = "TTexture";

class TVector2 {
    float x, y;

    public TVector2() {
        this.x = 0;
        this.y = 0;
    }

    public TVector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

class TVector3 {
    float x, y, z;

    public TVector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public TVector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

class TGameScript {
    String name;
    Object parentInstance;

    public TGameScript(String name, Object parentInstance) {
        this.name = name;
        this.parentInstance = parentInstance;
    }

    public void onStart() {
        System.out.println("GameScript :" + name + ": was started");
    }

    public void onUpdate() {
        System.out.println("GameScript :" + name + ": was updated");
    }

    // Called before
    public void onRenderBegin() {
        System.out.println("GameScript :" + name + ": was updated on render begin");
    }

    public void onRenderEnd() {
        System.out.println("GameScript :" + name + ": was updated on render end");
    }

    public void onExit() {
        System.out.println("GameScript :" + name + ": has exited");
    }

    public void onKeyEvent() {
        System.out.println("GameScript :" + name + ": detected key event");
    }

    public void onMouseEvent() {
        System.out.println("GameScript :" + name + ": detected mouse event");
    }

    public void onCollided() {
        System.out.println("GameScript :" + name + ": detected entity collision");
    }
}

class CAttribute {
    public String type;
    public String id;
    public Object value;
    public boolean canEdit;
    public boolean canSave;

    public CAttribute(String type, String id, Object value) {
        this.type = type;
        this.id = id;
        this.value = value;
        canEdit = true;
        canSave = false;
    }

    public CAttribute(String type, String id, Object value, boolean canEdit, boolean canSave) {
        this.type = type;
        this.id = id;
        this.value = value;
        this.canEdit = canEdit;
        this.canSave = canSave;
    }

    static final String TINT = "TInt";
    static final String TFLOAT = "TFloat";
    static final String TDOUBLE = "TDouble";
    static final String TSTRING = "TString";
    static final String TARRAY = "TArray";
    static final String TVECTOR2 = "TVector2";
    static final String TVECTOR3 = "TVector3";
    static final String TGAMESCRIPT = "TGameScript";
    static final String TMESH = "TMesh";
    //static final String TTEXTURE = "TTexture";
}

class CAttributeRegistry extends SafeList<CAttribute> {
    public CAttributeRegistry() {
        super();
    }

    public CAttributeRegistry(int allocatedSize) {
        super(allocatedSize);
    }

    public CAttribute getAttribute(String id) {
        for (int i = 0; i < this.getSize(); i++) {
            CAttribute a = this.get(i);

            if (a.id.equals(id)) {
                return a;
            }
        }

        return null;
    }

    public void addUniqueAttribute(CAttribute attribute) {
        for (int i = 0; i < this.getSize(); i++) {
            CAttribute a = this.get(i);

            if (a.id.equals(attribute.id)) {
                return;
            }
        }

        this.add(attribute);
    }
}

class EComponent {
    public String type; // User defined may be any valid string
    public CAttributeRegistry attributes;
}

class EComponentRegistry extends SafeList<EComponent> {
    public EComponentRegistry() {
        super();
    }

    public EComponentRegistry(int allocatedSize) {
        super(allocatedSize);
    }

    // Obtains a component if it exists
    public EComponent getComponentByType(String type) {
        for (int i = 0; i < this.getSize(); i++) {
            EComponent c = this.get(i);

            if (c.type.equals(type)) {
                return c;
            }
        }

        return null;
    }

    // Adds a component if it doesn't exist
    public void addUniqueComponent(EComponent component) {
        for (int i = 0; i < this.getSize(); i++) {
            EComponent c = this.get(i);

            if (c.type.equals(component.type)) {
                return;
            }
        }

        this.add(component);
    }
}

class TransformComponentWrapper {
    public TVector3 translation;
    public TVector3 scale;
    public TVector3 rotation;

    public TransformComponentWrapper() {
        translation = new TVector3();
        scale = new TVector3();
        rotation = new TVector3();
    }
}

// Updates to the Transformated entity projected to the screen;
class ScreenSpaceWrapper {
    public TVector2 position;
    public TVector2 scale;
}

class ScriptableEntity {
    String id;
    String name;
    String parent;
    boolean canSave;
    boolean canSelect;
    boolean isVisible; // Determines if the entity is visible inside of the World Viewport of the editor
    EComponentRegistry components;
    GameStateManager manager;

    // Pointers to potential components
    Mesh mesh;
    SafeList<TGameScript> scripts; // Stores the scripts to avoid refinding
    TransformComponentWrapper transform; // Stores the found transformation to avoid refinding it
    ScreenSpaceWrapper screenSpace; // Stores the position of an object in screen space

    public ScriptableEntity(String id, String name, String parent, boolean canSave, boolean canSelect, boolean isVisible) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.canSave = canSave;
        this.canSelect = canSelect;
        this.isVisible = isVisible;
        components = new EComponentRegistry();
        scripts = new SafeList<TGameScript>();
        transform = new TransformComponentWrapper();
        screenSpace = new ScreenSpaceWrapper();
        mesh = new Mesh();
    }

    public void onInit(GameStateManager manager) {
        this.manager = manager;
    }

    public void onUpdate() {
        
    }
}

class Mesh {
    String source;
    public VERTEX_COMBO[] vertices;
    public INDEX_COMBO[] indices;
    public Vector4[] textureCoords;
    public Vector4[] normals;
    public Vector4 color = new Vector4(1.0f, 1.0f, 1.0f);

    public Mesh() {
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
        vertices = triangleVerticies;

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
        textureCoords = triangleUvCoords;

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

        this.indices = indices;
    }

    public void loadFromOBJFormat(String sourcePath) {

    }
}

class EngineRenderer {
    private Device device;
    private SwapChain swapChain;
    private Viewport viewport;
    public Camera3D cam;
    public Matrix4x4 model = Matrix4x4.identity();
    public Matrix4x4 view = Matrix4x4.identity();
    public Matrix4x4 projection = Matrix4x4.identity();
    Dimension frameBufferDimensions;
    DepthStencilView depthStencilView;
    public TransformComponentWrapper transformation = new TransformComponentWrapper();
    DefaultShaderProgram shaderProgram = new DefaultShaderProgram();
    
    public class ConstBufferInput {
        public Matrix4x4 model;
        public Matrix4x4 view;
        public Matrix4x4 projection;
        public Matrix4x4 mvp;
        public Mesh mesh;
        public Texture2D activeTexture;
    }
    
    public class DefaultShaderProgram extends ShaderProgram {
        public ConstBufferInput constBuffer = new ConstBufferInput();

        @Override
        public Vector4 onVertexShaderCalled(Object input) {
            Vector4 in = constBuffer.mesh.vertices[((INDEX_COMBO)input).vertex].pos;
            Vector4 pos = new Vector4(in.x, in.y, in.z, 1.0f);

            pos = Vector4.mul(pos, constBuffer.mvp);

            return pos;
        }

        @Override
        public void onGeometryShaderCalled(Object[] input) {

        }

        @Override
        public Vector4 onPixelShaderCalled(int x, int y) {
            float halfWidth = frameBufferDimensions.width / 2.0f;
            float halfHeight = frameBufferDimensions.height / 2.0f;
            float nx = (float)x / (float)frameBufferDimensions.width;
            float ny = (float)y / (float)frameBufferDimensions.height;
            return new Vector4(nx, ny, constBuffer.mesh.color.z, 1.0f);
        }
    };

    public EngineRenderer(Graphics targetGraphics, Dimension targetDimensions, Dimension frameBufferDimensions) {
        this.frameBufferDimensions = frameBufferDimensions;
        device = new Device();
        swapChain = new SwapChain(targetGraphics, targetDimensions, frameBufferDimensions);
        device.setFrameBuffer(swapChain.getFrameBuffer());
        depthStencilView = new DepthStencilView(frameBufferDimensions);
        device.setDepthStencil(depthStencilView);
        device.setShaderProgram(shaderProgram);
        viewport = new Viewport(0.0f, 0.0f, (float)targetDimensions.getWidth(), (float)targetDimensions.getHeight(), 0.0f, 0.0f);
        cam = new Camera3D();
        transformation.scale.x = 1.0f;
        transformation.scale.y = 1.0f;
        transformation.scale.z = 1.0f;
    }
    
    public void setTransformation(TransformComponentWrapper transformation) {
        this.transformation = transformation;
    }

    public void loadTexture(Texture2D texture) {
        shaderProgram.constBuffer.activeTexture = texture;
    }
    
    public void startRender() {
        // Clear the screen
        device.clearRenderTarget(Color.black.getRGB());
        device.clearDepthStencile(0.0f);
    }

    public void renderMesh(Mesh mesh) {
        shaderProgram.constBuffer.mesh = mesh;

        // Set viewport
        Viewport softr4jViewport = new Viewport(0, (int)frameBufferDimensions.getHeight(), (int)frameBufferDimensions.getWidth(), -(int)frameBufferDimensions.getHeight(), 0, 0);
        device.setViewport(softr4jViewport);

        // Set primitive type
        device.setPrimitiveTopology(PrimitiveTopology.TRIANGLES);

        // Set rasterization state
        RasterizationState rasterizer = new RasterizationState(RasterizationState.FILL, true, true, 4.0f);
        device.setRasterizationState(rasterizer);
        
        // Setup vertex buffer
        softr4j.Buffer vertexBuffer = new softr4j.Buffer(BindFlag.VERTEX_BUFFER, mesh.vertices, mesh.vertices.length, 0, 0);
        device.bindBuffer(vertexBuffer);

        // Setup index buffer
        softr4j.Buffer indexBuffer = new softr4j.Buffer(BindFlag.INDEX_BUFFER, mesh.indices, mesh.indices.length, 0, 0);
        device.bindBuffer(indexBuffer);
        
        // Update const buffer
        cam.update();
        shaderProgram.constBuffer.model = Matrix4x4.translation(transformation.translation.x, transformation.translation.y, transformation.translation.z);
        shaderProgram.constBuffer.model = Matrix4x4.mul(shaderProgram.constBuffer.model, Matrix4x4.scale(transformation.scale.x, transformation.scale.y, transformation.scale.z));
        shaderProgram.constBuffer.model = Matrix4x4.mul(shaderProgram.constBuffer.model, Matrix4x4.reotateX(transformation.rotation.x));
        shaderProgram.constBuffer.model = Matrix4x4.mul(shaderProgram.constBuffer.model, Matrix4x4.reotateY(transformation.rotation.y));
        shaderProgram.constBuffer.model = Matrix4x4.mul(shaderProgram.constBuffer.model, Matrix4x4.reotateX(transformation.rotation.z));
        shaderProgram.constBuffer.view = Matrix4x4.lookAt(
            cam.cameraPos,
            Vector4.add(cam.cameraPos, cam.cameraFront),
            cam.cameraUp
        );
        shaderProgram.constBuffer.projection = Matrix4x4.perspective((float)Math.toRadians(60.0), 
            (float)frameBufferDimensions.getWidth() / (float)frameBufferDimensions.getHeight(), 0.1f, 200);
        shaderProgram.constBuffer.mvp = Matrix4x4.mul(
            Matrix4x4.mul(shaderProgram.constBuffer.projection, shaderProgram.constBuffer.view), 
            shaderProgram.constBuffer.model);

        // Preform draw call
        device.drawIndexed();
    }

    public void endRender() {
        // Present to the window
        swapChain.present();
    }

    public void updateSwapChain(Graphics targetGraphics, Dimension targetDimensions, Dimension frameBufferDimensions) {
        this.frameBufferDimensions = frameBufferDimensions;
        depthStencilView.resize(frameBufferDimensions);
        swapChain.requestValidation(targetGraphics, targetDimensions, frameBufferDimensions);
        viewport = new Viewport(0.0f, 0.0f, (float)targetDimensions.getWidth(), (float)targetDimensions.getHeight(), 0.0f, 0.0f);
    }
}

class GameLevel {
    public String name;
    private String projectPath;
    public SafeList<ScriptableEntity> entityRegistry;
    public TGameScript levelEventScript;

    GameLevel(String projectPath) {
        name = "default.xml";
        this.projectPath = projectPath;
        entityRegistry = new SafeList<ScriptableEntity>();
        levelEventScript = new TGameScript("default-script", this);
    }

    public void openLevel(String projectPath, String levelName) {
        entityRegistry.clear();
        name = levelName;
        this.projectPath = projectPath;
    }

    public void addEntity(ScriptableEntity e) {
        entityRegistry.add(e);
    }

    public void removeEntity(String entityId) {
        for (int i = 0; i < entityRegistry.getSize(); i++) {
            ScriptableEntity e = entityRegistry.get(i);

            if (e.id.equals(entityId)) {
                entityRegistry.remove(i);
                break;
            }
        }
    }

    public ScriptableEntity getEntity(String entityId) {
        for (int i = 0; i < entityRegistry.getSize(); i++) {
            ScriptableEntity e = entityRegistry.get(i);

            if (e.id.equals(entityId)) {
                return entityRegistry.get(i);
            }
        }

        return null;
    }

    public void saveLevel() {

    }

    public void clearLevel() {
        entityRegistry.clear();
    }
}

class StateRequest {
    public int type;
    public Object[] args;

    public StateRequest(int type, Object[] args) {
        this.type = type;
        this.args = args;
    }

    public StateRequest(int type) {
        this.type = type;
        this.args = null;
    }

    public static final int REFRESH_LEVEL = 0;
    public static final int LOAD_LEVEL = 1;
    public static final int REMOVE_ENTITY = 2;
    public static final int ADD_ENTITY = 3;
    public static final int REMOVE_COMPONENT = 4;
    public static final int ADD_COMPONENT = 5;
    public static final int RMEOVE_ATTRIBUTE = 6;
    public static final int ADD_ATTRIBUTE = 7;
    public static final int FADE_IN = 8;
    public static final int FADE_OUT = 9;
}

class StateRequestQueue {
    SafeList<StateRequest> requestRegistry;
    
    public StateRequestQueue() {
        requestRegistry = new SafeList<StateRequest>();
    }

    public void submitRequest(StateRequest request) {
        requestRegistry.add(request);
    }

    public boolean getNextRequest(StateRequest dest) {
        if (requestRegistry.getSize() <= 0) {
            return false;
        }

        dest.type = requestRegistry.get(0).type;
        dest.args = requestRegistry.get(0).args;
        requestRegistry.remove(0);
        return true;
    }
}

class GameStateManager {
    public String currentProject;
    private JPanel viewportPanel;
    private TGameScript globalGameEventScript;
    public EngineRenderer renderer;
    public GameLevel currentLevel;
    private StateRequestQueue requestQueue;
    public SafeList<Mesh> loadedMeshes;
    public SafeList<Texture2D> loadedTextures;
    public SafeList<TGameScript> loadedScripts;

    public GameStateManager(String currentProject, JPanel viewportPanel) {
        this.currentProject = currentProject;
        this.viewportPanel = viewportPanel;
        renderer = new EngineRenderer(viewportPanel.getGraphics(), viewportPanel.getSize(), viewportPanel.getSize());
        currentLevel = new GameLevel(currentProject);
        requestQueue = new StateRequestQueue();
        loadedMeshes = new SafeList<Mesh>();
        loadedTextures = new SafeList<Texture2D>();
        loadedScripts = new SafeList<TGameScript>();
    }

    public void requestLevelRefresh() {
        requestQueue.submitRequest(new StateRequest(StateRequest.REFRESH_LEVEL));
    }

    public void requestLoadLevel(String levelPath) {
        requestQueue.submitRequest(new StateRequest(StateRequest.LOAD_LEVEL, new Object[] { levelPath }));
    }

    public void requestRemoveEntity(String id) {
        requestQueue.submitRequest(new StateRequest(StateRequest.REMOVE_ENTITY, new Object[] { id }));
    }

    public void requestAddEntity(ScriptableEntity e) {
        requestQueue.submitRequest(new StateRequest(StateRequest.ADD_ENTITY, new Object[] { e }));
    }

    public void requestRemoveComponent(String comonentType) {
        requestQueue.submitRequest(new StateRequest(StateRequest.REMOVE_COMPONENT, new Object[] { comonentType }));
    }

    public void requestAddComponent(EComponent c) {
        requestQueue.submitRequest(new StateRequest(StateRequest.ADD_COMPONENT, new Object[] { c }));
    }

    public void requestRemoveAttribute(String comonentType, String attributeId) {
        requestQueue.submitRequest(new StateRequest(StateRequest.RMEOVE_ATTRIBUTE, new Object[] { comonentType, attributeId }));
    }

    public void requestAddAttribute(String comonentType, CAttribute a) {
        requestQueue.submitRequest(new StateRequest(StateRequest.ADD_ATTRIBUTE, new Object[] { comonentType, a }));
    }

    public void requestFadeIn(float speed) {
        requestQueue.submitRequest(new StateRequest(StateRequest.FADE_IN, new Object[] { speed }));
    }

    public void requestFadeOut(float speed) {
        requestQueue.submitRequest(new StateRequest(StateRequest.FADE_OUT, new Object[] { speed }));
    }

    public void validateRequests() {
        StateRequest r = new StateRequest(0);

        while (requestQueue.getNextRequest(r)) {
            System.out.println(r.type);
        }
    }

    public void tickRenderer() {

    }
}

class Engine extends GameWindow {
    ArrayList<Integer> keys = new ArrayList<>();
    EngineRenderer render;

    public Engine() {
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
        render = new EngineRenderer(getGraphics(), getMaximumSize(), getSize());
    }

    @Override
    protected void onTick() {
        if (keys.contains(KeyEvent.VK_W)) {
            render.cam.moveForward(1);
        } else if (keys.contains(KeyEvent.VK_S)) {
            render.cam.moveForward(-1);
        }

        if (keys.contains(KeyEvent.VK_A)) {
            render.cam.moveRight(-1);
        } else if (keys.contains(KeyEvent.VK_D)) {
            render.cam.moveRight(1);
        }

        if (keys.contains(KeyEvent.VK_Q)) {
            render.cam.moveUp(-1);
        } else if (keys.contains(KeyEvent.VK_E)) {
            render.cam.moveUp(1);
        }

        if (keys.contains(KeyEvent.VK_LEFT)) {
            render.cam.turnRight(-1);
        } else if (keys.contains(KeyEvent.VK_RIGHT)) {
            render.cam.turnRight(1);
        }

        if (keys.contains(KeyEvent.VK_DOWN)) {
            render.cam.turnUp(-1);
        } else if (keys.contains(KeyEvent.VK_UP)) {
            render.cam.turnUp(1);
        }

        render.startRender();

        render.transformation.translation.x = 0;
        render.renderMesh(new Mesh());

        Mesh someMesh = new Mesh();
        someMesh.color = new Vector4(1.0f, 0.0f, 0.0f);
        render.transformation.translation.x = 3;
        render.renderMesh(someMesh);

        render.endRender();
    }
    
    @Override
    protected void onResized() {
        render.updateSwapChain(getGraphics(), getMaximumSize(), getSize());
    }

    @Override
    protected void onDestroy() {
        
    }
}

public class Main {
    public static void main(String[] args) {
        String content = EasyFiles.readFile("src\\main\\java\\Projects\\MyProject1\\Levels\\level1.xml");

        content = content.replace("\r", "");
        content = content.replace("\n", "");
        content = content.replace("\t", "");

        while (content.contains(" <")) {
            content = content.replace(" <", "<");
        }

        //System.out.println(content);

        // Parse the document
        // MarkDownDocument mainDocument = new MarkDownDocument("Document");
        // MarkDownReader.read(mainDocument, content);
// 
        // // Get the root directory from the document
        // MarkDownDocument rootDocument = new MarkDownDocument("Root");
        // MarkDownReader.read(rootDocument, mainDocument.getElementsByTag("Root").get(0).content);
        // SafeList<MarkDownElement> scriptableEntities = rootDocument.getElementsByTag("ScriptableEntity");
// 
        // for (int i = 0; i < scriptableEntities.getSize(); i++) {
        //     MarkDownElement e = scriptableEntities.get(i);
        //     
        //     for (int j = 0; j < e.attributes.getSize(); j++) {
        //         MarkDownAttribute a = e.attributes.get(j);
        //         System.out.println(String.format("%s: %s", a.name, a.value));
        //     }
// 
        //     System.out.println(e.content);
        // }

        Engine engine = new Engine();
        engine.start((long)(1000.0 / 5120.0)); // 60 FPS

        // JPanel viewportPanel = new JPanel();
        // viewportPanel.setSize(new Dimension(500, 500));
        // GameStateManager stateManager = new GameStateManager("src\\main\\java\\Projects\\MyProject1", viewportPanel);
// 
        // stateManager.requestFadeIn(10.0f);
// 
        // stateManager.validateRequests();
    }
}
