package wqlin.basepopup.entity;

/**
 * Created by wqlin on 2017/9/17.
 */

public class LocationSizeInfo {
    private int[] mLocation;
    private int mWidth;
    private int mHeight;

    public LocationSizeInfo(int[] location, int width, int height) {
        mLocation = location;
        mWidth = width;
        mHeight = height;
    }

    public int[] getLocation() {
        return mLocation;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
