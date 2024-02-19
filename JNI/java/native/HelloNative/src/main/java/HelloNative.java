
public class HelloNative{

    public static void main(String[] args) {
        greeting();
    }
    public static native void greeting();

    static {
        System.loadLibrary("HelloNative");
    }
}
