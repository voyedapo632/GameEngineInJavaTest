package engine4j.MarkDownParser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine4j.editor.ui.CustomTab;
import engine4j.editor.ui.EditorStyle;
import engine4j.editor.ui.TabLayout;
import engine4j.editor.ui.TabManager;
import engine4j.editor.ui.ToolButton;
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

// Primitive data types
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

abstract class PrimitiveDataType<T> {
    public String serialize(T t) { return ""; }
    public T deserialize(String s) { return null; }
}

class TVector2 extends PrimitiveDataType<TVector2> {
    float x, y;

    public TVector2() {
        this.x = 0;
        this.y = 0;
    }

    public TVector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String serialize(TVector2 t) { 
        return "" + x + ", " + y;
    }

    @Override
    public TVector2 deserialize(String s) { 
        String clearedStr = "";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if ((c >= '0' && c <= '9') || c == '-' || c == '.' || c == ',') {
                clearedStr += c;
            }
        }

        String[] strValues = clearedStr.split(",");
        float value1 = 0.0f, value2 = 0.0f;

        if (strValues[0].length() >= 1) {
            try {
                value1 = Float.parseFloat(strValues[0]);
            } catch (Exception e) { }
        }

        if (strValues[1].length() >= 1) {
            try {
                value2 = Float.parseFloat(strValues[1]);
            } catch (Exception e) { }
        }

        return new TVector2(value1, value2); 
    }
}

class TVector3 extends PrimitiveDataType<TVector3> {
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

    @Override
    public String serialize(TVector3 t) { 
        return "" + x + ", " + y + ", " + z;
    }

    @Override
    public TVector3 deserialize(String s) { 
        String clearedStr = "";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if ((c >= '0' && c <= '9') || c == '-' || c == '.' || c == ',') {
                clearedStr += c;
            }
        }

        String[] strValues = clearedStr.split(",");
        float value1 = 0.0f, value2 = 0.0f, value3 = 0.0f;

        if (strValues[0].length() >= 1) {
            try {
                value1 = Float.parseFloat(strValues[0]);
            } catch (Exception e) { }
        }

        if (strValues[1].length() >= 1) {
            try {
                value2 = Float.parseFloat(strValues[1]);
            } catch (Exception e) { }
        }

        if (strValues[2].length() >= 1) {
            try {
                value3 = Float.parseFloat(strValues[2]);
            } catch (Exception e) { }
        }

        return new TVector3(value1, value2, value3);
    }
}

class E4JObject {
    protected String id;
    protected String name;

    public E4JObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public String serialize() { 
        return String.format("<%s id=\"%s\" name=\"%s\"/>", getClass().getSimpleName(), id, name);
    }
}

class E4JGameObject extends E4JObject {
    public E4JGameObject(String id, String name) {
        super(id, name);
    }

    public Object parent;
    public String type; // Tells the engine how to interpret the entity
    public boolean canSave;
    public boolean canSelect;
    public boolean isVisible; // Determines if the entity is visible inside of the World Viewport of the editor
    public EComponentRegistry components;
    public SafeList<TGameScript> scripts; // Stores the scripts to avoid refinding
    public GameStateManager manager;

    public Object getParent() {
        return parent;
    }
}

// class E4JScript extends E4JObject {
//     protected String source;
//     protected Object parent;
// 
//     public E4JScript(String source, Object parent) {
//         this.source = source;
//         this.parent = parent;
//     }
// 
//     public String getSource() {
//         return source;
//     }
// 
//     public Object getParent() {
//         return parent;
//     }
// 
//     public void start() {
// 
//     }
// 
//     public void update(float deltaTime) {
//         
//     }
// }

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

enum UIComponentType {
    BUTTON("BUTTON"),
    STACK_PANEL("BUTTON"),
    SCOLL_PANEL("BUTTON"),
    TEXT_LABEL("BUTTON"),
    TEXT_FIELD("BUTTON");
    private UIComponentType(String s) {}
}

class UIComponentWrapper {
    public TVector2 positionOffset;
    public TVector2 positionScale;
    public TVector2 sizeOffset;
    public TVector2 sizeScale;
    public UIComponentType type;
}

class ScriptableEntity {
    public String id;
    public String name;
    public String parent;
    public String type; // Tells the engine how to interpret the entity
    public boolean canSave;
    public boolean canSelect;
    public boolean isVisible; // Determines if the entity is visible inside of the World Viewport of the editor
    public EComponentRegistry components;
    public SafeList<TGameScript> scripts; // Stores the scripts to avoid refinding
    public GameStateManager manager;

    public ScriptableEntity(String id, String name, String parent, String type, boolean canSave, boolean canSelect, boolean isVisible) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.type = type;
        this.canSave = canSave;
        this.canSelect = canSelect;
        this.isVisible = isVisible;
        components = new EComponentRegistry();
        scripts = new SafeList<TGameScript>();
    }

    public void initializeComponents() { }

    public void start(GameStateManager manager) {
        this.manager = manager;
    }

    public void update() { 
        if (type.equals("ScriptableEntity")) {
            // Render bill board
        }
    }
}

class Folder extends ScriptableEntity {
    public Folder(String id, String name, String parent, boolean canSave, boolean canSelect) {
        super(id, name, parent, "Folder", canSave, canSelect, false);
    }
}

class StaticMesh extends ScriptableEntity {
    private TransformComponentWrapper transform;
    private Mesh mesh;

    public StaticMesh(String id, String name, String parent, boolean canSave, boolean canSelect, boolean isVisible) {
        super(id, name, parent, "StaticMesh", canSave, canSelect, isVisible);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
    }

    @Override
    public void start(GameStateManager manager) {
        super.initializeComponents();
        this.manager = manager;
    }

    @Override
    public void update() {
        super.update();
    }
}

class UIObject extends ScriptableEntity {
    private TransformComponentWrapper transform;

    public UIObject(String id, String name, String parent, boolean canSave, boolean canSelect, boolean isVisible) {
        super(id, name, parent, "UIObject", canSave, canSelect, isVisible);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
    }

    @Override
    public void start(GameStateManager manager) {
        super.initializeComponents();
        this.manager = manager;
    }

    @Override
    public void update() {
        super.update();
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
    DefaultShaderProgram shaderProgram = new DefaultShaderProgram();
    public TransformComponentWrapper transformation = new TransformComponentWrapper();

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
        private Vector4[] ins = new Vector4[3];
        private Vector4[] cols = new Vector4[3];

        @Override
        public Vector4 onVertexShaderCalled(Object input, int index) {
            VERTEX_COMBO vi = constBuffer.mesh.vertices[((INDEX_COMBO)input).vertex];
            Vector4 in = vi.pos;
            ins[index] = vi.pos;
            cols[index] = vi.col;
            Vector4 pos = new Vector4(in.x, in.y, in.z, 1.0f);

            pos = Vector4.mul(pos, constBuffer.mvp);

            return pos;
        }

        float dx;
        float dy;
        float len1;

        @Override
        public void onGeometryShaderCalled(Object[] input) {
            dx = ins[1].x - ins[0].x;
            dy = ins[1].y - ins[0].y;
            len1 = (float)Math.sqrt(dx*dx + dy*dy);
        }

        @Override
        public Vector4 onPixelShaderCalled(int x, int y) {
            float halfWidth = frameBufferDimensions.width / 2.0f;
            float halfHeight = frameBufferDimensions.height / 2.0f;
            float nx = ((float)x / (float)frameBufferDimensions.width - ins[0].x);
            float ny = ((float)y / (float)frameBufferDimensions.height - ins[0].y);
            float l = (float)Math.sqrt(nx*nx + ny*ny);
            float dif = l / len1;
            Vector4 newCol = Vector4.mul(Vector4.sub(cols[1],  cols[0]), dif);
            
            // Vector4 color = (Vector4)input[0];
            return new Vector4(constBuffer.mesh.color.x, constBuffer.mesh.color.y, constBuffer.mesh.color.z, 1.0f);
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
    TabLayout centralTabLayout;
    JPanel viewportPanel;
    TabManager tb;

    public Engine() {
        super(1200, 700, "3D Software Renderer");

        // Mener bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(EditorStyle.BACKGROUND);
        menuBar.setBorder(new EmptyBorder(1, 1, 1, 1));
        menuBar.setForeground(Color.lightGray);
        
        JMenu file = new JMenu("File");
        file.setForeground(Color.lightGray);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        
        file.add(saveMenuItem);
        file.add(exitMenuItem);
        
        JMenu edit = new JMenu("Edit");
        edit.setForeground(Color.lightGray);
        JMenu view = new JMenu("View");
        view.setForeground(Color.lightGray);
        JMenu selection = new JMenu("Selection");
        selection.setForeground(Color.lightGray);
        JMenu debug = new JMenu("Debug");
        JMenuItem playMenuItem = new JMenuItem("▶︎ Play");
        JMenuItem stopMenuItem = new JMenuItem("⏹ Stop");
        
        debug.add(playMenuItem);
        debug.add(stopMenuItem);
        debug.setForeground(Color.lightGray);
        JMenu help = new JMenu("Help");
        help.setForeground(Color.lightGray);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(view);
        menuBar.add(selection);
        menuBar.add(debug);
        menuBar.add(help);
        
        this.setJMenuBar(menuBar);

        // Tool bar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBackground(EditorStyle.BACKGROUND);

        ToolButton undoButton = new ToolButton("↩");
        toolBar.add(undoButton);

        ToolButton redoButton = new ToolButton("↪");
        toolBar.add(redoButton);

        ToolButton refreshButton = new ToolButton("↻ Refresh");
        toolBar.add(refreshButton);

        ToolButton saveButton = new ToolButton("💾 Save");
        toolBar.add(saveButton);

        this.add(toolBar, BorderLayout.NORTH);

        // Play test button
        ToolButton playTestButton = new ToolButton("▶︎ Play");
        playTestButton.setForeground(new Color(165, 225, 120));

        toolBar.add(playTestButton);

        tb = new TabManager();
        this.add(tb, BorderLayout.CENTER);

        centralTabLayout = new TabLayout(new Dimension(200, 200), tb);
        tb.add(centralTabLayout, BorderLayout.CENTER);
        
        viewportPanel = new JPanel();
        centralTabLayout.addTab(new CustomTab("World Viewport", new JLabel("🗔"), viewportPanel));
        
        viewportPanel.addKeyListener(new KeyAdapter() {
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

        viewportPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {                
                viewportPanel.setVisible(true);
                viewportPanel.requestFocus();
            }
        });

        TabLayout outlinerLayout = new TabLayout(new Dimension(350, 320), tb);
        tb.add(outlinerLayout, BorderLayout.EAST);

        JPanel outlinerPanel = new JPanel();
        outlinerPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        outlinerLayout.addTab(new CustomTab("Outliner", new JLabel("Ψ"), outlinerPanel));

        TabLayout detailsLayout = new TabLayout(new Dimension(350, 550), tb);
        outlinerLayout.add(detailsLayout, BorderLayout.SOUTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        detailsLayout.addTab(new CustomTab("Details", new JLabel("Ψ"), detailsPanel));

        TabLayout contentBrowserLayout = new TabLayout(new Dimension(350, 320), tb);
        centralTabLayout.add(contentBrowserLayout, BorderLayout.SOUTH);

        JPanel contentBrowserPanel = new JPanel();
        contentBrowserPanel.setBackground(EditorStyle.BACKGROUND_ACCENT);
        contentBrowserLayout.addTab(new CustomTab("Content Browser", new JLabel("🖿"), contentBrowserPanel));
    }

    @Override
    protected void onInit() {
        render = new EngineRenderer(viewportPanel.getGraphics(), viewportPanel.getMaximumSize(), viewportPanel.getSize());
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
        render.updateSwapChain(viewportPanel.getGraphics(), viewportPanel.getMaximumSize(), viewportPanel.getSize());
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
        MarkDownDocument mainDocument = new MarkDownDocument("Document");
        MarkDownReader.read(mainDocument, content);

        // Get the root directory from the document
        MarkDownDocument rootDocument = new MarkDownDocument("Root");
        MarkDownReader.read(rootDocument, mainDocument.getElementsByTag("Root").get(0).content);
        SafeList<MarkDownElement> scriptableEntities = rootDocument.getElementsByTag("ScriptableEntity");

        for (int i = 0; i < scriptableEntities.getSize(); i++) {
            MarkDownElement e = scriptableEntities.get(i);

            for (int j = 0; j < e.attributes.getSize(); j++) {
                MarkDownAttribute a = e.attributes.get(j);
                System.out.println(String.format("%s: %s", a.name, a.value));
            }
        
            System.out.println(e.content);
        }

        Engine engine = new Engine();
        engine.start((long)(1000.0 / 5120.0)); // 60 FPS

        E4JObject obj = new E4JObject("item1", "item 1");
        System.out.println(obj.serialize());


        // JPanel viewportPanel = new JPanel();
        // viewportPanel.setSize(new Dimension(500, 500));
        // GameStateManager stateManager = new GameStateManager("src\\main\\java\\Projects\\MyProject1", viewportPanel);
// 
        // stateManager.requestFadeIn(10.0f);
// 
        // stateManager.validateRequests();
    }
}
