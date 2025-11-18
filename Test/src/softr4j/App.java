package softr4j;

public class App {
    public static void main(String[] args) throws Exception {
        Game game = new Game();

        game.start((long)(1000.0 / 5120.0)); // 60 FPS
    }
}
