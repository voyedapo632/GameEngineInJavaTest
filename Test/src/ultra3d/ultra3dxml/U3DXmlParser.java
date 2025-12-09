package ultra3d.ultra3dxml;

import java.util.ArrayList;

public final class U3DXmlParser {
    public static String clearInput(String xmlInput) {
        String newXmlInput = xmlInput.replace("\r", "");

        while (newXmlInput.contains(" <")) {
            newXmlInput = newXmlInput.replace(" <", "<");
        }

        newXmlInput = newXmlInput.replace("\n<", "<");
        newXmlInput = newXmlInput.replace(">\n", ">");
        return newXmlInput;
    }

    public static ArrayList<U3DXmlToken> tokonizeAttributes(String input) {
        ArrayList<U3DXmlToken> tokens = new ArrayList<>();
        String clearInput = "";

        // Clear white space
        for (int i = 0, j = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '\"') {
                j++;
            }

            if (c == ' ' && j % 2 == 0) {
                continue;
            }

            clearInput += c;
        }

        // Parser
        int offset = 0;

        for (int i = 0, j = 0; i < clearInput.length(); i++) {
            char c = clearInput.charAt(i);

            if (c == '\"') {
                j++;

                if (j % 2 == 0) {
                    tokens.add(new U3DXmlToken(clearInput.substring(offset, i + 1), U3DXmlToken.ATTRIBUTE));
                    offset = i + 1;
                }
            }
        }

        return tokens;
    }

    public static ArrayList<String> tokonizeToString(String input) {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("");

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '<') {
                if (!tokens.getLast().isEmpty()) {
                    tokens.add("<");
                } else {
                    tokens.set(tokens.size() - 1, tokens.getLast() + '<');
                }
            } else if (c == '>') {
                tokens.set(tokens.size() - 1, tokens.getLast() + '>');
            } else if (tokens.getLast().endsWith(">")) {
                tokens.add(c + "");
            } else {
                tokens.set(tokens.size() - 1, tokens.getLast() + c);
            }
        }

        return tokens;
    }

    public static ArrayList<U3DXmlToken> tokonize(String input) {
        ArrayList<String> strTokens = tokonizeToString(input);
        ArrayList<U3DXmlToken> tokens = new ArrayList<U3DXmlToken>();

        for (String strToken : strTokens) {
            if (strToken.contains("</")) {
                tokens.add(new U3DXmlToken(strToken, U3DXmlToken.END_ELEMENT));
            } else if (strToken.contains("<!--")) {
                tokens.add(new U3DXmlToken(strToken, U3DXmlToken.COMMENT));
            } else if (strToken.contains("<")) {
                tokens.add(new U3DXmlToken(strToken, U3DXmlToken.ELEMENT));
            } else {
                tokens.add(new U3DXmlToken(strToken, U3DXmlToken.CONSTANT));
            }
        }

        return tokens;
    }

    public static ArrayList<U3DXmlElement> getTopLevelElements(ArrayList<U3DXmlToken> tokonizedInput) {
        ArrayList<U3DXmlElement> elements = new ArrayList<>();

        for (int i = 0; i < tokonizedInput.size(); i++) {
            if (i > tokonizedInput.size()) {
                break;
            }

            U3DXmlToken token = tokonizedInput.get(i);

            if (token.getType().equals(U3DXmlToken.ELEMENT)) {
                elements.add(U3DXmlElement.parseElement(token, tokonizedInput));
                i += U3DXmlElement.parseElement(token, tokonizedInput).getInnerXml().size() + 1;
            }
        }

        return elements;
    }

    public static ArrayList<String> getTopLevelConstants(ArrayList<U3DXmlToken> tokonizedInput) {
        ArrayList<String> constants = new ArrayList<>();

        for (int i = 0; i < tokonizedInput.size(); i++) {
            if (i > tokonizedInput.size()) {
                break;
            }

            U3DXmlToken token = tokonizedInput.get(i);

            if (token.getType().equals(U3DXmlToken.ELEMENT)) {
                i += U3DXmlElement.parseElement(token, tokonizedInput).getInnerXml().size() + 1;
            } else if (token.getType().equals(U3DXmlToken.CONSTANT)) {
                constants.add(token.getValue());
            }
        }

        return constants;
    }
}