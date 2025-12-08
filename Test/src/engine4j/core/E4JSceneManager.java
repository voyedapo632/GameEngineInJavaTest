package engine4j.core;

import engine4j.MarkDownParser.MarkDownAttribute;
import engine4j.MarkDownParser.MarkDownDocument;
import engine4j.MarkDownParser.MarkDownElement;
import engine4j.MarkDownParser.MarkDownReader;
import engine4j.util.EasyFiles;
import engine4j.util.SafeList;

public class E4JSceneManager {
    protected E4JRenderContext rendeContext;
    protected SafeList<E4JScene> scenes;
    protected E4JScene activeScene;
    protected SafeList<E4JScene> removeSceneQueue;

    public E4JSceneManager() {
        scenes = new SafeList<>();
    }

    public E4JRenderContext getRenderContext() {
        return rendeContext;
    }

    public void setRenderContext(E4JRenderContext rendeContext) {
        this.rendeContext = rendeContext;
    }

    public SafeList<E4JScene> getScenes() {
        return scenes;
    }

    public void addScene(E4JScene scene) {
        if (scene != null) {
            if (!scenes.contains(scene)) {
                scenes.add(scene);
                scene.start();
            }
        }
    }

    public void removeScene(E4JScene scene) {
        if (scene != null) {
            if (scenes.contains(scene)) {
                removeSceneQueue.add(scene);
            }
        }
    }

    public void removeScene(String sceneId) {
        for (int i = 0; i < scenes.getSize(); i++) {
            E4JScene scene = scenes.get(i);

            if (scene != null) {
                if (scene.getId().equals(sceneId)) {
                    removeSceneQueue.add(scene);
                    break;
                }
            }
        }
    }

    public E4JScene getScene(String sceneId) {
        for (int i = 0; i < scenes.getSize(); i++) {
            E4JScene scene = scenes.get(i);

            if (scene != null) {
                if (scene.getId().equals(sceneId)) {
                    return scene;
                }
            }
        }

        return null;
    }

    public void loadScene(String filePath) {
        String content = EasyFiles.readFile(filePath);

        content = content.replace("\r", "");
        content = content.replace("\n", "");
        content = content.replace("\t", "");

        while (content.contains(" <")) {
            content = content.replace(" <", "<");
        }
        
        // Parse the document
        MarkDownDocument mainDocument = new MarkDownDocument("Document");
        MarkDownReader.read(mainDocument, content);

        MarkDownElement rootScene = mainDocument.getElementsByTag("E4JScene").get(0);
        MarkDownDocument rootSceneDocument = new MarkDownDocument(rootScene.name);
        MarkDownReader.read(rootSceneDocument, rootScene.content);
        SafeList<MarkDownElement> rootSceneEntities = rootSceneDocument.getElementsByTag("E4JGameObject");
        E4JScene newScene = new E4JScene(rootScene.getAttributeByName("id").value, this);

        for (int i = 0; i < rootSceneEntities.getSize(); i++) {
            MarkDownElement rootGameObject = rootSceneEntities.get(i);

            E4JGameObject newGameObject = new E4JGameObject(rootGameObject.getAttributeByName("id").value, rootGameObject.getAttributeByName("name").value);
            newGameObject.getParent().setId(rootGameObject.getAttributeByName("parent").value);

            // Load the components
            MarkDownDocument rootGameObjectDocument = new MarkDownDocument(rootGameObject.name);
            MarkDownReader.read(rootGameObjectDocument, rootGameObject.content);
            SafeList<MarkDownElement> rootGameObjectComponents = rootGameObjectDocument.getElementsByTag("E4JComponent");

            for (int j = 0; j < rootGameObjectComponents.getSize(); j++) {
                MarkDownElement rootComponent = rootGameObjectComponents.get(j);

                E4JComponent newComponent = new E4JComponent(rootComponent.getAttributeByName("id").value);
                newComponent.getParent().setId(rootComponent.getAttributeByName("parent").value);

                // Load the attributes
                MarkDownDocument rootComponentDocument = new MarkDownDocument(rootComponent.name);
                MarkDownReader.read(rootComponentDocument, rootComponent.content);
                SafeList<MarkDownElement> rootComponentAttributes = rootGameObjectDocument.getElementsByTag("E4JAttribute");

                for (int k = 0; k < rootComponentAttributes.getSize(); k++) {
                    MarkDownElement rootAttribute = rootComponentAttributes.get(k);
                    String type = rootAttribute.getAttributeByName("dataType").value;
                    String strValue = rootAttribute.content;
                    Object parsedValue;

                    switch (type) {
                        case "Integer" -> {
                            parsedValue = Integer.parseInt(strValue);
                        } case "Float" -> {
                            parsedValue = Float.parseFloat(strValue);
                        } case "Boolean" -> {
                            parsedValue = Boolean.parseBoolean(strValue);
                        } case "E4JVector2" -> {
                            parsedValue = E4JVector2.parseE4JVector2(strValue);
                        }  case "E4JVector3" -> {
                            parsedValue = E4JVector3.parseE4JVector3(strValue);
                        } default -> {
                            parsedValue = strValue;
                        }
                    }

                    E4JAttribute newAttribute = new E4JAttribute(rootAttribute.getAttributeByName("id").value, parsedValue, Integer.parseInt(rootAttribute.getAttributeByName("editAccess").value));
                    newAttribute.getParent().setId(rootAttribute.getAttributeByName("parent").value);
                    newComponent.addAttribute(newAttribute);
                }

                newGameObject.addComponent(newComponent);
            }
            
            newScene.addGameObject(newGameObject);
        }

        addScene(newScene);
    }

    public void validateScenes(float deltaTime) {
        // Validate requests to move scenes
        if (!removeSceneQueue.isEmpty()) {
            for (int i = 0; i < removeSceneQueue.getSize(); i++) {
                E4JScene scene = removeSceneQueue.get(i);

                scene.exit();
                scenes.remove(scenes.indexOf(scene));
            }

            removeSceneQueue.clear();
        }

        // Validate existing scenes
        for (int i = 0; i < scenes.getSize(); i++) {
            E4JScene scene = scenes.get(i);

            if (scene != null) {
                scene.update(deltaTime);
            }
        }
    }
}
