package engine4j.util;

import java.awt.Graphics;
import java.util.Vector;

public class ECSEntity {
    public String id;
    public String childInstanceId;
    public String parent;
    public Object host;
    public Vector<ECSComponent> components;
    // No save variables
    public boolean hasCollided = false;
    public int screenX;
    public int screenY;
    public int screenScaleX;
    public int screenScaleY;

    public ECSEntity() { 
        this.id = "";
        this.childInstanceId = "ECSEntity";
        this.parent = "";
        this.host = null;
        components = new Vector<ECSComponent>();
    }

    public ECSEntity(String childInstanceId) { 
        this.id = "";
        this.childInstanceId = childInstanceId;
        this.parent = "";
        this.host = null;
        components = new Vector<ECSComponent>();
    }

    public ECSEntity(ECSEntity base, Object host) {
        this.id = base.id;
        this.childInstanceId = base.childInstanceId;
        this.parent = base.parent;
        this.host = host;
        this.components = (Vector<ECSComponent>)base.components.clone();
    }

    public ECSEntity(String id, String childInstanceId, String parent, Object host) {
        this.id = id;
        this.childInstanceId = childInstanceId;
        this.parent = parent;
        this.host = host;
        components = new Vector<ECSComponent>();
    }

    public void laodScripts() { }
    public void endScripts() { }
    public void onEntityStart() { }
    public void onEntityTick() { }
    public void onEntityShouldDraw(Graphics g) {  }
    public void onEntityEnd() { }
    public void onEntityDestroyed() { }
    public ECSEntity newChild() { return new ECSEntity(); }
    public ECSEntity newChild(ECSEntity base, Object host) { return new ECSEntity(base, host); }
    public ECSEntity newChild(String id, String parent, Object host) { return new ECSEntity(id, childInstanceId, parent, host); }
}