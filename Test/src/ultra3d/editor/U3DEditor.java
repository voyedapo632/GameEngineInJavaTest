package ultra3d.editor;

import engine4j.util.GameWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import ultra3d.editor.ui.U3DColors;
import ultra3d.editor.ui.U3DDockManeger;
import ultra3d.editor.ui.U3DDockWindow;
import ultra3d.framework.U3DSceneManager;

public class U3DEditor extends GameWindow {
    public U3DDockManeger mainDockManager;
    public U3DDockManeger centralDockArea;
    public U3DDockManeger sceneDockArea;
    public OutlinerDockWindow outliner;
    public DetailsDockWindow details;
    public ContentBrowserWindow contentBrowser;
    public U3DDockWindow prefabs;
    public ConsoleWindow console;
    public SolutionExplorerWindow solutionExplorer;
    public ArrayList<SceneDockWindow> loadedScenes;
    public ArrayList<KeyEvent> keys;
    public U3DSceneManager sceneManager;
    public String projectPath;
    public EditorMenuBar menuBar;
    public EditorToolBar toolBar;

    public U3DEditor(String projectPath) {
        super(1200, 700, "Ultra3D Editor (" + projectPath + ")");
        this.projectPath = projectPath;
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        loadedScenes = new ArrayList<>();
        initComponents();
    }

    public final void initComponents() {
        // Menu bar
        menuBar = new EditorMenuBar(this);
        
        // Tool bar
        toolBar = new EditorToolBar(this);
        add(toolBar, BorderLayout.NORTH);

        // Main dock manager
        mainDockManager = new U3DDockManeger();
        add(mainDockManager, BorderLayout.CENTER);

        // Central dock area
        centralDockArea = new U3DDockManeger();
        mainDockManager.add(centralDockArea, BorderLayout.CENTER);

        // Scene dock area
        sceneDockArea = new U3DDockManeger();
        centralDockArea.add(sceneDockArea, BorderLayout.CENTER);

        // Outliner
        outliner = new OutlinerDockWindow(this);
        outliner.setPreferredSize(new Dimension(350, 500));
        mainDockManager.addChildDockWindow(outliner, U3DDockManeger.DOCK_POSITION_RIGHT);
        
        // Details
        details = new DetailsDockWindow(this);
        details.setPreferredSize(new Dimension(350, 450));
        outliner.addChildDockWindow(details, U3DDockManeger.DOCK_POSITION_BOTTOM);

        // Content browser
        contentBrowser = new ContentBrowserWindow(this);
        contentBrowser.setPreferredSize(new Dimension(350, 300));
        centralDockArea.addChildDockWindow(contentBrowser, U3DDockManeger.DOCK_POSITION_BOTTOM);

        // Prefabs
        prefabs = new U3DDockWindow("Prefabs", new JLabel("[]"));
        prefabs.setPreferredSize(new Dimension(350, 300));
        // centralDockArea.addChildDockWindow(prefabs, U3DDockManeger.DOCK_POSITION_LEFT);

        // Console
        console = new ConsoleWindow (this);
        console.setPreferredSize(new Dimension(550, 300));
        contentBrowser.addChildDockWindow(console, U3DDockManeger.DOCK_POSITION_RIGHT);

        // Solution explorer
        solutionExplorer = new SolutionExplorerWindow(this);
        solutionExplorer.setPreferredSize(new Dimension(350, 350));
        centralDockArea.addChildDockWindow(solutionExplorer, U3DDockManeger.DOCK_POSITION_LEFT);
    }

    @Override
    protected void onInit() {
        // Scene manager
        sceneManager = new U3DSceneManager();
        loadGameAssets();

        File dir = new File(projectPath + "\\scenes");
        
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                loadScene(file.getName());
            }
        }

        validate();

        for (SceneDockWindow scene : loadedScenes) {
            scene.onStart();
        }
    }

    @Override
    protected void onTick() {
        for (SceneDockWindow scene : loadedScenes) {
            scene.onUpdateBegin(0.0f);
            scene.onUpdateEnd(0.0f);
        }
    }

    @Override
    protected void onResized() {
    }

    @Override
    protected void onDestroy() {
    }

    public void loadScene(String name) {
        SceneDockWindow scene = new SceneDockWindow(this, name);
                
        if (loadedScenes.isEmpty()) {
            sceneDockArea.addChildDockWindow(scene, U3DDockManeger.DOCK_POSITION_CENTER);
        } else {
            loadedScenes.get(0).addChildDockWindow(scene, U3DDockManeger.DOCK_POSITION_CENTER);
        }

        loadedScenes.add(scene);
        console.writeLine("Scene \"" + scene.sceneName + "\" was loaded", Color.green);
    }

    public void loadGameAssets() {
        File dir = new File(projectPath + "\\assets");
        
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                if (file.getName().endsWith(".obj")) {
                    sceneManager.loadOBJFile(file.getPath());
                    console.writeLine("Object file \"" + file.getName() + "\" was loaded", U3DColors.text);
                } else if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                    console.writeLine("Image file \"" + file.getName() + "\" was loaded", U3DColors.text);
                    sceneManager.loadTextureFile(file.getPath());
                }
            }
        }
    }
}
