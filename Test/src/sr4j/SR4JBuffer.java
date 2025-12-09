package sr4j;

public class SR4JBuffer {
    public static final int BUFFER_TYPE_VERTEX = 0;
    public static final int BUFFER_TYPE_INDEX = 1;

    protected Object[] data;
    protected long size;
    protected long stride;
    protected long offset;
    protected int type;

    public SR4JBuffer(Object[] data, long size, long stride, long offset, int type) {
        this.data = data.clone();
        this.size = size;
        this.stride = stride;
        this.offset = offset;
        this.type = type;
    }

    public void setData(Object[] data) {
        this.data = data.clone();
    }

    public Object[] getData() {
        return data;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public void setStride(long stride) {
        this.stride = stride;
    }

    public long getStride() {
        return stride;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getOffset() {
        return offset;
    }

    public int getType() {
        return type;
    }
}
