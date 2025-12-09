package sr4j;

import java.awt.Color;

public class SR4JShader {
    public static final int SHADER_TYPE_VERTEX = 0;
    public static final int SHADER_TYPE_GEOMETRY = 1;
    public static final int SHADER_TYPE_FRAGMENT = 2;

    protected SR4JDevice device;
    protected int shaderType;

    public SR4JShader(SR4JDevice device, int shaderType) {
        this.device = device;
        this.shaderType = shaderType;
    }

    public final SR4JDevice getDevice() {
        return device;
    }

    public final int getShaderType() {
        return shaderType;
    }
    
    public final Vector4 getFragColor() {
        return null;
    }
    
    public final Vector4 getFragPosition() {
        return device.fragPosition;
    }
    
    public final Vector4 getTextureFragColor() {
        return null;
    }

    public final Vector4 getTextureFragPosition() {
        return device.textureFragPos;
    }

    public final void outFragColor(Vector4 color) {
        device.fragColor = color;
    }
    
    public final Object vertexFromIndex(int index) {
        return device.vertexBuffer.getData()[index];
    }

    // Array of points to array of triangles
    // Triangle:
    //     (x, y, z), (r, b, b)
    //     (x, y, z), (r, b, b)
    //     (x, y, z), (r, b, b)
    // Triangle:
    //     (x, y, z), (r, b, b)
    //     (x, y, z), (r, b, b)
    //     (x, y, z), (r, b, b)
    public Vector4[] pointsToTriangle(Vector4[] points) {
        return null;
    }

    public Vector4 pointOfInterception(Vector4 a, Vector4 b) {

        return null;
    }

    public final void outPosition(Vector4 position) {
        // if (position.w < 0) {
        //     position.w = 0;
        // }
// 
        // position.x /= position.w;
        // position.y /= position.w;

        device.positions[device.index] = position;
    }

    public final void outColor(Vector4 color) {
        device.colors[device.index] = color;
    }
    
    public final void outTextureCoord(Vector4 coord) {
        device.textureCoords[device.index] = coord;
    }

    public final Vector4 fromTexture2D(Vector4 uv, int index) {
        if (device.textures[index] == null) {
            return device.getDefaultColor();
        }

        int u = (int)(uv.x * device.textures[index].getWidth());
        int v = (int)(uv.y * device.textures[index].getHeight());

        int col = device.textures[index].getPixel(u, device.textures[index].getHeight() - v);
        Color col2 = new Color(col);
        return new Vector4(col2.getRed() / 255.0f, col2.getGreen() / 255.0f, col2.getBlue() / 255.0f, 1.0f);
    }

    public final Object register(int index) {
        return device.registry[index];
    }

    public Object main(Object in) {
        return null;
    }
}
