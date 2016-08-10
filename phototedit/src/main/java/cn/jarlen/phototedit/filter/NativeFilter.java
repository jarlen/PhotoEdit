package cn.jarlen.phototedit.filter;

/**
 * DESCRIBE:
 * Created by hjl on 2016/8/10.
 */
public class NativeFilter {
    static {
        System.loadLibrary("NativeFilter");
    }

    public native String test();
}
