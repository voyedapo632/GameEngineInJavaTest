package Projects.MyProject1.Source;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import engine4j.util.BehavioralScript;
import engine4j.util.ECSEntity;
import engine4j.util.Vec3;

public class PlayerMovementScript extends BehavioralScript {
    public PlayerMovementScript(ECSEntity parent) {
        super(parent);
    }

    @Override
    public void onStart() {
        System.out.println("PlayerMovementScript was laoded!");
    }

    int maxJump = 250;
    boolean isJump = false;
    int jumpHeight = 0;
    float velocityY = 0.0f;

    @Override
    public void onTick() {
        Vec3 pos = (Vec3)parent.components.get(0).getParsedObjectValue();
        Vec3 camera = game.cameraPos;
        int speed = 10;
        int camOffsetX = 500;
        int camOffsetY = 300;

        if (parent.id.startsWith("thePlayer")) {
            if (!parent.hasCollided && !isJump) {
                pos.y += speed * velocityY;
                velocityY += 0.1;
            } else {
                velocityY = 0.0f;
            }

            if (game.keyCodeDownCodes.contains(KeyEvent.VK_SPACE)) {
                if (parent.hasCollided) {
                    isJump = true;
                    jumpHeight = (int)pos.y - maxJump;
                }
            }

            if (isJump) {
                if (pos.y > jumpHeight) {
                    pos.y -= speed * 2;
                } else {
                    isJump = false;
                }
            }

            parent.hasCollided = false;

            if (game.keyCodeDownCodes.contains(KeyEvent.VK_A)) {
                pos.x -= speed;
            } else if (game.keyCodeDownCodes.contains(KeyEvent.VK_D)) {
                pos.x += speed;
            }


            camera.x = (int)((pos.x - camOffsetX));
            camera.y = (int)((pos.y + camOffsetY));
        }
        
        System.out.println(parent.id);
    }

    @Override
    public void onShouldDraw(Graphics g) {
        
    }

    @Override
    public void onEnd() {
        if (parent.id.startsWith("thePlayer")) {
            Vec3 pos = (Vec3)parent.components.get(0).getParsedObjectValue();
            parent.components.get(0).value = Vec3.serialize(pos);
            game.saveLevel();
        }
        System.out.println("DONE");
    }

    @Override
    public void onDestroyed() {

    }
}
