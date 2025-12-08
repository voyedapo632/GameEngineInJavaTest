package engine4j.core;

public class Main {
    public static void main(String[] args) {
        E4JSceneManager sceneManager = new E4JSceneManager();
        sceneManager.loadScene("src\\main\\java\\engine4j\\core\\levelData.xml");

        // Print out the scene
        System.out.println(sceneManager.getScene("Scene 1").toString());
    }
}
