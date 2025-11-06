package Projects.MyProject2.Source;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(1200, 700, "My Project 1");
        game.setExtendedState(game.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        game.start((long)(1000.0 / 60.0));
    }
}
