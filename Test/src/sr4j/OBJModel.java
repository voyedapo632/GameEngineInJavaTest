package sr4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class OBJModel {
    public String contents;
    public ArrayList<Vector4> vertexes;
    public ArrayList<Vector4> textureVertexes;
    public ArrayList<Vector4> normals;
    public ArrayList<OBJFace> faces;
    public String fileName;

    public OBJModel(String filePath) {
        contents = "";
        fileName = "";
        File file = new File(filePath);

        fileName = file.getName();

        try (Scanner sn = new Scanner(file)) {
            while (sn.hasNextLine()) {
                String line = sn.nextLine();
                contents += line + '\n';
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        vertexes = new ArrayList<>();
        textureVertexes = new ArrayList<>();
        normals = new ArrayList<>();
        faces = new ArrayList<>();
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void parse() {
        for (String line : contents.split("\n")) {
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("#") || line.startsWith("o")) {
                continue;
            }

            // Vertexes
            if (line.startsWith("v ")) {
                String[] sepS = line.split(" ");
                
                float value1 = Float.parseFloat(sepS[1]);
                float value2 = Float.parseFloat(sepS[2]);
                float value3 = Float.parseFloat(sepS[3]);

                vertexes.add(new Vector4(value1, value2, value3, 1.0f));
            } else if (line.startsWith("vn ")) {
                String[] sepS = line.split(" ");
                
                float value1 = Float.parseFloat(sepS[1]);
                float value2 = Float.parseFloat(sepS[2]);
                float value3 = Float.parseFloat(sepS[3]);

                normals.add(new Vector4(value1, value2, value3, 1.0f));
            } else if (line.startsWith("vt ")) {
                String[] sepS = line.split(" ");
                
                float value1 = Float.parseFloat(sepS[1]);
                float value2 = Float.parseFloat(sepS[2]);

                textureVertexes.add(new Vector4(value1, value2, 1.0f, 1.0f));
            } else if (line.startsWith("f ")) {
                String[] sepS = line.split(" ");
                
                OBJFace value1 = OBJFace.parseFace(sepS[1]);
                OBJFace value2 = OBJFace.parseFace(sepS[2]);
                OBJFace value3 = OBJFace.parseFace(sepS[3]);
                
                faces.add(value1);
                faces.add(value2);
                faces.add(value3);
            }
        }
    }
}
