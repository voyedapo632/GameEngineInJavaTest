package engine4j;

import engine4j.util.SafeList;

public class Main {
    public static void main(String[] args) {
        SafeList<String> list = new SafeList<>(10);

        for (int i = 0; i < 61; i++) {
            list.add("Hello, World! x (" + i + ")");
        }

        list.uncheck(3);

        for (int i = 0; i < list.getSize(); i++) {
            System.out.println(list.get(i));
        }
    }
}
