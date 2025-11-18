package engine4j.MarkDownParser;

import engine4j.util.SafeList;

class MarkDownAttribute {
    public String name;
    public String value;

    public MarkDownAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

class MarkDownToken {
    public String value;
    public int offset;

    public MarkDownToken(String value, int offset) {
        this.value = value;
        this.offset = offset;
    }
}

class MarkDownElement {
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

        for (int i = 0; i < data.length(); i++) {
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

class MarkDownDocument {
    public String name;
    public SafeList<MarkDownElement> elements;

    public MarkDownDocument(String name) {
        this.name = name;
        elements = new SafeList<MarkDownElement>();
    }

    public void appendElement(MarkDownElement e) {
        elements.add(e);
    }

    public SafeList<MarkDownElement> getElementsByTag(String elementName) {
        SafeList<MarkDownElement> gotElements = new SafeList<MarkDownElement>();

        for (int i = 0; i < elements.getSize(); i++) {
            MarkDownElement e = elements.get(i);

            if (e.name.startsWith(elementName)) {
                gotElements.add(e);
            }
        }

        return gotElements;
    }
}

public class MarkDownReader {
    public static void parseAttributes(SafeList<MarkDownAttribute> dest, String content) {
        // remove whitespace
        String clearedStr = "";
        boolean isInStr = false;
        
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '"') {
                if (isInStr) {
                    isInStr = false;
                } else {
                    isInStr = true;
                }
            }

            if (!isInStr && content.charAt(i) == ' ') {
                continue;
            }

            clearedStr += content.charAt(i);
        }

        // Split between every even occourence of '"' starting from count = 0
        SafeList<String> tokens = new SafeList<String>();
        int count = 0;
        int offset = 0;

        for (int i = 0; i < clearedStr.length(); i++) {
            if (clearedStr.charAt(i) == '"') {
                count++;

                if (count % 2 == 0) {
                    tokens.add(clearedStr.substring(offset, i + 1));
                    offset = i + 1;
                }
            }
        }

        // Pasrse the tokens
        for (int i = 0; i < tokens.getSize(); i++) {
            String token = tokens.get(i);

            dest.add(new MarkDownAttribute(token.substring(0, token.indexOf("=")), 
                token.substring(token.indexOf("\"") + 1, token.lastIndexOf("\""))));
        }
    }

    public static SafeList<MarkDownToken> getTokens(String content) {
        SafeList<MarkDownToken> tokens = new SafeList<MarkDownToken>();

        int begin =  0;
        int end = 0;

        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '<') {
                begin = i;
            } else if (content.charAt(i) == '>') {
                end = i + 1;

                if (!content.startsWith("<!--", begin)) {
                    tokens.add(new MarkDownToken(content.substring(begin, end), begin));
                }
            }
        }

        return tokens;
    }

    public static void read(MarkDownDocument document, String content) {
        SafeList<MarkDownToken> tokens = MarkDownReader.getTokens(content);

        for (int i = 0; i < tokens.getSize(); i++) {
            MarkDownElement element = new MarkDownElement(tokens.get(i), content);
            
            if (element.isValid) {
                document.elements.add(element);
            }
        }
    }

    public MarkDownReader(MarkDownDocument document, String content) {
        SafeList<MarkDownToken> tokens = MarkDownReader.getTokens(content);

        for (int i = 0; i < tokens.getSize(); i++) {
            MarkDownElement element = new MarkDownElement(tokens.get(i), content);
            
            if (element.isValid) {
                document.elements.add(element);
            }
        }
    }
}
