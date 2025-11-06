package engine4j.util;

public class ProjectSettings {
    public String name;
    public String defaultLevel;

    public ProjectSettings() { 
        name = "";
        defaultLevel = "";
    }

    public ProjectSettings(String name, String defaultLevel) {
        this.name = name;
        this.defaultLevel = defaultLevel;
    }

    public void loadFromFile(String path) {
        
    }
}
