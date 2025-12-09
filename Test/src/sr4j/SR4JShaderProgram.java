package sr4j;

import java.util.ArrayList;

public class SR4JShaderProgram {
    protected SR4JDevice device;
    protected ArrayList<SR4JShader> vertexShaders;
    protected ArrayList<SR4JShader> geometryShaders;
    protected ArrayList<SR4JShader> fragmentShaders;

    public SR4JShaderProgram(SR4JDevice device) {
        this.device = device;
        vertexShaders = new ArrayList<>();
        geometryShaders = new ArrayList<>();
        fragmentShaders = new ArrayList<>();
    }

    public void bindShader(SR4JShader shader) {
        if (shader == null) {
            return;
        }

        switch (shader.getShaderType()) {
            case SR4JShader.SHADER_TYPE_VERTEX -> {
                vertexShaders.add(shader);
            } case SR4JShader.SHADER_TYPE_GEOMETRY -> {
                geometryShaders.add(shader);
            } case SR4JShader.SHADER_TYPE_FRAGMENT -> {
                fragmentShaders.add(shader);
            }
        }
    }

    public void unbindShader(SR4JShader shader) {
        if (shader == null) {
            return;
        }

        switch (shader.getShaderType()) {
            case SR4JShader.SHADER_TYPE_VERTEX -> {
                vertexShaders.remove(shader);
            } case SR4JShader.SHADER_TYPE_GEOMETRY -> {
                geometryShaders.remove(shader);
            } case SR4JShader.SHADER_TYPE_FRAGMENT -> {
                fragmentShaders.remove(shader);
            }
        }
    }

    public void callVertexShaders(Object in) {
        Object lastInput = in;

        for (SR4JShader vertexShader : vertexShaders) {
            lastInput = vertexShader.main(lastInput);
        }
    }

    public void callGeometryShaders(Object in) {
        Object lastInput = in;

        for (SR4JShader geometryShader : geometryShaders) {
            lastInput = geometryShader.main(lastInput);
        }
    }

    public void callFragmentShaders(Object in) {
        Object lastInput = in;

        for (SR4JShader fragmentShader : fragmentShaders) {
            lastInput = fragmentShader.main(lastInput);
        }
    }
}
