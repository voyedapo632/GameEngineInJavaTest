package engine4j.editor;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {
    public static ProjectBrowser projectBrowser = new ProjectBrowser();
    public static Editor editor;
    
    public static void main(String[] args) {
        JFrame splashScreen = new JFrame();
        splashScreen.setSize(new Dimension(600, 350));
        splashScreen.setUndecorated(true);
        splashScreen.setLocationRelativeTo(null);
        splashScreen.setVisible(true);

        // Splash screen
        // try {
        //     TimeUnit.SECONDS.sleep(2);
        // } catch (InterruptedException e) {
        // }

        splashScreen.setVisible(false);

        //editor.start((long)(1000.0 / 60.0)); // 60 FPS
        projectBrowser.setLocationRelativeTo(null);
        projectBrowser.start((long)(1000.0 / 60.0)); // 60 FPS
    }
}
