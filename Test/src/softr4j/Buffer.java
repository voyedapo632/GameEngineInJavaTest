package softr4j;

public class Buffer {
    public BindFlag bindFlag;
    public Object[] data;
    public int size;
    public int offset;
    public int stride;

    public Buffer(BindFlag bindFlag, Object[] data, int size, int offset, int stride) {
        this.bindFlag = bindFlag;
        this.data = data;
        this.size = offset;
        this.offset = offset;
        this.stride = stride;
    }
}
