package engine4j.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class EasyFiles {
    public static String readFile(String path) {
        FileReader fr;
        try {
            fr = new FileReader(path);
            String txt = "";
            int i;

            try {
                // Using read method
                while ((i = fr.read()) != -1) {
                    txt += (char)i;
                }
                fr.close();
            } catch (IOException ex) {
            }
            return txt;
        } catch (FileNotFoundException ex) {
        }

        return "File path not found."; 
    }

    public static void writeFile(String path, char[] text) {
        FileWriter fr;
        try {
            fr = new FileWriter(path);
            fr.write(text);
            fr.flush();
            fr.close();
        } catch (IOException ex) {
            System.out.println("TEST!");
        }
    }
}
