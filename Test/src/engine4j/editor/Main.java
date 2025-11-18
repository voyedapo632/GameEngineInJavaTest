package engine4j.editor;

import java.awt.Dimension;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JFrame;

import engine4j.Framework;
import engine4j.util.SafeList;

class Vector3 {
    float x, y, z;
}

class Transform {
    Vector3 position;
    Vector3 scale;
    Vector3 rotation;
}

class GameScript {
    GameObject parent;
    String name;

    public void onStart() { }
    public void onUpdate() { }
    public void onRenderBegin() { }
    public void onRenderEnd() { }
    public void onExit() { }
    public void onKeyEvent() { }
    public void onMouseEvent() { }
    public void onCollided() { }
}

class Attribute {
    public String type;
    public String id;
    public String group;
    public boolean canEdit;
    public boolean canSave;
    public Object value;

    public Attribute(String type, String id, Object value) {
        group = "root";
        canEdit = true;
        canSave = true;
    }

    public Attribute(String type, String id, Object value, String group) {
        this.group = group;
        canEdit = true;
        canSave = true;
    }

    static final String TInt = "TInt";
    static final String TFloat = "TFloat";
    static final String TDouble = "TDouble";
    static final String TArray = "TArray";
    static final String TVector3 = "TVector3";
    static final String TVector2 = "TVector2";
    static final String TMesh = "TMesh";
    static final String TTexture = "TTexture";
    static final String TGameScript = "TGameScript";
    static final String TString = "TString";
}

class AttributeList extends SafeList<Attribute> {
    public AttributeList() {
        super();
    }

    public AttributeList(int allocatedSize) {
        super(allocatedSize);
    }

    public Attribute getAttribute(String id, String group) {
        for (int i = 0; i < this.getSize(); i++) {
            Attribute a = this.get(i);

            if (a.id.equals(id) && a.group.equals(group)) {
                return a;
            }
        }

        return null;
    }

    public void addUniqueAttribute(Attribute attribute) {
        for (int i = 0; i < this.getSize(); i++) {
            Attribute a = this.get(i);

            if (a.id.equals(attribute.id) && a.group.equals(attribute.group)) {
                return;
            }
        }

        this.add(attribute);
    }
}

class TransformWC {
    public Component base;
    public Vector3 translation;
    public Vector3 scale;
    public Vector3 rotation;

    public TransformWC(Component base) {
        this.base = base;
        translation = (Vector3)base.attributes.getAttribute("trnaslation", base.type).value;
        scale = (Vector3)base.attributes.getAttribute("scale", base.type).value;
        rotation = (Vector3)base.attributes.getAttribute("rotation", base.type).value;
    }
}

class Component {
    public String type;
    public AttributeList attributes;
}

class ScriptableEntity {
    String id;
    String parent;
    boolean canSave;
    boolean canSelect;
    SafeList<Component> components;
    SafeList<GameScript> scripts;

    public ScriptableEntity(String id, String parent, boolean canSave, boolean canSelect) {
        this.id = id;
        this.parent = parent;
        this.canSave = canSave;
        this.canSelect = canSelect;
    }
}

class GameObject {
    String id;
    String parent;
    Transform transform;
    boolean canSave;
    boolean canSelect;
    AttributeList atributes;
    Framework framework;

    public GameObject(String id) {
        this.id = id;
    }

    public void start() { }
    public void update() { }
}

class Player extends GameObject {
    Attribute weight;
    Attribute mass;
    Attribute health;
    Attribute velocity;

    public Player(String id) {
        super(id);

        // Create physics atributes
        atributes.addUniqueAttribute(new Attribute(Attribute.TFloat, "weight", 0.0f, "Entity"));
        atributes.addUniqueAttribute(new Attribute(Attribute.TFloat, "mass", 0.0f, "Entity"));
        atributes.addUniqueAttribute(new Attribute(Attribute.TFloat, "health", 0.0f, "Entity"));
        atributes.addUniqueAttribute(new Attribute(Attribute.TVector3, "velocity", new Vector3(), "Entity"));
        atributes.addUniqueAttribute(new Attribute(Attribute.TGameScript, "entityScript", "EntityScript.java", "Entity"));

        // Create sprite renderer atributes
        atributes.addUniqueAttribute(new Attribute(Attribute.TString, "source", "heart.png", "SpriteRenderer"));
        atributes.addUniqueAttribute(new Attribute(Attribute.TVector3, "position", new Vector3(), "SpriteRenderer"));
        atributes.addUniqueAttribute(new Attribute(Attribute.TGameScript, "spriteRendererScript", "SpriteRendererScript.java", "SpriteRenderer"));

        // Initialize pointers
        initializeAtributes();
    }

    public final void initializeAtributes() {
        weight = atributes.getAttribute("weight", "Entity");
        mass = atributes.getAttribute("mass", "Entity");
        mass = atributes.getAttribute("health", "Entity");
        mass = atributes.getAttribute("velocity", "Entity");
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {
        super.update();
    }
}

public class Main {
    public static ProjectBrowser projectBrowser = new ProjectBrowser();
    public static Editor editor;
    
    public static void main(String[] args) {
        //GameObject playerObject = new GameObject("player");
//
        //// Create physics atributes
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TFloat, "weight", 0.0f, "Entity"));
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TFloat, "mass", 0.0f, "Entity"));
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TFloat, "health", 0.0f, "Entity"));
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TVector3, "velocity", new Vector3(), "Entity"));
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TGameScript, "entityScript", "EntityScript.java", "Entity"));
//
        //// Create sprite renderer atributes
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TString, "source", "heart.png", "SpriteRenderer"));
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TVector3, "position", new Vector3(), "SpriteRenderer"));
        //playerObject.atributes.addUniqueAttribute(new Attribute(Attribute.TGameScript, "spriteRendererScript", "SpriteRendererScript.java", "SpriteRenderer"));



        JFrame splashScreen = new JFrame();
        splashScreen.setSize(new Dimension(600, 350));
        splashScreen.setUndecorated(true);
        splashScreen.setLocationRelativeTo(null);
        splashScreen.setVisible(true);

        // Splash screen
        // try {
        //     TimeUnit.SECONDS.sleep(2);
        // } catch (InterruptedException e) {
        // }

        splashScreen.setVisible(false);

        //editor.start((long)(1000.0 / 60.0)); // 60 FPS
        projectBrowser.setLocationRelativeTo(null);
        projectBrowser.start((long)(1000.0 / 60.0)); // 60 FPS
    }
}
