package engine4j.util;

import java.util.Vector;

public class SafeList<T> {
    private Object[] data;
    private boolean[] checkList;
    private int allocatedSize;
    private int localSize;
    private int allocateSteps;

    public SafeList(int allocatedSize) {
        this.allocatedSize = allocatedSize;
        data = new Object[this.allocatedSize];
        checkList = new boolean[this.allocatedSize];
        this.localSize = 0;
        this.allocateSteps = allocatedSize;
    }

    public SafeList() {
        this.allocatedSize = 1024;
        data = new Object[this.allocatedSize];
        checkList = new boolean[this.allocatedSize];
        this.localSize = 0;
        this.allocateSteps = allocatedSize;
    }

    public T get(int index) {
        if (index >= 0 && index < data.length) {
            if (checkList[index]) {
                return (T)data[index];
            }
        }
        
        return null;
    }

    public T getLast() {
        if (checkList[localSize - 1]) {
            return (T)data[localSize - 1];
        }

        return null;
    }

    public void set(int index, T value) {
        if (index >= 0 && index < data.length) {
            data[index] = value;
            checkList[index] = true;
        }
    }

    public void add(T value) {
        if (localSize >= allocatedSize) {
            reallocte(allocatedSize + allocateSteps);
        }

        data[localSize] = value;
        checkList[localSize] = true;
        localSize++;
    }

    public void reallocte(int newSize) {
        allocatedSize = newSize;
        Object[] newData = new Object[allocatedSize];
        boolean[] newCheckList = new boolean[allocatedSize];

        if (localSize > allocatedSize) {
            localSize = allocatedSize;
        }

        for (int i = 0; i < localSize; i++) {
            newData[i] = data[i];
        }

        for (int i = 0; i < allocatedSize; i++) {
            newCheckList[i] = checkList[i];
        }

        data = newData;
        checkList = newCheckList;
    }

    public void resize(int newSize) {
        if (newSize < 0) {
            localSize = 0;
        } else if (newSize > allocatedSize) {
            localSize = allocatedSize;
        } else {
            localSize = newSize;
        }
    }

    public void remove(int index) {
        for (int i = index + 1; i < localSize; i++) {
            data[i - 1] = data[i];
        }

        localSize--;
    }

    public void clear() {
        localSize = 0;
    }

    public void uncheck(int index) {
        if (index >= 0 && index < data.length) {
            checkList[index] = false;
        }
    }

    public void recheck(int index) {
        if (index >= 0 && index < data.length) {
            checkList[index] = true;
        }
    }

    public void uncheckAll() {
        for (int i = 0; i < localSize; i++) {
            checkList[i] = false;
        }
    }

    public void recheckAll() {
        for (int i = 0; i < localSize; i++) {
            checkList[i] = true;
        }
    }

    public boolean contains(T value) {
        for (int i = 0; i < localSize; i++) {
            if (checkList[i] == true && data[i].equals(value)) {
                return true;
            }
        }

        return false;
    }

    public int getSize() {
        return localSize;
    }

    public int getAllocatedSize() {
        return allocatedSize;
    }

    public Object[] getData() {
        return data;
    }
}
