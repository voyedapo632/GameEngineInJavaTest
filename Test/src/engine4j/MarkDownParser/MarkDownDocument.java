package engine4j.MarkDownParser;

import engine4j.util.SafeList;

public class MarkDownDocument {
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