package wqlin.basepopup.entity;

/**
 * Created by wqlin on 2017/9/17.
 */

public class LocationConsumer {
    private int[] mLocation;
    private int[] mAnchorLocation;
    private int mLocationType;
    private int mWidth;
    private int mHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mAnchorWidth;
    private int mAnchorHeight;
    private Object locationSizeInterceptMsg;

    public LocationConsumer(int[] location, int[] anchorLocation, int locationType, int width,
                            int height, int screenWidth, int screenHeight, int anchorWidth,
                            int anchorHeight, Object locationSizeInterceptMsg) {
        mLocation = location;
        mAnchorLocation = anchorLocation;
        mLocationType = locationType;
        mWidth = width;
        mHeight = height;
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mAnchorWidth = anchorWidth;
        mAnchorHeight = anchorHeight;
        this.locationSizeInterceptMsg = locationSizeInterceptMsg;
    }

    public int[] getLocation() {
        return mLocation;
    }

    public int getLocationType() {
        return mLocationType;
    }

    public int[] getAnchorLocation() {
        return mAnchorLocation;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getAnchorWidth() {
        return mAnchorWidth;
    }

    public int getAnchorHeight() {
        return mAnchorHeight;
    }

    public Object getLocationSizeInterceptMsg() {
        return locationSizeInterceptMsg;
    }
}
