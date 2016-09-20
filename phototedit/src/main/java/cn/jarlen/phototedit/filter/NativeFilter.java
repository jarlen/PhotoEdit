package cn.jarlen.phototedit.filter;

/**
 * DESCRIBE:
 * Created by hjl on 2016/8/10.
 */
public class NativeFilter {
    static {
        System.loadLibrary("NativeFilter");
    }

    public native int[] ToGray(int[] pixels, int width, int height, float factor);


    public native int[] ToMosatic(int[] pixels, int width, int height,int factor);

    public native int[] ToBrown(int[] pixels, int width, int height,float factor);

    public native String test();
}
