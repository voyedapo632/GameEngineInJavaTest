package engine4j.MarkDownParser;

import engine4j.util.SafeList;

public class MarkDownElement {
    public String name;
    public String content;
    public String strArgs;
    public SafeList<MarkDownAttribute> attributes;
    public boolean isValid;
    
    public MarkDownElement(MarkDownToken mdToken, String data) {
        isValid = true;
        attributes = new SafeList<MarkDownAttribute>();

        if (!mdToken.value.startsWith("</") && !mdToken.value.endsWith("/>")) {
            String open = mdToken.value.substring(0, mdToken.value.length() - 1);

            if (open.contains(" ")) {
                open = open.substring(0, open.indexOf(" ")) + "";
                MarkDownReader.parseAttributes(attributes, 
                    mdToken.value.substring(mdToken.value.indexOf(" ") + 1, mdToken.value.indexOf(">")));
            }
            
            String close = "</" + open.substring(1, open.length()) + ">";
            
            //System.out.println("index=" + index + ", " + open + ", " + close);
            String gotContent = getScrope(data, open, close, mdToken.offset);

            gotContent = gotContent.substring(gotContent.indexOf(">") + 1, gotContent.length());

            //System.out.println("CONTENT=\"" + gotContent + "\"");

            name = open.substring(1, open.length());
            content = gotContent;
        } else if (mdToken.value.endsWith("/>")) {
            String open = mdToken.value;
            if (open.contains(" ")) {
                open = open.substring(0, open.indexOf(" "));
                MarkDownReader.parseAttributes(attributes, 
                    mdToken.value.substring(mdToken.value.indexOf(" ") + 1, mdToken.value.indexOf("/>")));
            }
            
            //System.out.println("index=" + index + ", " + open);
            name = open.substring(1, open.length());
            content = "";
        } else {
            isValid = false;
        }
    }

    private String getScrope(String data, String startToken, String endToken, int offset) {
        int scopeLevel = 0;
        int begin = -1;

        for (int i = offset; i < data.length(); i++) {
            if (data.startsWith(startToken, i)) {
                scopeLevel++;

                if (begin == -1) {
                    begin = i;
                }
            } else if (data.startsWith(endToken, i)) {
                scopeLevel--;

                if (scopeLevel == 0) {
                    return data.substring(begin, i);
                }
            }
        }

        return "";
    }

    public MarkDownAttribute getAttributeByName(String name) {
        for (int i = 0; i < attributes.getSize(); i++) {
            MarkDownAttribute a = attributes.get(i);

            if (a.name.equals(name)) {
                return a;
            }
        }

        return null;
    }

    public void appendAttribute(MarkDownAttribute a) {
        attributes.add(a);
    }
}