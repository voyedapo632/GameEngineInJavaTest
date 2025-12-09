package ultra3d.framework;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

import sr4j.OBJModel;
import sr4j.SR4JTexture2D;

public class U3DSceneManager {
    protected HashMap<String, OBJModel> loadedModels;
    protected HashMap<String, SR4JTexture2D> laodedTextures;
    protected HashMap<String, U3DScript> loadedScripts;
    protected HashMap<String, U3DScene> scenes;

    public U3DSceneManager() {
        loadedModels = new HashMap<>();
        laodedTextures = new HashMap<>();
        scenes = new HashMap<>();
    }

    public void loadOBJFile(String path) {
        OBJModel objModel = new OBJModel(path);
        
        objModel.parse();
        loadedModels.put(objModel.fileName, objModel);
    }

    public void loadTextureFile(String path) {
        File file = new File(path);
        SR4JTexture2D cubeTexture = null;
        BufferedImage source = null;

        try {
            source = ImageIO.read(file);
        } catch (IOException e) {

        }

        if (source != null) {
            cubeTexture = new SR4JTexture2D(source);
        }

        laodedTextures.put(file.getName(), cubeTexture);
    }

    public OBJModel getObjectModel(String name) {
        return loadedModels.get(name);
    }

    public SR4JTexture2D getTexture(String name) {
        return laodedTextures.get(name);
    }

    public void linkStaticScript(U3DScript script) {
        loadedScripts.put(script.getId(), script);
    }

    public void linkDynamicScript(String projectFoldLocation, String scriptPackName) {
        try {
            Class<?> myClass = Class.forName(projectFoldLocation + ".Source." + scriptPackName.replace(".java", ""));
            U3DScript script = (U3DScript)myClass.getConstructor(this.getClass()).newInstance(new Object[] { this });

            loadedScripts.put(script.getId(), script);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.println(ex);
        }
    }

    public U3DScript createScript(String scriptId) {
        U3DScript script = loadedScripts.get(scriptId);

        try {
            return (U3DScript)script.getClass().getConstructor(this.getClass()).newInstance(new Object[] { this });
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(e);
        }

        return null;
    }

    public void unlinkScript(String scriptId) {
        loadedScripts.remove(scriptId);
    }

    public void unlinkScripts() {
        loadedScripts.clear();
    }

    public ArrayList<U3DScene> getScenes() {
        return new ArrayList<>(scenes.values());
    } 

    public void loadScene(String path) {
        U3DScene scene = new U3DScene(path);

        String text = "";
        File file = new File(path);

        try (Scanner sn = new Scanner(file)) {
            while (sn.hasNext()) {
                text += sn.nextLine();
            }

            sn.close();
        } catch (FileNotFoundException e) {
        }

        scene.loadFromText(text);

        addScene(scene);
    }

    public void addScene(U3DScene scene) {
        scenes.put(scene.getSceneId(), scene);
    }
    
    public U3DScene getScene(String sceneId) {
        return scenes.get(sceneId);
    }
    
    public U3DScene getActiveScene() {
        for (U3DScene scene : scenes.values()) {
            if (scene.getViewport().hasFocus()) {
                return scene;
            }
        }

        return null;
    }

    public void removeScene(String sceneId) {
        scenes.remove(sceneId);
    }
}
