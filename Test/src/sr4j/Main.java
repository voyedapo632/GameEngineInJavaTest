package sr4j;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start((long)(1000.0 / 1024.0)); // 1024 FPS
    }
}
