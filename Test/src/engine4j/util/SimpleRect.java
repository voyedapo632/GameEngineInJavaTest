package engine4j.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;

import engine4j.Framework;
import engine4j.editor.ui.EditorStyle;

public class SimpleRect extends ECSEntity {
    public SimpleRect() { super("SimpleRect"); }

    public SimpleRect(ECSEntity base, Object host) {
        super(base, host);
    }

    public SimpleRect(String id, String parent, Object host) {
        super(id, "SimpleRect", parent, host);
    }

    @Override public ECSEntity newChild() { return new SimpleRect(); }
    @Override public ECSEntity newChild(ECSEntity base, Object host) { return new SimpleRect(base, host); }
    @Override public ECSEntity newChild(String id, String parent, Object host) { return new SimpleRect(id, parent, host); }
    private BufferedImage img;
    private String lastSprite;

    Vector<BehavioralScript> behavioralScripts = new Vector<BehavioralScript>();

    @Override
    public void laodScripts() {
        behavioralScripts.clear();

        for (ECSComponent c : components) {
            if (c.type.startsWith("BehavioralScript")) {
                try {
                    Class<?> myClass = Class.forName("Projects.MyProject1.Source." + new String(c.value));
                    BehavioralScript scrtipt = (BehavioralScript)myClass.getConstructor(ECSEntity.class).newInstance(new Object[] { this });
                    behavioralScripts.add(scrtipt);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        for (BehavioralScript script : behavioralScripts) {
            script.onStart();
        }
    }

    @Override
    public void endScripts() {
        for (BehavioralScript script : behavioralScripts) {
            script.onEnd();
        }
    }

    @Override
    public void onEntityStart() {
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Transform", "Vec3", "translation", new Vec3(200, 200, 0).serialize(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Transform", "Vec2", "scale", new Vec2(50, 50).serialize(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Style", "Vec3", "color", new Vec3(255, 0, 255).serialize(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Style", "int", "alpha", "255".toCharArray(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Style", "String", "sprite", "heart.png".toCharArray(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Style", "boolean", "Use Sprite", "true".toCharArray(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Behavior", "boolean", "relativeToCamera", "true".toCharArray(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Transform", "Vec3", "translation$", new Vec3(0, 0, 0).serialize(), true, true));
        ECSHelper.addComponentIfNotExist(this, new ECSComponent("Transform", "Vec2", "scale$", new Vec2(0, 0).serialize(), true, true));
        //ECSHelper.addComponentIfNotExist(this, new ECSComponent("Scripts", "BehavioralScript", "playerMovementScript", "PlayerMovementScript".toCharArray(), true, true));
        //ECSHelper.removeComponent(this, "playerMovementScript");
        //ECSHelper.addComponentIfNotExist(this, new ECSComponent("Style", "Transform2D", "transformation", "200,200,0~50,50~50,50".toCharArray(), true, true));
        // Pre-parse the components
        //this.components.remove(9);
        
        ECSHelper.parseComponentList(components);

        laodScripts();
        Framework framework = (Framework)this.host;
        // System.out.println(ECSHelper.serializeEntity(this));
        String sprite = (String)ECSHelper.getComponent(this, "sprite").getParsedObjectValue();
        lastSprite = sprite;

        try {
            img = ImageIO.read(new File(framework.projectPath + "\\Assets\\" + sprite));
        } catch (Exception e) {
        }
    }

    boolean isHovered;

    public static boolean isCollideRect(Vec3 player1, Vec2 scale1, Vec3 player2, Vec2 scale2) {
        if(player1.x < player2.x + scale2.x &&
            player1.x + scale1.x > player2.x &&
            player1.y < player2.y + scale2.y &&
            player1.y + scale1.y > player2.y) {
            return true;
        }

        return false;
    }

    @Override
    public void onEntityTick() {
        if (((Framework)host).contextState.equals(Framework.CONTEXT_EDITOR)) {
            ECSHelper.parseComponentList(components);
        }
        
        Vec3 pos = (Vec3)this.components.get(0).getParsedObjectValue();
        Vec2 scale = (Vec2)this.components.get(1).getParsedObjectValue();
        Vec3 pos$ = (Vec3)ECSHelper.getComponent(this, "translation$").getParsedObjectValue();
        Vec2 scale$ = (Vec2)ECSHelper.getComponent(this, "scale$").getParsedObjectValue();
        boolean mouseClicked = ((Framework)host).mouseClicked;
        Vec3 camera = ((Framework)host).cameraPos;
        Point mousePos = ((Framework)host).mousePosition;
        Vector<String> selectedEntities = ((Framework)host).selectedActors;
        SafeList<ECSEntity> allEntities = ((Framework)host).entitys;
        SafeList<Integer> keyCodeDownCodes = ((Framework)host).keyCodeDownCodes;
        Dimension targetSize = ((Framework)host).viewport.getSize();
        boolean multiSelect = ((Framework)host).multiSelect;
        boolean relativeToCamera = (boolean)ECSHelper.getComponent(this, "relativeToCamera").getBooleanParsedValue();

        for (int i = 0; i < allEntities.getSize(); i++) {
            ECSEntity e = allEntities.get(i);

            if (!this.id.startsWith("thePlayer") && e.id.startsWith("thePlayer")) {
                Vec3 pos2 = (Vec3)e.components.get(0).getParsedObjectValue();
                Vec2 scale2 = (Vec2)e.components.get(1).getParsedObjectValue();

                if (isCollideRect(pos, scale, pos2, scale2)) {
                    e.hasCollided = true;
                    break;
                }
            }
        }

        float zValue = camera.z;
        int xOffset = 0;
        int yOffset = 0;
        
        if (relativeToCamera) {
            xOffset = (int)((-camera.x) * (1.0 - (float)pos.z));
            yOffset = (int)((-camera.y) * (1.0 - (float)pos.z));
        }

        if (relativeToCamera) {
            screenX = (int)(xOffset + pos.x + zValue * (xOffset + pos.x)) + (int)(targetSize.width * pos$.x);
            screenY = (int)(yOffset + pos.y + zValue * (yOffset + pos.y)) + (int)(targetSize.height * pos$.y);
        } else {
            screenX = (int)(xOffset + pos.x) + (int)(targetSize.width * pos$.x);
            screenY = (int)(yOffset + pos.y) + (int)(targetSize.height * pos$.y);
        }


        int scaleXOffset = (int)(targetSize.width * scale$.x);
        int scaleYOffset = (int)(targetSize.height * scale$.y);

        if (relativeToCamera) {
            scaleXOffset += scale.x * zValue;
            scaleYOffset += scale.y * zValue;
        }

        screenScaleX = (int)(scale.x + scaleXOffset);
        screenScaleY = (int)(scale.y + scaleYOffset);

        //System.out.println("mouse: " + mousePos.x + ", " + mousePos.y);
        if (mousePos != null && ((Framework)host).contextState.equals(Framework.CONTEXT_EDITOR)) {
            if (ECSHelper.isCollidePointRect(mousePos.x, mousePos.y, 
                    screenX, screenY, screenScaleX, screenScaleY)) {
                if (mouseClicked) {
                    if (!multiSelect) {
                        selectedEntities.clear();
                    }

                    selectedEntities.add(id);
                }

                isHovered = true;
            } else {
                isHovered = false;
            }
            
            Framework framework = (Framework)this.host;
            String sprite = (String)ECSHelper.getComponent(this, "sprite").getParsedObjectValue();

            if (lastSprite != sprite) {
                try {
                    img = ImageIO.read(new File(framework.projectPath + "\\Assets\\" + sprite));
                    lastSprite = sprite;
                } catch (Exception e) {
                }
            }
        }

        if (((Framework)host).contextState.equals(Framework.CONTEXT_GAME)) {
            for (BehavioralScript script : behavioralScripts) {
                script.onTick();
            }
        }
    }

    @Override
    public void onEntityShouldDraw(Graphics g) {
        if (((Framework)host).contextState.equals(Framework.CONTEXT_GAME)) {
            for (BehavioralScript script : behavioralScripts) {
                script.onShouldDraw(g);
            }
        }

        Vec3 camera = ((Framework)host).cameraPos;
        Point mousePos = ((Framework)host).mousePosition;
        Vector<String> selectedEntities = ((Framework)host).selectedActors;
        Dimension targetSize = ((Framework)host).targetSize;
        Vec3 pos = (Vec3)this.components.get(0).getParsedObjectValue();
        Vec2 scale = (Vec2)this.components.get(1).getParsedObjectValue();
        Vec3 pos$ = (Vec3)ECSHelper.getComponent(this, "translation$").getParsedObjectValue();
        Vec2 scale$ = (Vec2)ECSHelper.getComponent(this, "scale$").getParsedObjectValue();
        Vec3 color = (Vec3)ECSHelper.getComponent(this, "color").getParsedObjectValue();
        int alpha = (int)ECSHelper.getComponent(this, "alpha").getPrimitiveParsedValue();
        boolean useSprite = (boolean)ECSHelper.getComponent(this, "Use Sprite").getBooleanParsedValue();
        boolean relativeToCamera = (boolean)ECSHelper.getComponent(this, "relativeToCamera").getBooleanParsedValue();
 
        if ((selectedEntities.contains(id) || isHovered) && ((Framework)host).contextState.equals(Framework.CONTEXT_EDITOR)) {
            int selectionWidth = 3;

            g.setColor(EditorStyle.FOCUS_SELECTION);
            g.fillRect(screenX - selectionWidth, screenY - selectionWidth, 
                screenScaleX + selectionWidth * 2, screenScaleY + selectionWidth * 2);
        }

        g.setColor(new Color((int)color.x, (int)color.y, (int)color.z, alpha));
        g.fillRect(screenX, screenY, screenScaleX, screenScaleY);

        if (useSprite) {
            g.drawImage(img, screenX, screenY, screenScaleX, screenScaleY, null);
        }
    }

    @Override
    public void onEntityEnd() { 
        if (((Framework)host).contextState.equals(Framework.CONTEXT_GAME)) {
            for (BehavioralScript script : behavioralScripts) {
                script.onEnd();
            }
        }
    }

    @Override
    public void onEntityDestroyed() { 

    }
}