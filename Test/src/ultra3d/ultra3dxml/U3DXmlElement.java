package ultra3d.ultra3dxml;

import java.util.ArrayList;

public class U3DXmlElement {
    private String tag;
    private ArrayList<U3DXmlAttribute> attributes;
    private ArrayList<U3DXmlToken> innerXml;

    public U3DXmlElement(String tag) {
        this.tag = tag;
        attributes = new ArrayList<>();
        innerXml = new ArrayList<>();
    }

    public U3DXmlElement(String tag, ArrayList<U3DXmlToken> innerXml) {
        this.tag = tag;
        attributes = new ArrayList<>();
        this.innerXml = innerXml;
    }

    public U3DXmlElement(String tag, ArrayList<U3DXmlAttribute> attributes, ArrayList<U3DXmlToken> innerXml) {
        this.tag = tag;
        this.attributes = attributes;
        this.innerXml = innerXml;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<U3DXmlAttribute> getAttributes() {
        return attributes;
    }

    public U3DXmlAttribute getAttributeByName(String name) {
        for (U3DXmlAttribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }

        return null;
    }

    public void setAttributes(ArrayList<U3DXmlAttribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<U3DXmlToken> getInnerXml() {
        return innerXml;
    }

    public void setInnerXml(ArrayList<U3DXmlToken> innerXml) {
        this.innerXml = innerXml;
    }

    public static U3DXmlElement parseElement(U3DXmlToken token, ArrayList<U3DXmlToken> tokonizedData) {
        if (!token.getType().equals(U3DXmlToken.ELEMENT)) {
            return null;
        }

        String tokenValue = token.getValue();
        int endOfTag = tokenValue.indexOf(" ");

        if (endOfTag == -1) {
            endOfTag = tokenValue.indexOf(">");
        }

        String tag = tokenValue.substring(tokenValue.indexOf("<") + 1, endOfTag);
        String openToken = "<" + tag;
        String closingToken = "</" + tag + ">";
        ArrayList<U3DXmlToken> innerXml = new ArrayList<>();

        if (!tokenValue.endsWith("/>")) {
            int offset = tokonizedData.indexOf(token);
            int endOffset = offset;
            int scopeLevel = 0;

            for (int i = offset; i < tokonizedData.size(); i++) {
                U3DXmlToken tokenOfI = tokonizedData.get(i);

                if (tokenOfI.getValue().startsWith(openToken)) {
                    scopeLevel++;
                } else if (tokenOfI.getValue().startsWith(closingToken)) {
                    scopeLevel--;
                }

                if (scopeLevel == 0) {
                    endOffset = i;
                    break;
                }
            }

            for (int i = offset + 1; i < endOffset; i++) {
                innerXml.add(tokonizedData.get(i));
            }
        }

        ArrayList<U3DXmlAttribute> attributes = new ArrayList<>();

        // Check if token has attributes
        if (tokenValue.contains("\"")) {
            String attributesStr = tokenValue.substring(tokenValue.indexOf(" "), tokenValue.indexOf(">"));
            ArrayList<U3DXmlToken> tokonizedAttributes = U3DXmlParser.tokonizeAttributes(attributesStr);

            for (U3DXmlToken attributeToken : tokonizedAttributes) {
                attributes.add(U3DXmlAttribute.parseAttribute(attributeToken));
            }
        }

        return new U3DXmlElement(tag, attributes, innerXml);
    }

    @Override
    public String toString() {
        String result = "<" + tag;

        for (U3DXmlAttribute attribute : attributes) {
            result += ' ' + attribute.toString();
        }

        result += ">\n";

        for (U3DXmlToken token : innerXml) {
            if (token.getType().equals(U3DXmlToken.ELEMENT) || token.getType().equals(U3DXmlToken.END_ELEMENT)) {
                result += '\t';
            }

            result += token.toString() + '\n';
        }

        return result + "</" + tag + '>';
    }
}