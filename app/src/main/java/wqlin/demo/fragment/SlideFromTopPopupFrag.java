package wqlin.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import wqlin.basepopup.BaseOldPopupWindow;
import wqlin.basepopup.callback.LocationSizeIntercept;
import wqlin.basepopup.entity.LocationConsumer;
import wqlin.basepopup.entity.LocationSizeInfo;
import wqlin.basepopup.entity.LocationType;
import wqlin.basepopup.callback.OnDismissListener;
import wqlin.basepopup.R;
import wqlin.demo.popup.SlideFromTopNewPopup;

/**
 * Created by wqlin on 2016/1/15.
 *
 * 从顶部向下滑动的popup
 */
public class SlideFromTopPopupFrag extends SimpleBaseFrag {

    private SlideFromTopNewPopup mSlideFromTopPopup;
    private RelativeLayout titleParent;

    private ImageView arrow;


    private RotateAnimation showArrowAnima;
    private RotateAnimation dismissArrowAnima;
    private LinearLayout llTitle;
    private View tvTest1;
    private View tvTest2;
    private View tvTest3;
    private View tvTest4;
    private FromTopOnClickListener mFromTopOnClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        buildShowArrowAnima();
        buildDismissArrowAnima();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void buildShowArrowAnima() {
        if (showArrowAnima != null) return;
        showArrowAnima = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showArrowAnima.setDuration(450);
        showArrowAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        showArrowAnima.setFillAfter(true);
    }

    private void buildDismissArrowAnima() {
        if (dismissArrowAnima != null) return;
        dismissArrowAnima = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dismissArrowAnima.setDuration(450);
        dismissArrowAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        dismissArrowAnima.setFillAfter(true);
    }

    private void startShowArrowAnima() {
        if (arrow == null) return;
        arrow.clearAnimation();
        arrow.startAnimation(showArrowAnima);
    }

    private void startDismissArrowAnima() {
        if (arrow == null) return;
        arrow.clearAnimation();
        arrow.startAnimation(dismissArrowAnima);
    }


    @Override
    public void bindEvent() {
        mFromTopOnClickListener = new FromTopOnClickListener();
        titleParent = (RelativeLayout) findViewById(R.id.rl_title);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvTest1 = findViewById(R.id.tv_test1);
        tvTest2 = findViewById(R.id.tv_test2);
        tvTest3 = findViewById(R.id.tv_test3);
        tvTest4 = findViewById(R.id.tv_test4);
        mSlideFromTopPopup = new SlideFromTopNewPopup(mContext);
        mSlideFromTopPopup.setOnDismissListener(onDismissListener);
        mSlideFromTopPopup.setLocationSizeIntercept(new LocationSizeIntercept() {
            @Override
            public LocationSizeInfo getLocationSize(LocationConsumer consumer) {
                int[] location = consumer.getLocation();
//                location[0] -= 100;
                LocationSizeInfo locationSizeInfo=new LocationSizeInfo(location,
                        consumer.getWidth(), consumer.getHeight());
                return locationSizeInfo;
            }
        },null);
        arrow = (ImageView) findViewById(R.id.iv_arrow);

        findViewById(R.id.ll_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlideFromTopPopup.isShowing()) startShowArrowAnima();
//                mSlideFromTopPopup.show(null, LocationType.CENTER_CENTER,0,0);
//                mSlideFromTopPopup.show(titleParent, LocationType.RIGHT_BOTTOM_RIGHT_TOP,0,0);
//                mSlideFromTopPopup.show(llTitle, LocationType.RIGHT_BOTTOM_RIGHT_TOP,0,0);
                mSlideFromTopPopup.show(llTitle, LocationType.BOTTOM_CENTER_TOP_CENTER,0,0);
            }
        });
        /*tvTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!mSlideFromTopPopup.isShowing())
                    startShowArrowAnima();
//                    mSlideFromTopPopup.show(tvTest, LocationType.LEFT_TOP_LEFT_BOTTOM,0,0);
//                    mSlideFromTopPopup.show(tvTest, LocationType.RIGHT_TOP_RIGHT_BOTTOM,0,0);
//                    mSlideFromTopPopup.show(tvTest, LocationType.TOP_CENTER_BOTTOM_CENTER,0,0);
//                    mSlideFromTopPopup.show(tvTest, LocationType.LEFT_BOTTOM_LEFT_TOP,0,0);
//                    mSlideFromTopPopup.show(tvTest, LocationType.RIGHT_BOTTOM_RIGHT_TOP,0,0);
//                    mSlideFromTopPopup.show(tvTest, LocationType.BOTTOM_CENTER_TOP_CENTER,0,0);
//                    mSlideFromTopPopup.show(tvTest, LocationType.CENTER_CENTER,0,0);
//                mSlideFromTopPopup.showAutoLocateNotCenter(100,0);
//                mSlideFromTopPopup.showAutoLocateNotCenter(100,1600);
//                mSlideFromTopPopup.showAutoLocateNotCenter(900,100);
//                mSlideFromTopPopup.showAutoLocateNotCenter(900,1400);
//                mSlideFromTopPopup.setOffsetX(-100);
                mSlideFromTopPopup.showAutoLocateNotCenter(tvTest1);

            }
        });*/
        tvTest1.setOnClickListener(mFromTopOnClickListener);
        tvTest2.setOnClickListener(mFromTopOnClickListener);
        tvTest3.setOnClickListener(mFromTopOnClickListener);
        tvTest4.setOnClickListener(mFromTopOnClickListener);
    }

    private class FromTopOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            /*mSlideFromTopPopup.setCompromiseSizeWhenCenter(true);
            mSlideFromTopPopup.show(v,LocationType.BOTTOM_CENTER_TOP_CENTER);*/
            mSlideFromTopPopup.showAutoLocateCenter(v);
//            mSlideFromTopPopup.showAutoLocateNotCenter(v);
        }
    }
    private OnDismissListener onDismissListener = new OnDismissListener() {

        @Override
        public boolean onBeforeDismiss() {
            startDismissArrowAnima();
            return super.onBeforeDismiss();
        }

        @Override
        public void onDismiss() {

        }
    };

    @Override
    public BaseOldPopupWindow getPopup() {
        return null;
    }

    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_slide_from_top_popup, container, false);
    }
}
