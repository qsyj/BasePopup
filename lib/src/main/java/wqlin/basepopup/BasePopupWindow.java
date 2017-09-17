/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 razerdp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package wqlin.basepopup;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import wqlin.basepopup.callback.LocationSizeIntercept;
import wqlin.basepopup.callback.OnDismissListener;
import wqlin.basepopup.callback.OnMeasureListener;
import wqlin.basepopup.entity.LocationConsumer;
import wqlin.basepopup.entity.LocationSizeInfo;
import wqlin.basepopup.entity.LocationType;
import wqlin.basepopup.entity.PopupConfig;
import wqlin.basepopup.util.PopupHelper;
import wqlin.library.R;
import wqlin.basepopup.util.InputMethodUtils;
import wqlin.basepopup.util.SimpleAnimUtil;

/**
 * Created by wqlin on 2017/9/14.
 * PopupViews 根元素设置尺寸是无效  始终是MATCH_PARENT
 * <p>
 * 必须在show前设置尺寸(默认是WRAP_CONTENT),构造函数和setSize()中设置
 * <p>
 * 抽象通用popupwindow的父类
 */
public abstract class BasePopupWindow implements BasePopup, PopupWindow.OnDismissListener, PopupController {
    private static final String TAG = "BaseOldPopupWindow";
    private static boolean isDebug = true;
    //元素定义
    private PopupWindowProxy mPopupWindow;
    //popup视图
    private View mPopupView;
    private Activity mContext;
    protected View mAnimaView;
    protected View mDismissView;
    //anima
    private Animation mShowAnimation;
    private Animator mShowAnimator;
    private Animation mExitAnimation;
    private Animator mExitAnimator;

    private boolean isExitAnimaPlaying = false;

    private int popupViewWidth;
    private int popupViewHeight;
    private final PopupHandler mHandler;

    private final PopupConfig mPopupConfig;

    /**
     * 动画结束 消息类型
     */
    private static final int MESSAGE_WHAT_ANIM_END = 1;

    public BasePopupWindow(Activity context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public BasePopupWindow(Activity context, int width, int height) {
        mPopupConfig = new PopupConfig();
        mHandler = new PopupHandler(this);
        initView(context, width, height);
    }
    private void initView(Activity context,int width,int height) {
        mContext = context;
        setSize(width,height);
        mPopupView = onCreatePopupView();
        mAnimaView = initAnimaView();

        setDismissWhenTouchOuside(true);

        //=============================================================为外层的view添加点击事件，并设置点击消失
        mDismissView = getClickToDismissView();

        if (mDismissView != null && !(mDismissView instanceof AdapterView)) {
            mDismissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (mAnimaView != null && !(mAnimaView instanceof AdapterView)) {
            mAnimaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        //初始化动画
        mShowAnimation = initShowAnimation();
        mShowAnimator = initShowAnimator();
        mExitAnimation = initExitAnimation();
        mExitAnimator = initExitAnimator();

    }


//------------------------------------------抽象 以及尽量重写的方法-----------------------------------------------

    /**
     * PopupWindow展示出来后，需要执行动画的View.一般为蒙层之上的View
     */
    protected abstract Animation initShowAnimation();

    /**
     * 设置一个点击后触发dismiss PopupWindow的View，一般为蒙层
     */
    public abstract View getClickToDismissView();

    /**
     * 设置展示动画View的属性动画
     */
    protected Animator initShowAnimator() {
        return null;
    }

    /**
     * 设置一个拥有输入功能的View，一般为EditTextView
     */
    public EditText getInputView() {
        return null;
    }

    /**
     * 设置PopupWindow销毁时的退出动画
     */
    protected Animation initExitAnimation() {
        return null;
    }

    /**
     * 设置PopupWindow销毁时的退出属性动画
     */
    protected Animator initExitAnimator() {
        return null;
    }

//------------------------------------------抽象 以及尽量重写的方法  end---------------------------------------------------


//------------------------------------------showPopup 测量 初始化 显示位置等-----------------------------------------------

    /**
     * PopupWindow显示  <p>
     * 是否自动适配popup的位置 不会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER  CENTER_CENTER
     */
    public void showAutoLocateNotCenter(View anchorView) {
        showAutoLocateNotCenter(anchorView, 0, 0);
    }
    /**
     * PopupWindow显示  <p>
     * 是否自动适配popup的位置 不会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER  CENTER_CENTER
     */
    public void showAutoLocateNotCenter(View anchorView, int xOff, int yOff) {
        setAutoLocatePopupCenter(false);
        setAutoLocatePopupNotCenter(true);
        setOffsetX(xOff);
        setOffsetY(yOff);
        tryToShowPopup(anchorView);
    }
    /**
     * PopupWindow显示  <p>
     * 是否自动适配popup的位置 不会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER  CENTER_CENTER
     */
    public void showAutoLocateNotCenter(int x, int y) {
        showAutoLocateNotCenter(x, y, 0, 0);
    }
    /**
     * PopupWindow显示  <p>
     * 是否自动适配popup的位置 不会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER  CENTER_CENTER
     */
    public void showAutoLocateNotCenter(int x, int y, int xOff, int yOff) {
        setAutoLocatePopupCenter(false);
        setAutoLocatePopupNotCenter(true);
        setLocationX(x);
        setLocationY(y);
        setOffsetX(xOff);
        setOffsetY(yOff);
        tryToShowPopup(null);
    }

    /**
     * PopupWindow显示  <p>
     * 自动适配popup的位置 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     * @param anchorView
     */
    public void showAutoLocateCenter(View anchorView) {
        showAutoLocateCenter(anchorView, 0, 0);
    }
    /**
     * PopupWindow显示  <p>
     * 自动适配popup的位置 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     * @param anchorView
     */
    public void showAutoLocateCenter(View anchorView, int xOff, int yOff) {
        setAutoLocatePopupCenter(true);
        setAutoLocatePopupNotCenter(false);
        setOffsetX(xOff);
        setOffsetY(yOff);
        tryToShowPopup(anchorView);
    }
    /**
     * PopupWindow显示  <p>
     * 自动适配popup的位置 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     */
    public void showAutoLocateCenter(int x, int y) {
        showAutoLocateCenter(x, y, 0, 0);
    }
    /**
     * PopupWindow显示  <p>
     * 自动适配popup的位置 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     */
    public void showAutoLocateCenter(int x, int y, int xOff, int yOff) {
        setAutoLocatePopupCenter(true);
        setAutoLocatePopupNotCenter(false);
        setLocationX(x);
        setLocationY(y);
        setOffsetX(xOff);
        setOffsetY(yOff);
        tryToShowPopup(null);
    }
    /**
     * PopupWindow显示
     */
    public void show(View anchorView, int loactionType) {
        show(anchorView,loactionType,0,0);
    }

    /**
     * PopupWindow显示
     */
    public void show(View anchorView, int loactionType, int xOff, int yOff) {
        setPopupLocationType(loactionType);
        setOffsetX(xOff);
        setOffsetY(yOff);
        tryToShowPopup(anchorView);
    }
    /**
     * 显示前的准备工作 <p>
     * 1.测量 <p>
     * 2.根据AnchorView 与Popup尺寸 获取位置
     */
    private void preShow() {
        if (mPopupView == null)
            return;
        int[] location = getShowLocation();

        mPopupWindow = new PopupWindowProxy(mPopupView, getWidth(), getHeight(), this);

        //===============================================动画================================================
        if (mPopupConfig.getAnimaStyleRes() > 0) {
            try {
                mPopupWindow.setAnimationStyle(R.style.PopupAnimaFade);
                mPopupWindow.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mPopupConfig.getOnMeasureListener() != null) {
            mPopupConfig.getOnMeasureListener().onMeasureComplete();
        }
        //===============================================动画  end================================================


        //========================点击外部是否消失================================================================
        if (mPopupConfig.isDismissWhenTouchOuside()) {
            //指定透明背景，back键相关
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        } else {
            mPopupWindow.setFocusable(false);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
        }
    //========================点击外部是否消失  end================================================================


    //========================点击返回键是否可以取消掉PopupWindow=============================================
        if (mPopupConfig.isBackPressEnable()) {
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        } else {
            mPopupWindow.setBackgroundDrawable(null);
        }

        //全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(mPopupWindow, mPopupConfig.isNeedFullScreen());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //========================点击返回键是否可以取消掉PopupWindow  end=============================================

        //========================键盘===============================================================================
        if (mPopupConfig.isNeedAdjust()) {
            mPopupWindow.setSoftInputMode(mPopupConfig.getSoftInputModeFlag());
        } else {
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
        //========================键盘end===============================================================================

        show(location);
    }

    private void show(int[] location) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(mPopupConfig.getAnchor(), Gravity.START | Gravity.TOP, location[0], location[1]);
        }
    }

    /**
     * 显示Popup前计算尺寸
     *
     * @return
     */
    private int[] preMeasurePopupView() {
        ViewGroup.LayoutParams popupViewP = mPopupView.getLayoutParams();

        FrameLayout frameLayout = null;
        if (popupViewP == null) {
            frameLayout = new FrameLayout(mContext);
            frameLayout.addView(mPopupView);
            popupViewP = mPopupView.getLayoutParams();
        }
        int width = mPopupConfig.getWidth();
        int height = mPopupConfig.getHeght();

        if (frameLayout != null) {
            frameLayout.removeView(mPopupView);
        }

        int screenWidth = PopupHelper.getScreenWidth(getContext());
        int screenHeight = PopupHelper.getScreenHeight(getContext());
        int realWidth = 0;
        int realHeight = 0;
        boolean isMeasure = false;
        if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
            realWidth = screenWidth;
        } else if (width >= 0) {
            realWidth = width;
        } else {
            isMeasure = true;
        }
        if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
            realHeight = screenHeight;
        } else if (height >= 0) {
            realHeight = height;
        } else {
            isMeasure = true;
        }
        if (isMeasure) {
            mPopupView.measure(View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(screenHeight, View.MeasureSpec.AT_MOST));
            realWidth = mPopupView.getMeasuredWidth();
            realHeight = mPopupView.getMeasuredHeight();
        }
        int[] size = new int[2];
        size[0] = realWidth;
        size[1] = realHeight;
        return size;
    }

    /**
     * 计算Popup位置  并修正width  height <p>
     * 向上充满屏幕时注意状态栏高度
     * @return
     */
    private int[] getShowLocation() {
        int[] size = preMeasurePopupView();
        int width = size[0];
        int height = size[1];
        int xOff = mPopupConfig.getXoff();
        int yOff = mPopupConfig.getYoff();
        int[] location = new int[2];
        int[] anchorLocation = new int[2];
        int statusHeight = PopupHelper.getStatusHeight(getContext());
        int screenWidth = PopupHelper.getScreenWidth(getContext());
        int screenHeight = PopupHelper.getScreenHeight(getContext())-statusHeight;

        int anchorWidth;
        int anchorHeight;
        boolean isAutoLocatePopup=mPopupConfig.isAutoLocatePopup();
        if (mPopupConfig.getAnchor() == null) {
            mPopupConfig.setAnchor(mContext.findViewById(android.R.id.content));
            if (isAutoLocatePopup) {
                setLocationTypeAutoLocate(null, screenWidth, screenHeight,statusHeight);
                anchorWidth = 0;
                anchorHeight = 0;
                anchorLocation[0] = mPopupConfig.getLocationX();
                anchorLocation[0] = anchorLocation[0] < 0 ? 0 : anchorLocation[0];
                anchorLocation[0] = anchorLocation[0] > screenWidth ? screenWidth : anchorLocation[0];
                anchorLocation[1] = modifyY(mPopupConfig.getLocationY(),statusHeight);
                anchorLocation[1] = anchorLocation[1] < 0 ? 0 : anchorLocation[1];
                anchorLocation[1]=anchorLocation[1] > screenHeight ? screenHeight : anchorLocation[1];
            } else {
                int x = mPopupConfig.getLocationX();
                int y = mPopupConfig.getLocationY();
                if ( x>= 0 && y >= 0) {
                    anchorWidth = 0;
                    anchorHeight = 0;
                    anchorLocation[0] = mPopupConfig.getLocationX();
                    anchorLocation[0] = anchorLocation[0] > screenWidth ? screenWidth : anchorLocation[0];
                    anchorLocation[1] = modifyY(mPopupConfig.getLocationY(),statusHeight);
                    anchorLocation[1]=anchorLocation[1] > screenHeight ? screenHeight : anchorLocation[1];
                } else {
                    anchorWidth = screenWidth;
                    anchorHeight = screenHeight;
                    anchorLocation[0] = 0;
                    anchorLocation[1] = 0;
                }

            }
        } else {
            View anchorView=mPopupConfig.getAnchor();
            if (isAutoLocatePopup) {
                setLocationTypeAutoLocate(anchorView, screenWidth, screenHeight,statusHeight);
            }
            anchorWidth= anchorView.getWidth();
            anchorHeight = anchorView.getHeight();
            mPopupConfig.getAnchor().getLocationOnScreen(anchorLocation);
            anchorLocation[1] = modifyY(anchorLocation[1], statusHeight);
        }

        int locationType = mPopupConfig.getLocationType();

        int anchorCenterX = 0;
        int anchorCenterY = 0;
        switch (locationType) {
            case LocationType.LEFT_TOP_LEFT_BOTTOM:
                width = PopupHelper.modifyWidth(width, screenWidth - anchorLocation[0] + xOff, screenWidth);
                height = PopupHelper.modifyHeight(height, anchorLocation[1] + yOff, screenHeight);
                location = PopupHelper.modifyLoaction(location, locationType,anchorLocation[0] + xOff,
                        anchorLocation[1] - height + yOff, width,height,screenWidth, screenHeight,mPopupConfig.isCompromiseSizeWhenCenter());
                break;
            case LocationType.RIGHT_TOP_RIGHT_BOTTOM:
                width = PopupHelper.modifyWidth(width, anchorLocation[0] + anchorWidth + xOff, screenWidth);
                height = PopupHelper.modifyHeight(height, anchorLocation[1] + yOff, screenHeight);
                location = PopupHelper.modifyLoaction(location, locationType,anchorLocation[0] + anchorWidth - width + xOff,
                        anchorLocation[1] - height + yOff, width,height,screenWidth, screenHeight,mPopupConfig.isCompromiseSizeWhenCenter());
                break;
            case LocationType.TOP_CENTER_BOTTOM_CENTER:
                anchorCenterX = PopupHelper.modifyCenterX(anchorLocation[0] + anchorWidth / 2 + xOff, screenWidth);
                if (mPopupConfig.isCompromiseSizeWhenCenter()) {
                    if (anchorCenterX == 0 || anchorCenterX == screenWidth||anchorCenterX==screenWidth/2) {
                        width = PopupHelper.modifyWidth(width, screenWidth, screenWidth);
                    } else {
                        if (anchorCenterX > screenWidth / 2) {
                            width = PopupHelper.modifyWidth(width, (screenWidth - anchorCenterX)*2, screenWidth);
                        } else {
                            width = PopupHelper.modifyWidth(width, anchorCenterX*2, screenWidth);
                        }
                    }
                }

                height = PopupHelper.modifyHeight(height, anchorLocation[1] + yOff, screenHeight);
                location = PopupHelper.modifyLoaction(location, locationType, anchorCenterX - width / 2,
                        anchorLocation[1] - height + yOff, width, height, screenWidth, screenHeight,mPopupConfig.isCompromiseSizeWhenCenter());

                break;
            case LocationType.LEFT_BOTTOM_LEFT_TOP:
                width = PopupHelper.modifyWidth(width, screenWidth - anchorLocation[0] + xOff, screenWidth);
                height = PopupHelper.modifyHeight(height, screenHeight - anchorLocation[1] - anchorHeight + yOff, screenHeight);
                location = PopupHelper.modifyLoaction(location, locationType,anchorLocation[0] + xOff,
                        anchorLocation[1] + anchorHeight + yOff,width,height, screenWidth, screenHeight,mPopupConfig.isCompromiseSizeWhenCenter());
                break;
            case LocationType.RIGHT_BOTTOM_RIGHT_TOP:
                width = PopupHelper.modifyWidth(width, anchorLocation[0] + anchorWidth + xOff, screenWidth);
                height = PopupHelper.modifyHeight(height, screenHeight - anchorLocation[1] - anchorHeight + yOff, screenHeight);
                location = PopupHelper.modifyLoaction(location, locationType,anchorLocation[0] + anchorWidth - width + xOff,
                        anchorLocation[1] + anchorHeight + yOff, width,height,screenWidth, screenHeight,mPopupConfig.isCompromiseSizeWhenCenter());
                break;
            case LocationType.BOTTOM_CENTER_TOP_CENTER:
                anchorCenterX = PopupHelper.modifyCenterX(anchorLocation[0] + anchorWidth / 2 + xOff, screenWidth);
                if (mPopupConfig.isCompromiseSizeWhenCenter()) {
                    if (anchorCenterX == 0 || anchorCenterX == screenWidth||anchorCenterX==screenWidth/2) {
                        width = PopupHelper.modifyWidth(width, screenWidth, screenWidth);
                    } else {
                        if (anchorCenterX > screenWidth / 2) {
                            width = PopupHelper.modifyWidth(width, (screenWidth - anchorCenterX)*2, screenWidth);
                        } else {
                            width = PopupHelper.modifyWidth(width, anchorCenterX*2, screenWidth);
                        }
                    }
                }
                height = PopupHelper.modifyHeight(height, screenHeight - anchorLocation[1] - anchorHeight + yOff, screenHeight);
                location = PopupHelper.modifyLoaction(location,locationType, anchorCenterX - width / 2,
                        anchorLocation[1] + anchorHeight + yOff,width,height, screenWidth, screenHeight,mPopupConfig.isCompromiseSizeWhenCenter());
                break;
            case LocationType.CENTER_CENTER:
                anchorCenterX = PopupHelper.modifyCenterX(anchorLocation[0] + anchorWidth / 2 + xOff, screenWidth);
                anchorCenterY = PopupHelper.modifyCenterY(anchorLocation[1] + anchorHeight / 2 + yOff, screenHeight);
                if (mPopupConfig.isCompromiseSizeWhenCenter()) {
                    if (anchorCenterX == 0 || anchorCenterX == screenWidth || anchorCenterX == screenWidth / 2) {
                        width = PopupHelper.modifyWidth(width, screenWidth, screenWidth);
                    } else {
                        if (anchorCenterX > screenWidth / 2) {
                            width = PopupHelper.modifyWidth(width, (screenWidth - anchorCenterX)*2, screenWidth);
                        } else {
                            width = PopupHelper.modifyWidth(width, anchorCenterX*2, screenWidth);
                        }
                    }

                    if (anchorCenterY == 0 || anchorCenterY == screenHeight || anchorCenterY == screenHeight / 2) {
                        height = PopupHelper.modifyHeight(height, screenHeight, screenHeight);
                    } else {
                        if (anchorCenterY > screenHeight / 2) {
                            height = PopupHelper.modifyHeight(height, (screenHeight - anchorCenterY)*2, screenHeight);
                        } else {
                            height = PopupHelper.modifyHeight(height, anchorCenterY*2, screenHeight);
                        }
                    }
                }

                location = PopupHelper.modifyLoaction(location,locationType, anchorCenterX - width / 2,
                        anchorCenterY - height / 2,width,height, screenWidth, screenHeight,mPopupConfig.isCompromiseSizeWhenCenter());
                break;
        }

        /*====================================拦截器==========================================*/
        LocationSizeIntercept locationSizeIntercept = mPopupConfig.getLocationSizeIntercept();
        if (locationSizeIntercept != null) {
            LocationSizeInfo locationSizeInfo=locationSizeIntercept.getLocationSize(new LocationConsumer(
                    location, anchorLocation, locationType,width, height, screenWidth, screenHeight, anchorWidth,
                    anchorHeight,mPopupConfig.getLocationSizeInterceptMsg()));
            location = locationSizeInfo.getLocation();
            width = locationSizeInfo.getWidth();
            height = locationSizeInfo.getHeight();
            //清除拦截器
            clearLocationSizeIntercept();
        }
        /*====================================拦截器 end==========================================*/

        setWidth(width);
        setHeight(height);
        OnMeasureListener onDismissListener = mPopupConfig.getOnMeasureListener();
        if ( onDismissListener!= null) {
            onDismissListener.onMeasureComplete();
        }
        //还原到屏幕左上角(0,0)
        location[1] += statusHeight;
        return location;
    }

    /**
     * 自动适配popup的位置时  计算出locationType
     * @param anchorView
     * @return LocationType 左上角位置
     */
    private void setLocationTypeAutoLocate(View anchorView, int screenWidth, int screenHeight,int statusHeight) {
        //0-->left 1-->right
        int xAnchorLocation=0;
        //0-->top 1-->bottom
        int yAnchorLocation = 0;
        int locationType = LocationType.LEFT_BOTTOM_LEFT_TOP;
        if (anchorView != null) {
            int[] anchorLocation = new int[2];
            anchorView.getLocationOnScreen(anchorLocation);
            anchorLocation[1] = modifyY(anchorLocation[1], statusHeight);

            int yTop = anchorLocation[1];
            yTop = yTop < 0 ? 0 : yTop;
            yTop = yTop > screenHeight ? screenHeight : yTop;
            int yBottom = anchorLocation[1] + anchorView.getHeight();
            yBottom = yBottom > screenHeight ? screenHeight : yBottom;
            yBottom = yBottom < 0 ? 0 : yBottom;
            int dBottomY = screenHeight - yBottom;

            if (mPopupConfig.isAutoLocatePopupCenter()) {
                if (yTop >= dBottomY) {
                    locationType = LocationType.TOP_CENTER_BOTTOM_CENTER;
                } else {
                    locationType = LocationType.BOTTOM_CENTER_TOP_CENTER;
                }
            } else {
                int xLeft = anchorLocation[0];
                xLeft = xLeft < 0 ? 0 : xLeft;
                xLeft = xLeft > screenWidth ? screenWidth : xLeft;
                int xRight = anchorLocation[0] + anchorView.getWidth();
                xRight = xRight > screenWidth ? screenWidth : xRight;
                xRight = xRight < 0 ? 0 : xRight;

                int dRightX = screenWidth - xRight;

                if (xLeft <= dRightX) {
                    xAnchorLocation = 0;
                } else {
                    xAnchorLocation = 1;
                }
                if (yTop >= dBottomY) {
                    yAnchorLocation = 0;
                } else {
                    yAnchorLocation = 1;
                }
                if (xAnchorLocation == 0 && yAnchorLocation == 0) {
                    locationType = LocationType.LEFT_TOP_LEFT_BOTTOM;
                } else if (xAnchorLocation == 1 && yAnchorLocation == 0) {
                    locationType = LocationType.RIGHT_TOP_RIGHT_BOTTOM;
                } else if (xAnchorLocation == 0 && yAnchorLocation == 1) {
                    locationType = LocationType.LEFT_BOTTOM_LEFT_TOP;
                } else if (xAnchorLocation == 1 && yAnchorLocation == 1) {
                    locationType = LocationType.RIGHT_BOTTOM_RIGHT_TOP;
                }
            }

        } else {
            int y = mPopupConfig.getLocationY();
            y = modifyY(y, statusHeight);
            y = y < 0 ? 0 : y;
            y = y > screenHeight ? screenHeight : y;
            int halfScreenHeight = screenHeight / 2;
            if (mPopupConfig.isAutoLocatePopupCenter()) {
                if (y >= halfScreenHeight) {
                    locationType = LocationType.TOP_CENTER_BOTTOM_CENTER;
                } else {
                    locationType = LocationType.BOTTOM_CENTER_TOP_CENTER;
                }
            } else {
                int x = mPopupConfig.getLocationX();
                x = x < 0 ? 0 : x;
                x = x > screenWidth ? screenWidth : x;

                int halfScreenWidth = screenWidth / 2;

                if (x <= halfScreenWidth && y <= halfScreenHeight) {
                    locationType = LocationType.LEFT_BOTTOM_LEFT_TOP;
                } else if (x <= halfScreenWidth && y >= halfScreenHeight) {
                    locationType = LocationType.LEFT_TOP_LEFT_BOTTOM;
                } else if (x >= halfScreenWidth && y <= halfScreenHeight) {
                    locationType = LocationType.RIGHT_BOTTOM_RIGHT_TOP;
                } else if (x >= halfScreenWidth && y >= halfScreenHeight) {
                    locationType = LocationType.RIGHT_TOP_RIGHT_BOTTOM;
                }
            }
        }
        setPopupLocationType(locationType);

    }

    /**
     * 修正Y坐标  将屏幕左上角为(0,0)改为 状态栏左下方为(0,0)
     * @param y
     * @return
     */
    private int modifyY(int y,int statusH) {
        return y-statusH;
    }


    private void tryToShowPopup(View v) {
        try {
            mPopupConfig.setAnchor(v);
            preShow();

            if (mShowAnimation != null && mAnimaView != null) {
                mAnimaView.clearAnimation();
                mAnimaView.startAnimation(mShowAnimation);
            }
            if (mShowAnimation == null && mShowAnimator != null && mAnimaView != null) {
                mShowAnimator.start();
            }
            //自动弹出键盘
            if (mPopupConfig.isAutoShowInputMethod() && getInputView() != null) {
                getInputView().requestFocus();
                InputMethodUtils.showInputMethod(getInputView(), 150);
            }
        } catch (Exception e) {
            Log.e(TAG, "show error");
            e.printStackTrace();
        }
    }
//------------------------------------------showPopup 测量 初始化 显示位置等 end-----------------------------------------------


    //------------------------------------------Getter/Setter--------------------------------------------------------------

    /**
     * PopupViews 根元素设置尺寸是无效  始终是MATCH_PARENT
     * <p>
     * 必须在show前设置尺寸(默认是WRAP_CONTENT),构造函数和setSize()中设置
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mPopupConfig.setWidth(width);
        mPopupConfig.setHeght(height);
    }

    public void setAdjustInputMethod(boolean needAdjust, int flag) {
        mPopupConfig.setNeedAdjust(needAdjust);
        mPopupConfig.setSoftInputModeFlag(flag);

    }

    /**
     * 当PopupWindow展示的时候，这个参数决定了是否自动弹出输入法
     * 如果使用这个方法，您必须保证通过 <strong>getInputView()<strong/>得到一个EditTextView
     */
    public void setAutoShowInputMethod(boolean autoShow) {
        mPopupConfig.setAutoShowInputMethod(autoShow);
    }

    /**
     * 这个参数决定点击返回键是否可以取消掉PopupWindow
     */
    public void setBackPressEnable(boolean backPressEnable) {
        mPopupConfig.setBackPressEnable(backPressEnable);
    }


    /**
     * 是否允许popupwindow覆盖屏幕（包含状态栏）
     */
    public void setPopupWindowFullScreen(boolean needFullScreen) {
        fitPopupWindowOverStatusBar(needFullScreen);
    }

    /**
     * 这个方法用于简化您为View设置OnClickListener事件，多个View将会使用同一个点击事件
     */
    protected void setViewClickListener(View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null && listener != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    private void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        mPopupConfig.setNeedFullScreen(needFullScreen);
    }



    /**
     * PopupWindow是否处于展示状态
     */
    public boolean isShowing() {
        if (mPopupWindow == null)
            return false;
        return mPopupWindow.isShowing();
    }

    public void setShowAnimation(Animation showAnimation) {
        if (mShowAnimation != null && mAnimaView != null) {
            mAnimaView.clearAnimation();
            mShowAnimation.cancel();
        }
        if (showAnimation != null && showAnimation != mShowAnimation) {
            mShowAnimation = showAnimation;
        }
    }

    public Animation getShowAnimation() {
        return mShowAnimation;
    }

    public void setShowAnimator(Animator showAnimator) {
        if (mShowAnimator != null) mShowAnimator.cancel();
        if (showAnimator != null && showAnimator != mShowAnimator) {
            mShowAnimator = showAnimator;
        }
    }

    public Animator getShowAnimator() {
        return mShowAnimator;
    }

    public void setExitAnimation(Animation exitAnimation) {
        if (mExitAnimation != null && mAnimaView != null) {
            mAnimaView.clearAnimation();
            mExitAnimation.cancel();
        }
        if (exitAnimation != null && exitAnimation != mExitAnimation) {
            mExitAnimation = exitAnimation;
        }
    }

    public Animation getExitAnimation() {
        return mExitAnimation;
    }

    public void setExitAnimator(Animator exitAnimator) {
        if (mExitAnimator != null) mExitAnimator.cancel();
        if (exitAnimator != null && exitAnimator != mExitAnimator) {
            mExitAnimator = exitAnimator;
        }
    }

    public Animator getExitAnimator() {
        return mExitAnimator;
    }

    public Activity getContext() {
        return mContext;
    }

    /**
     * 获取popupwindow的根布局
     *
     * @return
     */
    public View getPopupWindowView() {
        return mPopupView;
    }

    /**
     * 获取popupwindow实例
     *
     * @return
     */
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    /**
     * 在执行popupwindow dismiss()前调用 onBeforeDismiss() ;dismiss() 后调用onDismiss();
     * @param onDismissListener
     */
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mPopupConfig.setDismissListener(onDismissListener);
    }

    /**
     * 设定x位置的偏移量(中心点在popup的左上角)
     *
     * @param offsetX
     */
    public void setOffsetX(int offsetX) {
        mPopupConfig.setXoff(offsetX);
    }

    /**
     * 当CENTER_CENTER是否妥协宽 高 ;TOP_CENTER_BOTTOM_CENTER  BOTTOM_CENTER_TOP_CENTER是否妥协宽度
     * @param compromiseSizeWhenCenter
     */
    public void setCompromiseSizeWhenCenter(boolean compromiseSizeWhenCenter) {
        mPopupConfig.setCompromiseSizeWhenCenter(compromiseSizeWhenCenter);
    }

    /**
     * 设定y位置的偏移量(中心点在popup的左上角)
     *
     * @param offsetY
     */
    public void setOffsetY(int offsetY) {
        mPopupConfig.setYoff(offsetY);
    }

    /**
     * 设置参考点，一般情况下，参考对象指的不是指定的view，而是它的windoToken，可以看作为整个screen
     *
     * @param locationType
     */
    public void setPopupLocationType(int locationType) {
        mPopupConfig.setLocationType(locationType);
    }

    private void setLocationX(int locationX) {
        mPopupConfig.setLocationX(locationX);
    }

    private void setLocationY(int locationY) {
        mPopupConfig.setLocationY(locationY);
    }
    /**
     *   优先级小于isAutoLocatePopupCenter <p>
     *  是否自动适配popup的位置 不会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER  CENTER_CENTER
     * @param autoLocatePopup
     */
    private void setAutoLocatePopupNotCenter(boolean autoLocatePopup) {
        mPopupConfig.setAutoLocatePopupNotCenter(autoLocatePopup);
    }
    /**
     * 优先级大于isAutoLocatePopupNotCenter <p>
     * 自动适配popup的位置 true时 只会适配到TOP_CENTER_BOTTOM_CENTER   BOTTOM_CENTER_TOP_CENTER
     * @param autoLocatePopupCenter
     */
    private void setAutoLocatePopupCenter(boolean autoLocatePopupCenter) {
        mPopupConfig.setAutoLocatePopupCenter(autoLocatePopupCenter);
    }

    private void setWidth(int width) {
        this.popupViewWidth = width;
    }

    private void setHeight(int height) {
        this.popupViewHeight = height;
    }

    /**
     * 获取poupwindow的高度，当popupwindow没show出来的时候高度会是0，此时则返回pre measure的高度，不一定精准
     *
     * @return
     */
    public int getHeight() {

        return popupViewHeight;
    }

    /**
     * 获取poupwindow的宽度，当popupwindow没show出来的时候高度会是0，此时则返回pre measure的宽度，不一定精准
     *
     * @return
     */
    public int getWidth() {

        return popupViewWidth;
    }


    /**
     * 点击外部是否消失
     * <p>
     * dismiss popup when touch ouside from popup
     *
     * @param dismissWhenTouchOuside true for dismiss
     */
    public void setDismissWhenTouchOuside(boolean dismissWhenTouchOuside) {
        mPopupConfig.setDismissWhenTouchOuside(dismissWhenTouchOuside);
    }

    public boolean isDismissWhenTouchOuside() {
        return mPopupConfig.isDismissWhenTouchOuside();
    }

    /**
     * 当BasePopupWindow执行getShowLocation() 没有设置宽高和返回location时 会调用mLocationSizeIntercept getLocationSize() <p>
     * 从而拦截并去修改popupwindow的location width height等信息  <p>
     *  在每次在show前调用
     *  @param locationSizeIntercept
     * @param locationSizeInterceptMsg 当使用LocationSizeIntercept时,在show前调用,在回调中LocationConsumer参数 会带有Object locationSizeInterceptMsg,
     *                                 可以通过该参数判断是否要执行改变Location size <p>
     *                                 起到TAG的作用
     *
     */
    public void setLocationSizeIntercept(LocationSizeIntercept locationSizeIntercept,Object locationSizeInterceptMsg) {
        mPopupConfig.setLocationSizeIntercept(locationSizeIntercept);
        setLocationSizeInterceptMsg(locationSizeInterceptMsg);
    }

    /**
     *
     * @param locationSizeInterceptMsg
     */
    private void setLocationSizeInterceptMsg(Object locationSizeInterceptMsg) {
        mPopupConfig.setLocationSizeInterceptMsg(locationSizeInterceptMsg);
    }

    /**
     * 清除LocationSizeIntercept拦截器
     */
    private void clearLocationSizeIntercept() {
        mPopupConfig.setLocationSizeIntercept(null);
        mPopupConfig.setLocationSizeInterceptMsg(null);
    }
    /**
     * 测量Popup contentview 完后回调  获取width height在这个回调后获取
     * @param onMeasureListener
     */
    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        mPopupConfig.setOnMeasureListener(onMeasureListener);
    }

//------------------------------------------Getter/Setter  end-------------------------------------------------


//------------------------------------------状态控制------------------------------------------------------------

    /**
     * 取消一个PopupWindow，如果有退出动画，PopupWindow的消失将会在动画结束后执行
     */
    public void dismiss() {
        try {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        } catch (Exception e) {
            Log.e(TAG, "dismiss error");
        }
    }

    @Override
    public boolean onBeforeDismiss() {
        return checkPerformDismiss();
    }

    @Override
    public boolean callDismissAtOnce() {
        boolean hasAnima = false;
        log("callDismissAtOnce()", "mExitAnimation isNull:" + (mExitAnimation == null) +
                ",mAnimaView isNull:" + (mAnimaView == null) +
                ",isExitAnimaPlaying:" + isExitAnimaPlaying);
        if (mExitAnimation != null && mAnimaView != null) {
            if (!isExitAnimaPlaying) {
                mExitAnimation.setAnimationListener(mAnimationListener);
                mAnimaView.clearAnimation();
                mAnimaView.startAnimation(mExitAnimation);
                isExitAnimaPlaying = true;
                hasAnima = true;
            }
        } else if (mExitAnimator != null) {
            if (!isExitAnimaPlaying) {
                mExitAnimator.removeListener(mAnimatorListener);
                mExitAnimator.addListener(mAnimatorListener);
                mExitAnimator.start();
                isExitAnimaPlaying = true;
                hasAnima = true;
            }
        }
        //如果有动画，则不立刻执行dismiss
        return !hasAnima;
    }

    /**
     * 直接消掉popup而不需要动画
     */
    public void dismissWithOutAnima() {
        if (!checkPerformDismiss()) return;
        try {
            if (mExitAnimation != null && mAnimaView != null) mAnimaView.clearAnimation();
            if (mExitAnimator != null) mExitAnimator.removeAllListeners();
            mPopupWindow.callSuperDismiss();
        } catch (Exception e) {
            Log.e(TAG, "dismiss error");
        }
    }


    private boolean checkPerformDismiss() {
        boolean callDismiss = true;
        if (mPopupConfig.getDismissListener() != null) {
            callDismiss = mPopupConfig.getDismissListener().onBeforeDismiss();
        }
        log("checkPerformDismiss()", "isExitAnimaPlaying:" + isExitAnimaPlaying);
        return callDismiss && !isExitAnimaPlaying;
    }
//------------------------------------------状态控制 end-----------------------------------------------



//------------------------------------------Anima-----------------------------------------------

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isExitAnimaPlaying = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mPopupWindow.callSuperDismiss();
            isExitAnimaPlaying = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isExitAnimaPlaying = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isExitAnimaPlaying = true;
            log("mAnimationListener onAnimationStart","isExitAnimaPlaying:"+isExitAnimaPlaying);
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_ANIM_END, animation.getDuration());
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };


    /**
     * 生成默认的AlphaAnimation
     */
    protected Animation getDefaultAlphaAnimation() {
        return SimpleAnimUtil.getDefaultAlphaAnimation();
    }
/*================================================工具========================================================*/
    /**
     * 这个方法封装了LayoutInflater.from(context).inflate，方便您设置PopupWindow所用的xml
     *
     * @param resId reference of layout
     * @return root View of the layout
     */
    public View createPopupById(int resId) {
        if (resId != 0) {
            return LayoutInflater.from(mContext).inflate(resId, null);
        } else {
            return null;
        }
    }

    protected View findViewById(int id) {
        if (mPopupView != null && id != 0) {
            return mPopupView.findViewById(id);
        }
        return null;
    }

/*================================================工具  end========================================================*/


//------------------------------------------callback-----------------------------------------------

    @Override
    public void onDismiss() {
        //移除消息
        mHandler.removeMessages(MESSAGE_WHAT_ANIM_END);

        if (mPopupConfig.getDismissListener() != null) {
            mPopupConfig.getDismissListener().onDismiss();
        }
        isExitAnimaPlaying = false;
        log("mAnimationListener onAnimationStart","isExitAnimaPlaying:"+isExitAnimaPlaying);
    }

    /**
     * 处理退出动画消息  onAnimationEnd()有bug可能不执行
     */
    private static class PopupHandler extends Handler {
        private final WeakReference<BasePopupWindow> mBasePopupNewWindowWeakReference;
        public PopupHandler(BasePopupWindow popupNewWindow) {
            mBasePopupNewWindowWeakReference = new WeakReference<BasePopupWindow>(popupNewWindow);
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case MESSAGE_WHAT_ANIM_END:
                    log("PopupHandler handleMessage","onAnimationEnd");
                    if (mBasePopupNewWindowWeakReference.get() != null) {
                        BasePopupWindow window = mBasePopupNewWindowWeakReference.get();
                        if (window != null) {
                            if ( window.getPopupWindow()!= null) {
                                window.mPopupWindow.callSuperDismiss();
                            }
                            window.isExitAnimaPlaying = false;
                        }
                    }
                    break;
            }

        }
    }

    private static void log(String key, String value) {
        if (isDebug) {
            Log.e(key, value);
        }

    }
}
