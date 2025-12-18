package sr4j;

public class Camera3D {
    public float cameraSpeed = 0.3f;
    public float cameraSencitivity = 0.25f;
    public float yaw = -90.0f;
    public float pitch = 0.0f;
    public Vector4 cameraPos =   new Vector4(0.0f, 0.0f, -8.0f);
    public Vector4 cameraFront = new Vector4(0.0f, 0.0f, 0.0f);
    public Vector4 cameraUp =    new Vector4(0.0f, 1.0f, 0.0f);
    public Matrix4x4 viewMatrix;

    public Camera3D() {

    }

    public void moveForward(float dir) {
        cameraPos = Vector4.sub(cameraPos, Vector4.mul(cameraFront, cameraSpeed * dir));
    }

    public void moveRight(float dir) {
        cameraPos = Vector4.add(cameraPos,
                            Vector4.mul(Vector4.normalize(Vector4.crossProduct(cameraFront, cameraUp)),
                                        -cameraSpeed * dir));
    }

    public void moveUp(float dir) {
        cameraPos.y += -cameraSpeed * dir;
    }

    public void turnRight(float dir) {
        yaw += cameraSencitivity * dir;
    }

    public void turnUp(float dir) {
        pitch += cameraSencitivity * dir;
    }

    public void update() {
        Vector4 direction = new Vector4((float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch)),
                                    (float)Math.sin(Math.toRadians(pitch)),
                                    (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch)), 0.0f);

        cameraFront = Vector4.normalize(direction);

        viewMatrix = Matrix4x4.lookAt(
                    cameraPos,
                    Vector4.add(cameraPos, cameraFront),
                    cameraUp
                    );
    }
}
