package ultra3d.editor;

public class Main {
    public static void main(String[] args) {
        U3DEditor editor = new U3DEditor("C:\\Users\\voyedapo632\\Documents\\GitHub\\GameEngineInJavaTest\\Test\\src\\u3dprojects\\MyProject1");
        editor.start((long)(1000.0 / 1024.0)); // 1024 FPS
    }
}
