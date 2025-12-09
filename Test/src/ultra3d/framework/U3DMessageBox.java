package ultra3d.framework;

import java.util.ArrayList;

public class U3DMessageBox {
    protected ArrayList<String> messages;

    public U3DMessageBox() {
        messages = new ArrayList<>();
    }

    public void appendMessage(String message) {
        messages.add(message);
    }

    public boolean getMessage(String dest) {
        if (messages.isEmpty()) {
            return false;
        }

        dest = messages.get(0);
        messages.remove(0);

        return true;
    }

    public boolean hasMessage(String message) {
        return messages.contains(message);
    }

    public boolean hasValidMessage(String[] validMessages) {
        for (String message : validMessages) {
            if (messages.contains(message)) {
                return true;
            }
        }
        
        return false;
    }

    public void removeValidMessage(String[] validMessages) {
        for (String message : validMessages) {
            if (messages.contains(message)) {
                messages.remove(message);
            }
        }
    }

    public void clear() {
        messages.clear();
    }
}
