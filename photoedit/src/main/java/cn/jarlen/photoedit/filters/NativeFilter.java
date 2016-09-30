package cn.jarlen.photoedit.filters;

/**
 * DESCRIBE:
 * Created by hjl on 2016/9/28.
 */
public class NativeFilter {
    static {
        System.loadLibrary("nativefilter");
    }

    public native String test();
}
