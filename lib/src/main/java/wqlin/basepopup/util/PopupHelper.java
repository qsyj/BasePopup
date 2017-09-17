package wqlin.basepopup.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.ViewGroup;

import wqlin.basepopup.entity.LocationType;

/**
 * Created by wqlin on 2017/9/17.
 */

public class PopupHelper {
    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        Rect rectangle= new Rect();
        decorView.getWindowVisibleDisplayFrame(rectangle);
        int rectangleTop = rectangle.top;
        /*int displayMetricsH = getContext().getResources().getDisplayMetrics().heightPixels;
        ViewGroup decorView = (ViewGroup) getContext().getWindow().getDecorView();
        int decorViewH = decorView.getHeight();
        int decorViewN = decorView.getChildCount();
        Rect rectangle= new Rect();
        decorView.getWindowVisibleDisplayFrame(rectangle);
        int rectangleTop = rectangle.top;
        for (int i = 0; i < decorViewN; i++) {
            View v=decorView.getChildAt(i);
            Rect cR = new Rect();
            int[] chLocation = new int[2];
            v.getLocationOnScreen(chLocation);
            v.getWindowVisibleDisplayFrame(cR);
            int cRTop = cR.top;
            int ch=v.getHeight();
        }

        ViewGroup contentView= (ViewGroup) mContext.findViewById(android.R.id.content);
        int[] contentViewLocation = new int[2];
        contentView.getLocationOnScreen(contentViewLocation);
        int contentViewH=contentView.getHeight();
        int contentN = contentView.getChildCount();

        for (int i = 0; i < contentN; i++) {
            View v=contentView.getChildAt(i);
            int[] chLocation = new int[2];
            v.getLocationOnScreen(chLocation);
            int ch=v.getHeight();
        }*/
        return rectangleTop;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 当不是CENTER_CENTER TOP_CENTER_BOTTOM_CENTER  BOTTOM_CENTER_TOP_CENTER 并妥协尺寸时 <p>
     * 修正location[]
     * @param location
     * @param x
     * @param y
     * @param screenWidth
     * @param screenHeight
     * @return
     */
    private static int[] modifyLoaction(int[] location, int x, int y, int screenWidth, int screenHeight) {
        if (x < 0)
            x = 0;
        if (x > screenWidth)
            x = screenWidth;
        if (y < 0)
            y = 0;
        if (y > screenHeight)
            y = screenHeight;
        location[0] = x;
        location[1] = y;
        return location;
    }
    /**
     * 修正location[]
     * @param location
     * @param x
     * @param y
     * @param width
     * @param height
     * @param screenWidth
     * @param screenHeight
     * @return
     */
    public static  int[] modifyLoaction(int[] location,int locationType ,int x, int y,int width,int height, int screenWidth, int screenHeight,boolean isCompromiseSizeWhenCenter) {
        if ((locationType== LocationType.CENTER_CENTER||
                locationType== LocationType.BOTTOM_CENTER_TOP_CENTER||
                locationType== LocationType.TOP_CENTER_BOTTOM_CENTER)&&
                !isCompromiseSizeWhenCenter) {
            //当CENTER_CENTER TOP_CENTER_BOTTOM_CENTER  BOTTOM_CENTER_TOP_CENTER 不妥协尺寸时 修正location[]
            int leftX = x - width / 2;
            if (leftX < 0) {
                x = 0;
            }
            int rightX = leftX + width;
            if (rightX > screenWidth) {
                x = x - rightX + screenWidth;
            }
            switch (locationType) {
                case LocationType.CENTER_CENTER:
                    int topY = y - height / 2;
                    if (topY < 0) {
                        y = 0;
                    }
                    int bottomY = topY + height;
                    if (bottomY > screenHeight) {
                        y = y - bottomY + screenHeight;
                    }
                    break;
            }
            location[0] = x;
            location[1] = y;
        } else {
            location = modifyLoaction(location, x, y, screenWidth, screenHeight);
        }

        return location;
    }
    public static int modifyCenterX(int anchorCenterX, int screenWidth) {
        if (anchorCenterX < 0)
            return 0;
        if (anchorCenterX > screenWidth)
            return screenWidth;
        return anchorCenterX;

    }

    public static int modifyCenterY(int anchorCenterY, int screenHeight) {
        if (anchorCenterY < 0)
            return 0;
        if (anchorCenterY > screenHeight)
            return screenHeight;
        return anchorCenterY;
    }

    public static int modifyWidth(int width, int maxWidth, int screenWidth) {
        int max = maxWidth > screenWidth ? screenWidth : maxWidth;
        width = width > max ? max : width;
        return width;
    }

    public static int modifyHeight(int height, int maxHeight, int screenHeight) {
        int max = maxHeight > screenHeight ? screenHeight : maxHeight;
        height = height > max ? max : height;
        return height;
    }
}
