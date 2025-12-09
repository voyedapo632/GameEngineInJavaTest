package u3dprojects.MyProject1.source;

public class Main {
    public static void main(String[] args) {
        Game gameTest = new Game(1200, 700, "Test");
        gameTest.start((long)(1000.0 / 1024.0)); // 1024 FPS
    }
}
