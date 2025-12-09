package sr4j;

public class Matrix4x4 {
    public Vector4 x, y, z, w;

    public Matrix4x4(Vector4 x, Vector4 y, Vector4 z, Vector4 w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Matrix4x4(Vector4 x, Vector4 y, Vector4 z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = new Vector4();
    }

    public Matrix4x4(Vector4 x, Vector4 y) {
        this.x = x;
        this.y = y;
        this.z = new Vector4();
        this.w = new Vector4();
    }

    public static Matrix4x4 transpose(Matrix4x4 mat) {
        return new Matrix4x4(
            new Vector4(mat.x.x, mat.y.x, mat.z.x, mat.w.x),
            new Vector4(mat.x.y, mat.y.y, mat.z.y, mat.w.y),
            new Vector4(mat.x.z, mat.y.z, mat.z.z, mat.w.z),
            new Vector4(mat.x.w, mat.y.w, mat.z.w, mat.w.w)
        );
    }

    public static Matrix4x4 mul(Matrix4x4 a, Matrix4x4 b) {
        Matrix4x4 nb = transpose(b);

        return new Matrix4x4(
            new Vector4(Vector4.dotProduct(a.x, nb.x), Vector4.dotProduct(a.x, nb.y), Vector4.dotProduct(a.x, nb.z), Vector4.dotProduct(a.x, nb.w)),
            new Vector4(Vector4.dotProduct(a.y, nb.x), Vector4.dotProduct(a.y, nb.y), Vector4.dotProduct(a.y, nb.z), Vector4.dotProduct(a.y, nb.w)),
            new Vector4(Vector4.dotProduct(a.z, nb.x), Vector4.dotProduct(a.z, nb.y), Vector4.dotProduct(a.z, nb.z), Vector4.dotProduct(a.z, nb.w)),
            new Vector4(Vector4.dotProduct(a.w, nb.x), Vector4.dotProduct(a.w, nb.y), Vector4.dotProduct(a.w, nb.z), Vector4.dotProduct(a.w, nb.w))
        );
    }
   
    public static Matrix4x4 identity() {
        return new Matrix4x4(
            new Vector4(1.0f, 0.0f, 0.0f, 0.0f),
            new Vector4(0.0f, 1.0f, 0.0f, 0.0f),
            new Vector4(0.0f, 0.0f, 1.0f, 0.0f),
            new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
        );
    }

    public static Matrix4x4 translation(float x, float y, float z) {
        return new Matrix4x4(
            new Vector4(1.0f, 0.0f, 0.0f, x),
            new Vector4(0.0f, 1.0f, 0.0f, y),
            new Vector4(0.0f, 0.0f, 1.0f, z),
            new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
        );
    }

    public static Matrix4x4 scale(float x, float y, float z) {
        return new Matrix4x4(
            new Vector4(x, 0.0f, 0.0f, 0.0f),
            new Vector4(0.0f, y, 0.0f, 0.0f),
            new Vector4(0.0f, 0.0f, z, 0.0f),
            new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
        );
    }

    public static Matrix4x4 rotateX(float rad) {
        return new Matrix4x4(
            new Vector4(1.0f, 0.0f, 0.0f, 0.0f),
            new Vector4(0.0f, (float)Math.cos(rad), (float)Math.sin(rad), 0.0f),
            new Vector4(0.0f, -(float)Math.sin(rad), (float)Math.cos(rad), 0.0f),
            new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
        );
    }

    public static Matrix4x4 rotateY(float rad) {
        return new Matrix4x4(
            new Vector4((float)Math.cos(rad), 0.0f, -(float)Math.sin(rad), 0.0f),
            new Vector4(0.0f, 1.0f, 0.0f, 0.0f),
            new Vector4((float)Math.sin(rad), 0.0f, (float)Math.cos(rad), 0.0f),
            new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
        );
    }

    public static Matrix4x4 rotateZ(float rad) {
        return new Matrix4x4(
            new Vector4((float)Math.cos(rad), -(float)Math.sin(rad), 0.0f, 0.0f),
            new Vector4((float)Math.sin(rad), (float)Math.cos(rad), 0.0f, 0.0f),
            new Vector4(0.0f, 0.0f, 1.0f, 0.0f),
            new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
        );
    }

    public static Matrix4x4 lookAt(Vector4 pos, Vector4 tar, Vector4 up) {
        Vector4 dir = Vector4.normalize(Vector4.sub(pos, tar));
        Vector4 right = Vector4.normalize(Vector4.crossProduct(up, dir));
        Vector4 cameraUp = Vector4.crossProduct(dir, right);

        Matrix4x4 matrix = new Matrix4x4(
            new Vector4(right.x,    right.y,    right.z,    0.0f),
            new Vector4(cameraUp.x, cameraUp.y, cameraUp.z, 0.0f),
            new Vector4(dir.x,      dir.y,      dir.z,      0.0f),
            new Vector4(0.0f,  0.0f,       0.0f,       1.0f)
        );

        return Matrix4x4.mul(matrix, Matrix4x4.translation(pos.x, pos.y, pos.z));
    }
    
    public static Matrix4x4 perspective(float fov, float aspectRatio, float nearPlane, float farPlane) {
        fov /= 2;

        return new Matrix4x4(
            new Vector4(1.0f / (aspectRatio * (float)Math.tan(fov)), 0.0f, 0.0f, 0.0f),
            new Vector4(0.0f, 1 / (float)Math.tan(fov), 0.0f, 0.0f),
            new Vector4(0.0f, 0.0f, -((farPlane + nearPlane) / (farPlane - nearPlane)), -((2 * farPlane * nearPlane) / (farPlane - nearPlane))),
            new Vector4(0.0f, 0.0f, -1.0f, 0.0f)
        );
    }
}

