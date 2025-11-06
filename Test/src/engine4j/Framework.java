package engine4j;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import engine4j.util.ECSEntity;
import engine4j.util.ECSHelper;
import engine4j.util.EasyFiles;
import engine4j.util.SafeList;
import engine4j.util.SimpleRect;
import engine4j.util.Vec3;

public class Framework {
    public static final String CONTEXT_EDITOR = "EDITOR";
    public static final String CONTEXT_GAME = "GAME";
    public String contextState;
    public Vector<String> changeHistory;
    public int changeHistoryPosition;
    public boolean multiSelect;
    public boolean mouseClicked;
    public Point mousePosition;
    public Image backbuffer;
    public Graphics gfx;
    public Graphics targetGfx;
    public Dimension targetSize;
    public Vector<String> selectedActors;
    public Vector<BufferedImage> importedImages;
    public SafeList<ECSEntity> entityInstanceConverters;
    public SafeList<ECSEntity> entitys;
    // public Vector<Entity> entities;
    public Vec3 cameraPos;
    SimpleRect rect;
    ECSEntity rectView;
    public String levelPath;
    public int keyCodeDown;
    public SafeList<Integer> keyCodeDownCodes;
    public JPanel viewport;
    public String projectPath;
    public String defaultLevelName;

    public Framework(Graphics _targetGfx, Dimension _targetSize, JPanel viewport) {
        this.update(_targetGfx, _targetSize);
        this.viewport = viewport;
        changeHistoryPosition = 0;
        changeHistory = new Vector<String>();
        keyCodeDownCodes = new SafeList<Integer>();
        mousePosition = new Point(-1, -1);
        mouseClicked = false;
        multiSelect = false;

        cameraPos = new Vec3(0, 0, 0);
        //entities = new Vector<Entity>();
        selectedActors = new Vector<String>();

        entityInstanceConverters = new SafeList<ECSEntity>();
        entitys = new SafeList<ECSEntity>();
    }

    public void appendEntity(ECSEntity entity) {
        for (int i = 0; i < entityInstanceConverters.getSize(); i++) {
            ECSEntity e = entityInstanceConverters.get(i);

            if (e.childInstanceId.startsWith(entity.childInstanceId)) {
                entitys.add(e.newChild(entity, this));
            }
        }

        entitys.getLast().onEntityStart();
    }

    public void loadLevel(String _levelPath) {
        levelPath = _levelPath;
        entitys.clear();

        String data = EasyFiles.readFile(levelPath);
        String[] splitData = data.split("~~~~~~");

        for (String str : splitData) {
            System.out.println(str + "\n~~~~~~");
            ECSEntity currentEntity = ECSHelper.deserializeEntity(str.toCharArray());

            for (int i = 0; i < entityInstanceConverters.getSize(); i++) {
                ECSEntity e = entityInstanceConverters.get(i);

                if (e.childInstanceId.startsWith(currentEntity.childInstanceId)) {
                    entitys.add(e.newChild(currentEntity, this));
                }
            }
        }

        for (int i = 0; i < entitys.getSize(); i++) {
            entitys.get(i).onEntityStart();
        }
    }

    public void changeLevel(String name) {
        viewport.setVisible(false);

        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
        }

        defaultLevelName = name;
        loadLevel(projectPath + "\\Levels" + "\\" + defaultLevelName);
        viewport.setVisible(true);
        viewport.requestFocus();
    }

    boolean doTrans = false;
    int fadeTransLevel = 0;
    int transInc = 255;
    String nextLevel = "";
    Color transColor = Color.black;

    public void switchToLevel(String name, int transInc, Color transColor) {
        defaultLevelName = name;

        if (!doTrans) {
            nextLevel = name;
            doTrans = true;
            this.transInc = transInc;
            this.transColor = transColor;
        }
    }

    public void render() {
        for (int i = 0; i < entitys.getSize(); i++) {
            ECSEntity e = entitys.get(i);

            if (e != null) {
                e.onEntityTick();
            }
        }

        if (gfx != null) {
            // Clear screen
            gfx.setColor(new Color(0, 0, 0));
            gfx.fillRect(0, 0, viewport.getWidth(), viewport.getHeight());

            for (int i = 0; i < entitys.getSize(); i++) {
                ECSEntity e = entitys.get(i);

                if (e != null) {
                    e.onEntityShouldDraw(gfx);
                }
            }

            if (doTrans) {
                if (fadeTransLevel < 255 - transInc) {
                    fadeTransLevel += transInc;
                } else {
                    viewport.setBackground(transColor);
                    changeLevel(nextLevel);
                    doTrans = false;
                }
            }

            if (!doTrans) {
                if (fadeTransLevel >= transInc) {
                    fadeTransLevel -= transInc;
                }
            }

            gfx.setColor(new Color(transColor.getRed(), transColor.getGreen(), transColor.getBlue(), fadeTransLevel));
            gfx.fillRect(0, 0, viewport.getWidth(), viewport.getHeight());

            mouseClicked = false;
        }
    }

    public void cleanup() {
        for (int i = 0; i < entitys.getSize(); i++) {
            ECSEntity e = entitys.get(i);

            if (e != null) {
                e.onEntityEnd();
                e.onEntityDestroyed();
            }
        }
    }

    public void reloadScripts() {
        for (int i = 0; i < entitys.getSize(); i++) {
            ECSEntity e = entitys.get(i);

            if (e != null) {
                e.laodScripts();
            }
        }
    }

    public void swapBuffers(ImageObserver io) {
        viewport.getGraphics().drawImage(backbuffer, 0, 0, io);
    }


    public void update(Graphics _targetGfx, Dimension newSize) {
        if (viewport != null) {
            backbuffer = new BufferedImage(viewport.getWidth(), viewport.getHeight(), BufferedImage.TYPE_INT_RGB);
            gfx = backbuffer.getGraphics();
        }
    }

    public void saveLevel() {
        String data = "";

        for (int i = 0; i < entitys.getSize(); i++) {
            ECSEntity e = entitys.get(i);

            if (e != null) {
                System.out.println(e.id);
                data += new String(ECSHelper.serializeEntity(e)) + "\n~~~~~~\n";
            }
        }

        data = data.substring(0, data.length() - 8);
        EasyFiles.writeFile(levelPath, data.toCharArray());
    }
}
