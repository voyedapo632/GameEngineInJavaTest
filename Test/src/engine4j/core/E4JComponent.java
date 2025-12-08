package engine4j.core;

import engine4j.util.SafeList;

public class E4JComponent extends E4JObject {
    protected SafeList<E4JAttribute> attributes;

    public E4JComponent(String id) {
        super(id);
        attributes = new SafeList<>();
    }

    public void addAttribute(E4JAttribute attribute) {
        if (!attributes.contains(attribute)) {
            attribute.setParent(this);
            attributes.add(attribute);
        }
    }

    public E4JAttribute getAttribute(String id) {
        for (int i = 0; i < attributes.getSize(); i++) {
            E4JAttribute attribute = attributes.get(i);

            if (attribute.getId().equals(id)) {
                return attribute;
            }
        }

        return null;
    }

    public SafeList<E4JAttribute> getAttributes() {
        return attributes;
    }

    public void removeAttribute(String id) {
        for (int i = 0; i < attributes.getSize(); i++) {
            E4JAttribute attribute = attributes.get(i);

            if (attribute.getId().equals(id)) {
                attributes.remove(i);
                return;
            }
        }
    }

    @Override
    public String toString() { 
        String result = String.format("<%s id=\"%s\" parent=\"%s\">\n", getType(), id, parent.getId());

        for (int i = 0; i < attributes.getSize(); i++) {
            result += "    " + attributes.get(i).toString() + '\n';
        }

        return result + String.format("</%s>", getType());
    }
}
