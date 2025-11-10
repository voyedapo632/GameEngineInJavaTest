package engine4j.editor;

import java.awt.Dimension;
import javax.swing.JFrame;

class Vector4 {
	float x, y, z, w;

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

	public static Vector4 add(Vector4 a, Vector4 b) {
	    return new Vector4(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
	}
	
	public static Vector4 sub(Vector4 a, Vector4 b) {
	    return new Vector4(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
	}
	
	public static Vector4 mul(Vector4 a, Vector4 b) {
	    return new Vector4(a.x * b.x, a.y * b.y, a.z * b.z, a.w * b.w);
	}
	
	public static Vector4 mul(Vector4 a, Matrix4x4 b) {
	    return new Vector4(Vector4.dotProduct(a, b.x), Vector4.dotProduct(a, b.y), Vector4.dotProduct(a, b.z), Vector4.dotProduct(a, b.w));
    }
	
	public static Vector4 div(Vector4 a, Vector4 b) {
	    return new Vector4(a.x / b.x, a.y / b.y, a.z / b.z, a.w / b.w);
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

class Matrix4x4 {
	Vector4 x, y, z, w;

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

    public Matrix4x4 transpose(Matrix4x4 mat) {
        return new Matrix4x4(
	        new Vector4(mat.x.x, mat.y.x, mat.z.x, mat.w.x),
	        new Vector4(mat.x.y, mat.y.y, mat.z.y, mat.w.y),
	        new Vector4(mat.x.z, mat.y.z, mat.z.z, mat.w.z),
	        new Vector4(mat.x.w, mat.y.w, mat.z.w, mat.w.w)
	    );
    }

    public Matrix4x4 mul(Matrix4x4 a, Matrix4x4 b) {
        Matrix4x4 nb = transpose(b);

        return new Matrix4x4(
	        new Vector4(Vector4.dotProduct(a.x, nb.x), Vector4.dotProduct(a.x, nb.y), Vector4.dotProduct(a.x, nb.z), Vector4.dotProduct(a.x, nb.w)),
	        new Vector4(Vector4.dotProduct(a.y, nb.x), Vector4.dotProduct(a.y, nb.y), Vector4.dotProduct(a.y, nb.z), Vector4.dotProduct(a.y, nb.w)),
	        new Vector4(Vector4.dotProduct(a.z, nb.x), Vector4.dotProduct(a.z, nb.y), Vector4.dotProduct(a.z, nb.z), Vector4.dotProduct(a.z, nb.w)),
	        new Vector4(Vector4.dotProduct(a.w, nb.x), Vector4.dotProduct(a.w, nb.y), Vector4.dotProduct(a.w, nb.z), Vector4.dotProduct(a.w, nb.w))
        );
    }
	
	public Matrix4x4 identity() {
	    return new Matrix4x4(
	        new Vector4(1.0f, 0.0f, 0.0f, 0.0f),
	        new Vector4(0.0f, 1.0f, 0.0f, 0.0f),
	        new Vector4(0.0f, 0.0f, 1.0f, 0.0f),
	        new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
	    );
	}

    public Matrix4x4 translation(float x, float y, float z) {
	    return new Matrix4x4(
	        new Vector4(1.0f, 0.0f, 0.0f, x),
	        new Vector4(0.0f, 1.0f, 0.0f, y),
	        new Vector4(0.0f, 0.0f, 1.0f, z),
	        new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
	    );
	}

    public Matrix4x4 scale(float x, float y, float z) {
	    return new Matrix4x4(
	        new Vector4(x, 0.0f, 0.0f, 0.0f),
	        new Vector4(0.0f, y, 0.0f, 0.0f),
	        new Vector4(0.0f, 0.0f, z, 0.0f),
	        new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
	    );
	}

    public Matrix4x4 reotateX(float rad) {
	    return new Matrix4x4(
	        new Vector4(1.0f, 0.0f, 0.0f, 0.0f),
	        new Vector4(0.0f, (float)Math.cos(rad), (float)Math.sin(rad), 0.0f),
	        new Vector4(0.0f, -(float)Math.sin(rad), (float)Math.cos(rad), 0.0f),
	        new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
	    );
	}

    public Matrix4x4 reotateY(float rad) {
	    return new Matrix4x4(
	        new Vector4((float)Math.cos(rad), 0.0f, -(float)Math.sin(rad), 0.0f),
	        new Vector4(0.0f, 1.0f, 0.0f, 0.0f),
	        new Vector4((float)Math.sin(rad), 0.0f, (float)Math.cos(rad), 0.0f),
	        new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
	    );
	}

    public Matrix4x4 reotateZ(float rad) {
	    return new Matrix4x4(
	        new Vector4((float)Math.cos(rad), -(float)Math.sin(rad), 0.0f, 0.0f),
	        new Vector4((float)Math.sin(rad), (float)Math.cos(rad), 0.0f, 0.0f),
	        new Vector4(0.0f, 0.0f, 1.0f, 0.0f),
	        new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
	    );
	}

    public Matrix4x4 lookAt(Vector4 position, Vector4 target, Vector4 at) {

        return null;
    }

    public Matrix4x4 perspective(float fov, float aspectRatio, float near, float far) {
        
        return null;
    }
}

public class Main {
    public static ProjectBrowser projectBrowser = new ProjectBrowser();
    public static Editor editor;
    
    public static void main(String[] args) {
        JFrame splashScreen = new JFrame();
        splashScreen.setSize(new Dimension(600, 350));
        splashScreen.setUndecorated(true);
        splashScreen.setLocationRelativeTo(null);
        splashScreen.setVisible(true);

        // Splash screen
        // try {
        //     TimeUnit.SECONDS.sleep(2);
        // } catch (InterruptedException e) {
        // }

        splashScreen.setVisible(false);

        //editor.start((long)(1000.0 / 60.0)); // 60 FPS
        projectBrowser.setLocationRelativeTo(null);
        projectBrowser.start((long)(1000.0 / 60.0)); // 60 FPS

        Matrix4x4 mat4x4 = new Matrix4x4(
            new Vector4(1, 2, 3, 1), 
            new Vector4(3, 2, 1, 1),
            new Vector4(1, 2, 3, 1)
        );

        Vector4 result2 = Vector4.mul(new Vector4(4, 5, 6, 1), mat4x4);
        System.out.printf("Result: %f, %f, %f, %f\n", result2.x, result2.y, result2.z, result2.w);
    }
}
