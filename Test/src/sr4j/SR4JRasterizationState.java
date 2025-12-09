package sr4j;

public class SR4JRasterizationState {
    public static final int DEPTH_TEST_NONE = 0;
    public static final int DEPTH_TEST_ENABLE = 1;
    public static final int DEPTH_TEST_INVERT = 2;

    public int fillMode;
    public boolean useDepthClamp;
    public boolean isGammaCorrected;
    public float drawResolution;
    public int depthTestMode;

    public SR4JRasterizationState(int fillMode, boolean useDepthClamp, boolean isGammaCorrected, float drawResolution, int depthTestMode) {
        this.fillMode = fillMode;
        this.useDepthClamp = useDepthClamp;
        this.isGammaCorrected = isGammaCorrected;
        this.drawResolution = drawResolution;
        this.depthTestMode = depthTestMode;
    }

    public static final int FILL = 0;
    public static final int WIREFRAME = 0;
    public static final int POINT = 0;
}
