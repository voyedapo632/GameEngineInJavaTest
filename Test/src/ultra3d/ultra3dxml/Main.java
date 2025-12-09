package ultra3d.ultra3dxml;

public class Main {
    public static void main(String[] args) {
        U3DXmlDocument document = U3DXmlDocument.readFile("src\\main\\java\\ultra3d\\ultra3dxml\\xmlExample.xml");
        U3DXmlDocument root = new U3DXmlDocument(document.getElementsByTagName("Root").get(0));

        for (U3DXmlElement gameObject : root.getElementsByTagName("GameObject")) {
            System.out.println(gameObject.getTag() + " " + gameObject.getAttributes());

            for (U3DXmlElement component : U3DXmlDocument.getElementsByTagName(gameObject, "Component")) {
                System.out.println("    " + component.getTag() + " " + component.getAttributes());

                for (U3DXmlElement attribute : U3DXmlDocument.getElementsByTagName(component, "Attribute")) {
                    System.out.println("        " + attribute.getTag() + " " + attribute.getAttributes() + " " + attribute.getInnerXml());
                }
            }
        }
    }
}
