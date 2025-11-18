package softr4j;

public class ShaderProgram {
    public ShaderProgram() { }

    public Vector4 onVertexShaderCalled(Object input) {
        return new Vector4();
    }

    public void onGeometryShaderCalled(Object[] input) {

    }

    public Vector4 onPixelShaderCalled(int x, int y) {
        return new Vector4();
    }
}
