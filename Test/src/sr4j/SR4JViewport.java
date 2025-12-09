package sr4j;

public class SR4JViewport {
    public float x, y, width, height, minDepth, maxDepth;

    public SR4JViewport(float x, float y, float width, float height, float minDepth, float maxDepth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }
}
