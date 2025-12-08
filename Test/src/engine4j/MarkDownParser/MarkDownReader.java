package engine4j.MarkDownParser;

import engine4j.util.SafeList;

class MarkDownToken {
    public String value;
    public int offset;

    public MarkDownToken(String value, int offset) {
        this.value = value;
        this.offset = offset;
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
