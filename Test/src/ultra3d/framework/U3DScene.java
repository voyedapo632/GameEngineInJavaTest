/***********************************************************************************************
 *  Copyright (C) 2025 Victor Oyedapo.
 * 
 *  All rights reserved.
 * 
 *  The following content is intended for an only for non-comerial use only.
***********************************************************************************************/

package ultra3d.framework;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JPanel;

import ultra3d.ultra3dxml.U3DXmlDocument;
import ultra3d.ultra3dxml.U3DXmlElement;
import ultra3d.util.U3DVector3f;

public class U3DScene extends U3DObject implements U3DEventInterface {
    public static final int PLAY_MODE_STOPPED = 0;
    public static final int PLAY_MODE_PAUSED = 1;
    public static final int PLAY_MODE_PLAYING = 2;
    public static final int SELECTION_MODE_POINTER = 0;
    public static final int SELECTION_MODE_TRANSLATION = 1;
    public static final int SELECTION_MODE_SCALE = 2;
    public static final int SELECTION_MODE_ROTATION = 3;
    public static final String MSG_HAS_SELECTED = "has selected"; 
    public static final String MSG_HAS_LOADED = "has loaded"; 
    public static final String MSG_HAS_SELECTED_ALL = "has selected all"; 
    public static final String MSG_HAS_DESELECTED = "has deselected"; 
    public static final String MSG_HAS_DESELECTED_ALL = "has deselected all";
    public static final String MSG_HAS_COPIED = "has copied";
    public static final String MSG_HAS_PASTED = "has pasted";
    public static final String MSG_COMPONENT_ADDED = "component added";
    public static final String MSG_COMPONENT_CHANGED = "component changed";
    public static final String MSG_COMPONENT_REMOVED = "component removed";
    public static final String MSG_ENTITY_ADDED = "entity added";
    public static final String MSG_ENTITY_REMOVED = "entity removed";
    public static final String MSG_ENTITY_COPIED = "entity copied";
    public static final String MSG_ENTITY_DUPLICATED = "entity duplicated";
    public static final String MSG_SELECTED_ENTITIES_DUPLICATED = "slected entity duplicated";
    public static final String MSG_SELECTED_ENTITIES_DELETED = "slected entity deleted";
    public static final String MSG_ENTITY_DELETED = "entity deleted";
    public static final String MSG_ENTITY_COPY_LIST_CLEARED = "entity copy list cleared";
    public static final String MSG_ENTITYS_PASTED = "entity pasted";
    public static final String MSG_SLECTED_ENTITIES_COPIED = "slected entities copied";
    protected JPanel viewport;
    protected U3DGraphicsEngine graphicsEngine;
    protected HashMap<String, U3DEntity> entities;
    protected HashMap<String, U3DComponentSystem> componentSystems;
    public ArrayList<String> selectedEntities;
    protected ArrayList<U3DEntity> copiedEntities;
    protected final ArrayList<Integer> keys;
    public int selectionMode;
    public String lastHovered;
    public boolean cameraMoved = false;
    public boolean mouseDown = false;
    public boolean shouldValidate = false;
    public U3DMessageBox messageBox = new U3DMessageBox();

    public U3DScene(String filePath) {
        super(filePath);
        lastHovered = "";
        entities = new HashMap<>();
        componentSystems = new HashMap<>();
        selectedEntities = new ArrayList<>();
        copiedEntities = new ArrayList<>();
        viewport = new JPanel();

        viewport.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                viewport.requestFocus();
            }
        });

        viewport.setBackground(Color.black);
        keys = new ArrayList<>();
        selectionMode = SELECTION_MODE_POINTER;
    }

    public ArrayList<Integer> getKeys(){
        return keys;
    }

    public void reload() {
        String text = "";
        File file = new File(id);

        try (Scanner sn = new Scanner(file)) {
            while (sn.hasNext()) {
                text += sn.nextLine();
            }

            sn.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        loadFromText(text);
        onValidated();
    }

    public void loadFromText(String text) {
        messageBox.appendMessage(U3DScene.MSG_HAS_LOADED);
        this.entities.clear();

        U3DXmlDocument document = new U3DXmlDocument(text);
        U3DXmlDocument root = new U3DXmlDocument(document.getElementsByTagName("Root").get(0));

        for (U3DXmlElement entityElement : root.getElementsByTagName("Entity")) {
            String entityId = entityElement.getAttributeByName("id").getValue();
            String entityParentId = entityElement.getAttributeByName("parentId").getValue();
            U3DEntity entity = new U3DEntity(entityId, entityParentId);

            for (U3DXmlElement componentElement : U3DXmlDocument.getElementsByTagName(entityElement,"Component")) {
                String componentId = componentElement.getAttributeByName("id").getValue();
                U3DComponent component = new U3DComponent(componentId);

                for (U3DXmlElement fieldElement : U3DXmlDocument.getElementsByTagName(componentElement.getInnerXml(),"Field")) {
                    component.addComponentField(U3DField.parseField(fieldElement));
                }

                entity.addComponent(component);
            }

            this.addEntity(entity);
        }
    }

    public void selectAllEntities() {
        for (U3DEntity entity : getEntities()) {
            addSelection(entity.getEntityId());
        }
        
        messageBox.appendMessage(U3DScene.MSG_HAS_SELECTED_ALL);
    }

    public void addSelection(String selection) {
        
        if (!selectedEntities.contains(selection)) {
            selectedEntities.add(selection);
        }

        messageBox.appendMessage(U3DScene.MSG_HAS_SELECTED);
    }

    public void removeSelection(String selection) {
        messageBox.appendMessage(U3DScene.MSG_HAS_DESELECTED);
        selectedEntities.remove(selection);
    }
    
    public void clearSelection() {
        messageBox.appendMessage(U3DScene.MSG_HAS_DESELECTED_ALL);
        selectedEntities.clear();
    }

    public void clearCopiedEntities() {
        messageBox.appendMessage(U3DScene.MSG_ENTITY_COPY_LIST_CLEARED);
        copiedEntities.clear();
    }

    public void copyEntity(String s) {
        messageBox.appendMessage(U3DScene.MSG_ENTITY_COPIED);

        if (getEntity(s) != null) {

        }
        
        U3DEntity entity = getEntity(s).copyEntity();
            
        if (!copiedEntities.contains(entity)) {
            copiedEntities.add(entity);
        }
    }

    public void copySelectedEntities() {
        messageBox.appendMessage(U3DScene.MSG_SLECTED_ENTITIES_COPIED);

        for (String s : selectedEntities) {
            copyEntity(s);
        }
    }

    public void pasteCopiedEntities() {
        messageBox.appendMessage(U3DScene.MSG_ENTITYS_PASTED);
        clearSelection();

        for (U3DEntity entity : copiedEntities) {
            if (entity != null) {
                U3DEntity newE = entity.copyEntity();
                String newName = newE.getEntityId() + "(" + entities.size() + ")";

                newE.setId(newName);

                U3DComponent transform = newE.getComponent("Transform");
                U3DVector3f translation = (U3DVector3f)transform.getComponentField("Translation").getValue();
                U3DVector3f scale = (U3DVector3f)transform.getComponentField("Scale").getValue();

                translation.y += scale.y * 2;
                addEntity(newE);
                addSelection(newE.getEntityId());
            }
        }

        onValidated();
    }

    public void duplicateSelected() {
        messageBox.appendMessage(U3DScene.MSG_SELECTED_ENTITIES_DUPLICATED);

        for (String s : selectedEntities) {
            U3DEntity entity = getEntity(s);

            if (entity != null) {
                U3DEntity newE = entity.copyEntity();
                String newName = newE.getEntityId() + "(" + entities.size() + 1 + ")";

                newE.setId(newName);
                addEntity(newE);
            }
        }

        onValidated();
    }

    public void deleteSelectedEntities() {
        for (String s : selectedEntities) {
            removeEntity(s);
        }
        
        onValidated();
        selectedEntities.clear();
        messageBox.appendMessage(U3DScene.MSG_SELECTED_ENTITIES_DELETED);
    }

    public ArrayList<String> getSelectedEntities() {
        return selectedEntities;
    }

    public JPanel getViewport() {
        return viewport;
    }

    public void setViewport(JPanel viewport) {
        this.viewport = viewport;
    }

    public U3DGraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    public void setGraphicsEngine(U3DGraphicsEngine graphicsEngine) {
        this.graphicsEngine = graphicsEngine;
    }

    public void addEntity(U3DEntity entity) {
        messageBox.appendMessage(U3DScene.MSG_ENTITY_ADDED);
        entities.put(entity.getEntityId(), entity);
    }
    
    public void removeEntity(String entityId) {
        messageBox.appendMessage(U3DScene.MSG_ENTITY_DELETED);

        entities.remove(entityId);
        onValidated();
    }
    
    public U3DEntity getEntity(String entityId) {
        return entities.get(entityId);
    }

    public ArrayList<U3DEntity> getEntities() {
        return new ArrayList<>(entities.values());
    }

    public Collection<U3DEntity> queryEntity(String componentId) {
        ArrayList<U3DEntity> validatedEntities = new ArrayList<>();

        for (U3DEntity entity : getEntities()) {
            if (entity.hasComponent(componentId)) {
                validatedEntities.add(entity);
            }
        }

        return validatedEntities;
    }

    public Collection<U3DEntity> queryEntities(String[] componentIds) {
        ArrayList<U3DEntity> validatedEntities = new ArrayList<>();

        for (U3DEntity entity : getEntities()) {
            if (entity.hasComponents(componentIds)) {
                validatedEntities.add(entity);
            }
        }

        return validatedEntities;
    }

    public void addComponentSystem(U3DComponentSystem system) {
        componentSystems.put(system.getClass().getSimpleName(), system);
    }

    public void removeComponentSystem(String type) {
        componentSystems.remove(type);
    }

    public U3DComponentSystem getComponentSystem(String type) {
        return componentSystems.get(type);
    }

    public Collection<U3DComponentSystem> getComponentSystems() {
        return componentSystems.values();
    }

    public void saveScene() {
        try (FileWriter myWriter = new FileWriter(id)) {
            myWriter.flush();
            myWriter.write(toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void onValidated() {
        shouldValidate = true;
        
        for (U3DComponentSystem system : getComponentSystems()) {
            system.onValidated();
        }
    }

    @Override
    public void onStart() {
        graphicsEngine = new U3DGraphicsEngine(viewport);

        for (U3DComponentSystem system : componentSystems.values()) {
            system.onStart();
        }
    }

    @Override
    public void onUpdateBegin(float deltaTime) {
        // Update
        graphicsEngine.beginRender();

        for (U3DComponentSystem system : componentSystems.values()) {
            system.onUpdateBegin(deltaTime);
        }
    }

    @Override
    public void onUpdateEnd(float deltaTime) {
        graphicsEngine.endRender();
        
        for (U3DComponentSystem system : componentSystems.values()) {
            system.onUpdateEnd(deltaTime);
        }

        shouldValidate = false;
        messageBox.clear();
    }

    @Override
    public void onEnd() {
        for (U3DComponentSystem system : componentSystems.values()) {
            system.onEnd();
        }

        messageBox.clear();
    }

    public String getSceneId() {
        return id;
    }

    @Override
    public String toString() {
        String result = """
                        <!--\r
                            Copyright (C) 2025 Victor Oyedapo.\r
                        \r
                            All rights reserved.\r
                        \r
                            The following document may be heavily modified by the Ultra3D Editor.\r
                            Modify at your own risk.\r
                        -->
                        
                        <Root>
                        """;

        for (U3DEntity entity : entities.values()) {
            String[] data = entity.toString().split("\n");

            for (String line : data) {
                result += "    " + line + "\n";
            }
        }

        return result + "</Root>";
    }
}