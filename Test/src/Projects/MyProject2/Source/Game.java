package Projects.MyProject2.Source;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;

import engine4j.Framework;
import engine4j.util.ECSEntity;
import engine4j.util.GameWindow;
import engine4j.util.SimpleRect;

public class Game extends GameWindow {
    String projectPath;
    Framework framework;
    JPanel viewportPanel;

    public Game(int width, int height, String title) {
        super(width, height, title);
        this.initializeComponets();
    }

    private void initializeComponets() {
        // Viewport
        viewportPanel = new JPanel();

        viewportPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (!framework.keyCodeDownCodes.contains(keyCode)) {
                    framework.keyCodeDownCodes.add(keyCode);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (int i = 0; i < framework.keyCodeDownCodes.getSize(); i++) {
                    Integer key = framework.keyCodeDownCodes.get(i);

                    if (key.equals(e.getKeyCode())) {
                        framework.keyCodeDownCodes.remove(i);
                    }
                }
            }
        });

        viewportPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {                
                viewportPanel.setVisible(true);
                viewportPanel.requestFocus();
            }
        });

        this.add(viewportPanel, BorderLayout.CENTER);
        
    }

    @Override
    protected void onInit() {
        System.out.println("Editor started!");
        framework = new Framework(viewportPanel.getGraphics(), viewportPanel.getSize(), viewportPanel);
        framework.entityInstanceConverters.add(new SimpleRect());
        framework.entityInstanceConverters.add(new ECSEntity());
        framework.loadLevel("src\\main\\java\\Projects\\MyProject2\\Levels\\level1.txt");
        viewportPanel.setVisible(true);
        viewportPanel.requestFocus();
    }

    @Override
    protected void onTick() {
        float speed = 5.0f;

        if (framework.keyCodeDownCodes.contains(KeyEvent.VK_UP)) {
            framework.cameraPos.y -= speed;
        } else if (framework.keyCodeDownCodes.contains(KeyEvent.VK_DOWN)) {
            framework.cameraPos.y += speed;
        }

        if (framework.keyCodeDownCodes.contains(KeyEvent.VK_LEFT)) {
            framework.cameraPos.x -= speed;
        } else if (framework.keyCodeDownCodes.contains(KeyEvent.VK_RIGHT)) {
            framework.cameraPos.x += speed;
        }

        if (viewportPanel.isVisible()) {
            framework.render();
            framework.swapBuffers(viewportPanel);
            while (viewportPanel.getGraphics() == null || viewportPanel.getWidth() < 1 || viewportPanel.getHeight() < 1);
            framework.update(viewportPanel.getGraphics(), viewportPanel.getSize());
            
        }
        // System.out.println("Game has ticked!");
    }

    @Override
    protected void onResized() {
        System.out.println(viewportPanel.getSize());
    }

    @Override
    protected void onDestroy() {
        System.out.println("Game has ended!");
    }
}
