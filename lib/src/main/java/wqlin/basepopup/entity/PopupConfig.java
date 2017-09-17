package wqlin.basepopup.entity;

import android.view.View;
import android.view.ViewGroup;

import wqlin.basepopup.callback.LocationSizeIntercept;
import wqlin.basepopup.callback.OnDismissListener;
import wqlin.basepopup.callback.OnMeasureListener;

/**
 * Created by wqlin on 2017/9/16.
 */

public class PopupConfig {
    private int width= ViewGroup.LayoutParams.WRAP_CONTENT;
    private int heght=ViewGroup.LayoutParams.WRAP_CONTENT;
    private View mAnchor;
    private int locationX = -1;
    private int locationY = -1;
    private int mXoff;
    private int mYoff;
    private int mLocationType = LocationType.LEFT_BOTTOM_LEFT_TOP;
    /**
     * 当BasePopupWindow执行getShowLocation() 没有设置宽高和返回location时 会调用mLocationSizeIntercept getLocationSize() <p>
     * 从而拦截并去修改popupwindow的location width height等信息
     */
    private LocationSizeIntercept mLocationSizeIntercept;
    /**
     * 当使用LocationSizeIntercept时,在show前调用,在回调中LocationConsumer参数 会带有Object locationSizeInterceptMsg,可以通过该参数判断是否要执行改变Location size <p>
     * 起到TAG的作用
     */
    private Object mLocationSizeInterceptMsg;

    /**
     * 测量Popup contentview 后回调  获取width height在这个回调后获取
     */
    private OnMeasureListener mOnMeasureListener;

    /**
     * 当CENTER_CENTER是否妥协宽 高 ;TOP_CENTER_BOTTOM_CENTER  BOTTOM_CENTER_TOP_CENTER是否妥协宽度
     */
    private boolean isCompromiseSizeWhenCenter = false;

    /**
     * 在执行popupwindow dismiss()前调用
     */
    private OnDismissListener mDismissListener;

    private int mAnimaStyleRes;
    /**
     * PopupWindow是否需要自适应输入法，为输入法弹出让出区域
     */
    private boolean needAdjust;
    private int mSoftInputModeFlag;

    /**
     *     是否自动弹出输入框(default:false)
     */
    private boolean autoShowInputMethod = false;
    /**
     * 这个参数决定点击返回键是否可以取消掉PopupWindow
     */
    private boolean backPressEnable;
    /**
     * 是否允许popupwindow覆盖屏幕（包含状态栏）
     */
    private boolean needFullScreen;
    /**
     * 点击外部是否消失
     */
    private boolean dismissWhenTouchOuside;

    /**
     *  优先级小于isAutoLocatePopupCenter <p>
     *  是否自动适配popup的位置 不会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER  CENTER_CENTER
     */
    private boolean isAutoLocatePopupNotCenter;
    /**
     * 优先级大于isAutoLocatePopupNotCenter <p>
     * 自动适配popup的位置 true时 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     */
    private boolean isAutoLocatePopupCenter;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeght() {
        return heght;
    }

    public void setHeght(int heght) {
        this.heght = heght;
    }

    public View getAnchor() {
        return mAnchor;
    }

    public void setAnchor(View anchor) {
        mAnchor = anchor;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getXoff() {
        return mXoff;
    }

    public void setXoff(int xoff) {
        mXoff = xoff;
    }

    public int getYoff() {
        return mYoff;
    }

    public void setYoff(int yoff) {
        mYoff = yoff;
    }

    public int getLocationType() {
        return mLocationType;
    }

    public void setLocationType(int locationType) {
        mLocationType = locationType;
    }

    /**
     * 当CENTER_CENTER是否妥协宽 高 ;TOP_CENTER_BOTTOM_CENTER  BOTTOM_CENTER_TOP_CENTE是否妥协宽度
     */
    public boolean isCompromiseSizeWhenCenter() {
        return isCompromiseSizeWhenCenter;
    }

    public void setCompromiseSizeWhenCenter(boolean compromiseSizeWhenCenter) {
        isCompromiseSizeWhenCenter = compromiseSizeWhenCenter;
    }

    /**
     * 在执行popupwindow dismiss()前调用 onBeforeDismiss() ;dismiss() 后调用onDismiss();
     * @return
     */
    public OnDismissListener getDismissListener() {
        return mDismissListener;
    }

    /**
     * 在执行popupwindow dismiss()前调用 onBeforeDismiss() ;dismiss() 后调用onDismiss();
     * @param dismissListener
     */
    public void setDismissListener(OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    public int getAnimaStyleRes() {
        return mAnimaStyleRes;
    }

    public void setAnimaStyleRes(int animaStyleRes) {
        mAnimaStyleRes = animaStyleRes;
    }

    public boolean isNeedAdjust() {
        return needAdjust;
    }

    public void setNeedAdjust(boolean needAdjust) {
        this.needAdjust = needAdjust;
    }

    public int getSoftInputModeFlag() {
        return mSoftInputModeFlag;
    }

    public void setSoftInputModeFlag(int softInputModeFlag) {
        mSoftInputModeFlag = softInputModeFlag;
    }

    public boolean isBackPressEnable() {
        return backPressEnable;
    }

    public void setBackPressEnable(boolean backPressEnable) {
        this.backPressEnable = backPressEnable;
    }

    public boolean isNeedFullScreen() {
        return needFullScreen;
    }

    public void setNeedFullScreen(boolean needFullScreen) {
        this.needFullScreen = needFullScreen;
    }

    public boolean isAutoShowInputMethod() {
        return autoShowInputMethod;
    }

    public void setAutoShowInputMethod(boolean autoShowInputMethod) {
        this.autoShowInputMethod = autoShowInputMethod;
    }

    public boolean isDismissWhenTouchOuside() {
        return dismissWhenTouchOuside;
    }

    public void setDismissWhenTouchOuside(boolean dismissWhenTouchOuside) {
        this.dismissWhenTouchOuside = dismissWhenTouchOuside;
    }

    /**
     * 是否自动适配Popup位置
     * @return
     */
    public boolean isAutoLocatePopup() {
        return isAutoLocatePopupNotCenter || isAutoLocatePopupCenter;
    }
    public boolean isAutoLocatePopupNotCenter() {
        return isAutoLocatePopupNotCenter;
    }

    public void setAutoLocatePopupNotCenter(boolean autoLocatePopupNotCenter) {
        isAutoLocatePopupNotCenter = autoLocatePopupNotCenter;
    }

    /**
     * 优先级大于isAutoLocatePopup <p>
     * 自动适配popup的位置 true时 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     * @return
     */
    public boolean isAutoLocatePopupCenter() {
        return isAutoLocatePopupCenter;
    }

    /**
     * 优先级大于isAutoLocatePopup <p>
     * 自动适配popup的位置 true时 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     * @param autoLocatePopupCenter
     */
    public void setAutoLocatePopupCenter(boolean autoLocatePopupCenter) {
        isAutoLocatePopupCenter = autoLocatePopupCenter;
    }

    /**
     * 测量Popup contentview 后回调  获取width height在这个回调后获取
     * @return
     */
    public OnMeasureListener getOnMeasureListener() {
        return mOnMeasureListener;
    }

    /**
     * 测量Popup contentview 后回调  获取width height在这个回调后获取
     * @param onMeasureListener
     */
    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        mOnMeasureListener = onMeasureListener;
    }

    /**
     * 当BasePopupWindow执行getShowLocation() 没有设置宽高和返回location时 会调用mLocationSizeIntercept getLocationSize() <p>
     * 从而拦截并去修改popupwindow的location width height等信息
     */
    public LocationSizeIntercept getLocationSizeIntercept() {
        return mLocationSizeIntercept;
    }

    /**
     * 当BasePopupWindow执行getShowLocation() 没有设置宽高和返回location时 会调用mLocationSizeIntercept getLocationSize() <p>
     * 从而拦截并去修改popupwindow的location width height等信息
     */
    public void setLocationSizeIntercept(LocationSizeIntercept locationSizeIntercept) {
        mLocationSizeIntercept = locationSizeIntercept;
    }
    /**
     * 当使用LocationSizeIntercept时,在show前调用,在回调中LocationConsumer参数 会带有Object locationSizeInterceptMsg,可以通过该参数判断是否要执行改变Location size <p>
     * 起到TAG的作用
     */
    public Object getLocationSizeInterceptMsg() {
        return mLocationSizeInterceptMsg;
    }
    /**
     * 当使用LocationSizeIntercept时,在show前调用,在回调中LocationConsumer参数 会带有Object locationSizeInterceptMsg,可以通过该参数判断是否要执行改变Location size <p>
     * 起到TAG的作用
     */
    public void setLocationSizeInterceptMsg(Object locationSizeInterceptMsg) {
        mLocationSizeInterceptMsg = locationSizeInterceptMsg;
    }
}
