package softr4j;

public class Viewport {
    float x, y, width, height, minDepth, maxDepth;

    public Viewport(float x, float y, float width, float height, float minDepth, float maxDepth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }
}
