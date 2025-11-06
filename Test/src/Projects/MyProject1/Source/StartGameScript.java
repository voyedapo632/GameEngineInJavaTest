package Projects.MyProject1.Source;

import java.awt.Color;
import java.awt.Graphics;

import engine4j.util.BehavioralScript;
import engine4j.util.ECSEntity;
import engine4j.util.ECSHelper;
import engine4j.util.Vec3; 

public class StartGameScript extends BehavioralScript {
    public StartGameScript(ECSEntity parent) {
        super(parent);
    }

    @Override
    public void onStart() {
        System.out.println("StartGameScript was laoded!");
    }

    float scaleFactor = 1.0f;
    float scaleFactorMax = 5.0f;

    @Override
    public void onTick() {
        Vec3 pos = (Vec3)parent.components.get(0).getParsedObjectValue();

        if (game.mousePosition != null) {
            // On click
            if (ECSHelper.isCollidePointRect(game.mousePosition.x, game.mousePosition.y, 
                    parent.screenX, parent.screenY, parent.screenScaleX, parent.screenScaleY) && game.mouseClicked) {
                System.out.println("Clicked!");
                game.switchToLevel("level2.txt", 25, Color.black);
            }

            // On hover
            if (ECSHelper.isCollidePointRect(game.mousePosition.x, game.mousePosition.y,
                parent.screenX, parent.screenY, parent.screenScaleX, parent.screenScaleY)) {
               scaleFactor = scaleFactorMax;
            } else {
                scaleFactor = 1.0f;
            }
        }
    }

    @Override
    public void onShouldDraw(Graphics g) {
        parent.screenX -= scaleFactor;
        parent.screenY -= scaleFactor;
        parent.screenScaleX += scaleFactor * 2;
        parent.screenScaleY += scaleFactor * 2;
    }

    @Override
    public void onEnd() {
        System.out.println("DONE");
    }

    @Override
    public void onDestroyed() {

    }
}
