package engine4j.util;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public boolean running;

    public GameWindow(int width, int height, String title) {
        this.setSize(width, height);
        this.setTitle(title);
        this.setLayout(new BorderLayout());
        running = false;
    }

    public void start(long interval) {
        this.setVisible(true);
        this.onInit();
        running = true;
       
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent comp) {
                onResized();
            }
        });

        Thread thread = new Thread(() -> {
            while (running) {
                if (this.isVisible()) {
                    this.onTick();
                } else {
                    this.stop();
                }


                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ex) {
                    this.stop();
                    System.out.println(ex);
                }
            }


            this.onDestroy();
        });


        thread.start();
    }


    public void stop() {
        running = false;
        this.setVisible(false);
    }


    protected void onInit() { }
    protected void onTick() { }
    protected void onResized() { }
    protected void onDestroy() { }
}
