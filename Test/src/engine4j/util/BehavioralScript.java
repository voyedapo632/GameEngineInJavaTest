package engine4j.util;

import java.awt.Graphics;

import engine4j.Framework;

public class BehavioralScript {
    public String scriptId;
    public ECSEntity parent;
    public Framework game;

    public BehavioralScript(ECSEntity parent) {
        this.parent = parent;
        this.game = (Framework)parent.host;
    }

    public void onStart() { }
    public void onTick() { }
    public void onShouldDraw(Graphics g) {  }
    public void onEnd() { }
    public void onDestroyed() { }
}