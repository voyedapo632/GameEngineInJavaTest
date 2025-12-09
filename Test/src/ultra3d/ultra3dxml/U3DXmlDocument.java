package ultra3d.ultra3dxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class U3DXmlDocument {
    private ArrayList<U3DXmlToken> tokonizedInput;
    private ArrayList<U3DXmlElement> elements;
    private ArrayList<String> constants;

    public U3DXmlDocument() {
        tokonizedInput = new ArrayList<>();
        elements = new ArrayList<>();
        constants = new ArrayList<>();
    }

    public U3DXmlDocument(String input) {
        tokonizedInput = U3DXmlParser.tokonize(U3DXmlParser.clearInput(input));
        elements = U3DXmlParser.getTopLevelElements(tokonizedInput);
        constants = U3DXmlParser.getTopLevelConstants(tokonizedInput);
    }

    public U3DXmlDocument(ArrayList<U3DXmlToken> innerXml) {
        tokonizedInput = (ArrayList<U3DXmlToken>) innerXml.clone();
        elements = U3DXmlParser.getTopLevelElements(tokonizedInput);
        constants = U3DXmlParser.getTopLevelConstants(tokonizedInput);
    }

    public U3DXmlDocument(U3DXmlElement element) {
        tokonizedInput = (ArrayList<U3DXmlToken>) element.getInnerXml().clone();
        elements = U3DXmlParser.getTopLevelElements(tokonizedInput);
        constants = U3DXmlParser.getTopLevelConstants(tokonizedInput);
    }

    public ArrayList<U3DXmlElement> getElementsByTagName(String tag) {
        ArrayList<U3DXmlElement> matchedElements = new ArrayList<>();

        for (U3DXmlElement element : elements) {
            if (element.getTag().equals(tag)) {
                matchedElements.add(element);
            }
        }

        return matchedElements;
    }

    public static ArrayList<U3DXmlElement> getElementsByTagName(ArrayList<U3DXmlToken> innerXml, String tag) {
        ArrayList<U3DXmlElement> matchedElements = new ArrayList<>();

        for (U3DXmlElement element : new U3DXmlDocument(innerXml).getElements()) {
            if (element.getTag().equals(tag)) {
                matchedElements.add(element);
            }
        }

        return matchedElements;
    }

    public static ArrayList<U3DXmlElement> getElementsByTagName(U3DXmlDocument document, String tag) {
        ArrayList<U3DXmlElement> matchedElements = new ArrayList<>();

        for (U3DXmlElement element : document.getElements()) {
            if (element.getTag().equals(tag)) {
                matchedElements.add(element);
            }
        }

        return matchedElements;
    }

    public static ArrayList<U3DXmlElement> getElementsByTagName(U3DXmlElement element, String tag) {
        ArrayList<U3DXmlElement> matchedElements = new ArrayList<>();

        for (U3DXmlElement _element : new U3DXmlDocument(element).getElements()) {
            if (_element.getTag().equals(tag)) {
                matchedElements.add(_element);
            }
        }

        return matchedElements;
    }

    public ArrayList<U3DXmlElement> getElements() {
        return elements;
    }

    public ArrayList<String> getConstants() {
        return constants;
    }

    public static U3DXmlDocument readFile(String path) {
        File file = new File(path);
        Scanner sn;
        
        try {
            String content = "";
            sn = new Scanner(file);

            while (sn.hasNextLine()) {
                content += sn.nextLine();
            }

            sn.close();
            return new U3DXmlDocument(content);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        return new U3DXmlDocument();
    }
}