package softr4j;

public class RasterizationState {
    public int fillMode;
    public boolean useDepthClamp;
    public boolean isGammaCorrected;
    public float drawResolution;

    public RasterizationState(int fillMode, boolean useDepthClamp, boolean isGammaCorrected, float drawResolution) {
        this.fillMode = fillMode;
        this.useDepthClamp = useDepthClamp;
        this.isGammaCorrected = isGammaCorrected;
        this.drawResolution = drawResolution;
    }

    public static final int FILL = 0;
    public static final int WIREFRAME = 0;
    public static final int POINT = 0;
}
