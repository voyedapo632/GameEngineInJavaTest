package softr4j;

public class Vector4 {
    public float x, y, z, w;

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 0.0f;
    }

    public Vector4(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public Vector4(float x) {
        this.x = x;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public Vector4() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public static Vector4 clamp(Vector4 v) {
        Vector4 newVec = new Vector4(v.x, v.y, v.z, v.w);
        
        if (newVec.x < 0.0f) {
            newVec.x = 0.0f; 
        } else if (newVec.x > 1.0f) {
            newVec.x = 1.0f;
        }

        if (newVec.y < 0.0f) {
            newVec.y = 0.0f; 
        } else if (newVec.y > 1.0f) {
            newVec.y = 1.0f;
        }

        if (newVec.z < 0.0f) {
            newVec.z = 0.0f; 
        } else if (newVec.z > 1.0f) {
            newVec.z = 1.0f;
        }

        if (newVec.w < 0.0f) {
            newVec.w = 0.0f; 
        } else if (newVec.w > 1.0f) {
            newVec.w = 1.0f;
        }

        return newVec;
    }

    public static Vector4 add(Vector4 a, Vector4 b) {
        return new Vector4(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
    }

    public static Vector4 add(Vector4 a, float b) {
        return new Vector4(a.x + b, a.y + b, a.z + b, a.w + b);
    }
   
    public static Vector4 sub(Vector4 a, Vector4 b) {
        return new Vector4(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
    }

    public static Vector4 sub(Vector4 a, float b) {
        return new Vector4(a.x - b, a.y - b, a.z - b, a.w - b);
    }
   
    public static Vector4 mul(Vector4 a, Vector4 b) {
        return new Vector4(a.x * b.x, a.y * b.y, a.z * b.z, a.w * b.w);
    }

    public static Vector4 mul(Vector4 a, float b) {
        return new Vector4(a.x * b, a.y * b, a.z * b, a.w * b);
    }
   
    public static Vector4 mul(Vector4 a, Matrix4x4 b) {
        return new Vector4(Vector4.dotProduct(a, b.x), Vector4.dotProduct(a, b.y), Vector4.dotProduct(a, b.z), Vector4.dotProduct(a, b.w));
    }
   
    public static Vector4 div(Vector4 a, Vector4 b) {
        return new Vector4(a.x / b.x, a.y / b.y, a.z / b.z, a.w / b.w);
    }

    public static Vector4 div(Vector4 a, float b) {
        return new Vector4(a.x / b, a.y / b, a.z / b, a.w / b);
    }
   
    public static float dotProduct(Vector4 a, Vector4 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }
   
    public static Vector4 crossProduct(Vector4 a, Vector4 b) {
        return new Vector4((a.y * b.z) - (a.z * b.y), (a.z * b.x) - (a.x * b.z), (a.x * b.y) - (a.y * b.x), a.w);
    }

    public static Vector4 normalize(Vector4 v) {
        float len = (float)Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        return new Vector4(v.x / len, v.y / len, v.z / len, v.w);
    }
}

