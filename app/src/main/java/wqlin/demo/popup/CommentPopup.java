package wqlin.demo.popup;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wqlin.basepopup.BasePopupWindow;
import wqlin.basepopup.R;
import wqlin.basepopup.callback.LocationSizeIntercept;
import wqlin.basepopup.entity.LocationConsumer;
import wqlin.basepopup.entity.LocationSizeInfo;
import wqlin.basepopup.entity.LocationType;
import wqlin.basepopup.util.PopupHelper;

import static wqlin.basepopup.util.SimpleAnimUtil.getScaleAnimation;

/**
 * Created by wqlin on 2016/1/16.
 * 微信朋友圈评论弹窗
 */
public class CommentPopup extends BasePopupWindow implements View.OnClickListener {

    private ImageView mLikeAnimaView;
    private TextView mLikeText;

    private RelativeLayout mLikeClikcLayout;
    private RelativeLayout mCommentClickLayout;
    public static final String LOCATION_SIZE_INTERCEPT_MSG = CommentPopup.class.getName();

    private OnCommentPopupClickListener mOnCommentPopupClickListener;

    private Handler mHandler;
    public CommentPopup(Activity context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CommentPopup(Activity context, int w, int h) {
        super(context, w, h);



        mHandler = new Handler();

        mLikeAnimaView = (ImageView) findViewById(R.id.iv_like);
        mLikeText = (TextView) findViewById(R.id.tv_like);

        mLikeClikcLayout = (RelativeLayout) findViewById(R.id.item_like);
        mCommentClickLayout = (RelativeLayout) findViewById(R.id.item_comment);

        mLikeClikcLayout.setOnClickListener(this);
        mCommentClickLayout.setOnClickListener(this);

        buildAnima();
    }

    public void showAutoLeftRightCenter(View v) {
        setLocationSizeIntercept(new AutoLeftRightCentIntercept(),LOCATION_SIZE_INTERCEPT_MSG);
        showAutoLocateNotCenter(v);
    }
    private AnimationSet mAnimationSet;

    private void buildAnima() {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(200);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, .2f);
        mAlphaAnimation.setDuration(400);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(400);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
        return getScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    public Animation initExitAnimation() {
        return getScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    public View onCreatePopupView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.popup_comment, null);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.comment_popup_contianer);
    }
    //=============================================================Getter/Setter

    public OnCommentPopupClickListener getOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setOnCommentPopupClickListener(OnCommentPopupClickListener onCommentPopupClickListener) {
        mOnCommentPopupClickListener = onCommentPopupClickListener;
    }

    //=============================================================clickEvent
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_like:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onLikeClick(v, mLikeText);
                    mLikeAnimaView.clearAnimation();
                    mLikeAnimaView.startAnimation(mAnimationSet);
                }
                break;
            case R.id.item_comment:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onCommentClick(v);
                    dismiss();
                }
                break;
        }
    }

    //=============================================================InterFace
    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, TextView likeText);

        void onCommentClick(View v);
    }
    //=============================================================abortMethods

    @Override
    public View getClickToDismissView() {
        return null;
    }

    static class AutoLeftRightCentIntercept implements LocationSizeIntercept {
        private int xOff = 0;
        private int yOff = 0;
        public AutoLeftRightCentIntercept() {

        }

        public AutoLeftRightCentIntercept(int xOff, int yOff) {
            this.xOff = xOff;
            this.yOff = yOff;
        }

        @Override
        public LocationSizeInfo getLocationSize(LocationConsumer consumer) {
            Object msg = consumer.getLocationSizeInterceptMsg();
            int width = consumer.getWidth();
            int height = consumer.getHeight();
            int[] location = consumer.getLocation();
            if (msg != null && msg.equals(LOCATION_SIZE_INTERCEPT_MSG)) {
                int xOff = 0;
                int yOff = 0;
                int locationType = consumer.getLocationType();

                int screenWidth = consumer.getScreenWidth();
                int screenHeight = consumer.getScreenHeight();
                int anchorWidth = consumer.getAnchorWidth();
                int anchorHeight = consumer.getAnchorHeight();

                int[] anchorLocation = consumer.getAnchorLocation();
                switch (locationType) {
                    case LocationType.RIGHT_TOP_RIGHT_BOTTOM:
                        width = PopupHelper.modifyWidth(width, anchorLocation[0] + xOff, screenWidth);
                        if (screenHeight - anchorLocation[1] - anchorHeight/2+yOff < height / 2) {
                            height = PopupHelper.modifyHeight(height, anchorLocation[1] + anchorHeight+yOff, screenHeight);
                            location[1] = location[1] + anchorHeight+yOff;
                        } else {
                            location[1] = location[1] +(anchorHeight + height) / 2 + yOff;
                        }
                        location[0] =location[0]- consumer.getAnchorWidth()+xOff;

                        break;
                    case LocationType.RIGHT_BOTTOM_RIGHT_TOP:
                        width = PopupHelper.modifyWidth(width, anchorLocation[0] + xOff, screenWidth);
                        if (screenHeight - anchorLocation[1] - anchorHeight/2+yOff < height / 2) {
                            height = PopupHelper.modifyHeight(height, screenHeight-anchorLocation[1]+yOff, screenHeight);
                            location[1] = location[1] - anchorHeight+yOff;
                        } else {
                            location[1] = location[1] -(anchorHeight + height) / 2 + yOff;
                        }

                        location[0] =location[0]- consumer.getAnchorWidth()+xOff;

                        break;
                    case LocationType.LEFT_TOP_LEFT_BOTTOM:
                        width = PopupHelper.modifyWidth(width, screenWidth-anchorLocation[0]-anchorWidth + xOff, screenWidth);

                        if (screenHeight - anchorLocation[1] - anchorHeight/2+yOff < height / 2) {
                            height = PopupHelper.modifyHeight(height, anchorLocation[1] + anchorHeight+yOff, screenHeight);
                            location[1] = location[1] + anchorHeight+yOff;
                        } else {
                            location[1] = location[1] +(anchorHeight + height) / 2 + yOff;
                        }

                        location[0] =location[0] + consumer.getAnchorWidth()+xOff;
                        break;
                    case LocationType.LEFT_BOTTOM_LEFT_TOP:
                        width = PopupHelper.modifyWidth(width, screenWidth-anchorLocation[0]-anchorWidth + xOff, screenWidth);

                        if (anchorLocation[1] + anchorHeight/2+yOff < height / 2) {
                            height = PopupHelper.modifyHeight(height, screenHeight-anchorLocation[1]+yOff, screenHeight);
                            location[1] = location[1] - anchorHeight+yOff;
                        } else {
                            location[1] = location[1]-(anchorHeight + height) / 2+ yOff;
                        }

                        location[0] =location[0] + consumer.getAnchorWidth()+xOff;
                        break;
                }
            }

            return new LocationSizeInfo(location, width, height);
        }
    }


}
