package ultra3d.framework;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import engine4j.util.GameWindow;
import sr4j.Camera3D;
import sr4j.Matrix4x4;
import sr4j.OBJModel;
import sr4j.SR4JTexture2D;

public class GameTest extends GameWindow {
    protected U3DGraphicsEngine engine;
    protected JPanel viewport;
    protected Camera3D cam;
    Point lastMouse = new Point(0, 0);
    boolean mouseDown = false;
    ArrayList<Integer> keys = new ArrayList<>();

    public GameTest(int width, int height, String title) {
        super(width, height, title);
    }

    OBJModel objModel;
    OBJModel objModel2;
    SR4JTexture2D cubeTexture;
    SR4JTexture2D cubeTexture2;

    @Override
    protected void onInit() {
        viewport = new JPanel();
        //viewport.setPreferredSize(new Dimension(500, 500));
        this.setLayout(new BorderLayout());
        add(viewport, BorderLayout.CENTER);
        engine = new U3DGraphicsEngine(viewport);
        cam = new Camera3D();
        viewport.requestFocus();

        viewport.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (!keys.contains(keyCode)) {
                    keys.add(keyCode);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (int i = 0; i < keys.size(); i++) {
                    Integer key = keys.get(i);

                    if (key.equals(e.getKeyCode())) {
                        keys.remove(i);
                    }
                }
            }
        });

        viewport.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    lastMouse = getMousePosition();
                    mouseDown = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDown = false;
            }
        });

        // Create 3d Model
        objModel = new OBJModel("C:\\Users\\kmhjh\\OneDrive\\Desktop\\untitledgasdasdasdfasdsdfsdfsdffsdfdsfsdfsdfsdfsdfdfsdf.obj");
        objModel.parse();

        objModel2 = new OBJModel("C:\\Users\\kmhjh\\OneDrive\\Desktop\\TriangulatedSphere.obj");
        objModel2.parse();

        // Create the cube image
        BufferedImage source = null;

        try {
            //source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fdsfsdfdss.png"));
            source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fdsfsdfsdsd.png"));
            //source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\dfdsfds.png"));
        } catch (IOException e) {

        }

        if (source != null) {
            cubeTexture = new SR4JTexture2D(source);
        }

        // Create the cube image
        BufferedImage source2 = null;

        try {
            //source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fdsfsdfdss.png"));
            //source = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\fdsfsdfsdsd.png"));
            source2 = ImageIO.read(new File("C:\\Users\\kmhjh\\OneDrive\\Pictures\\dfdsfds.png"));
        } catch (IOException e) {

        }

        if (source2 != null) {
            cubeTexture2 = new SR4JTexture2D(source2);
        }
    }

    private void inputCallback() {
        if (mouseDown) {
            if (!getMousePosition().equals(lastMouse)) { 
                int dx = (int)getMousePosition().getX() - (int)lastMouse.getX();
                int dy = (int)getMousePosition().getY() - (int)lastMouse.getY();
                Robot robot = null;

                try {
                    robot = new Robot();
                }  catch (AWTException e) {
                    System.out.println(e);
                }
                
                if (robot != null) {
                    robot.mouseMove(getX() + (int)lastMouse.getX(), getY() + (int)lastMouse.getY());
                }

                lastMouse = getMousePosition();
                //System.out.println("END: " + e.getPoint());
                cam.yaw += dx * cam.cameraSencitivity;
                cam.pitch -= dy * cam.cameraSencitivity;
            }
        }

        if (keys.contains(KeyEvent.VK_W)) {
            cam.moveForward(1);
        } else if (keys.contains(KeyEvent.VK_S)) {
            cam.moveForward(-1);
        }

        if (keys.contains(KeyEvent.VK_A)) {
            cam.moveRight(-1);
        } else if (keys.contains(KeyEvent.VK_D)) {
            cam.moveRight(1);
        }

        if (keys.contains(KeyEvent.VK_Q)) {
            cam.moveUp(-1);
        } else if (keys.contains(KeyEvent.VK_E)) {
            cam.moveUp(1);
        }

        if (keys.contains(KeyEvent.VK_DOWN)) {
            cam.turnUp(-1);
        } else if (keys.contains(KeyEvent.VK_UP)) {
            cam.turnUp(1);
        }
    }

    @Override
    protected void onTick() {
        cam.update();
        inputCallback();

        engine.beginRender();
        engine.setViewMatrix(cam.viewMatrix);
        
        // Draw object 1
        for (int i = 0; i < 10; i++) {
            engine.setModelMatrix(Matrix4x4.translation(-3.0f * i, 0.0f, 0.0f));
            engine.setTexture(cubeTexture);
            engine.drawOBJModel(objModel);
        }

        // Draw object 2
        engine.setModelMatrix(Matrix4x4.translation(3.0f, 0.0f, 0.0f));
        engine.setTexture(cubeTexture2);
        engine.drawOBJModel(objModel2);

        engine.endRender();
    }

    @Override
    protected void onResized() {
        engine.validateEngine();
    }

    @Override
    protected void onDestroy() { }
}
