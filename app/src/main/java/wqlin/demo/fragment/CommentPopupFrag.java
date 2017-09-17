package wqlin.demo.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import wqlin.basepopup.BaseOldPopupWindow;
import wqlin.basepopup.R;
import wqlin.demo.popup.CommentPopup;
import wqlin.demo.utils.ToastUtils;

/**
 * Created by wqlin on 2016/1/16.
 */
public class CommentPopupFrag extends SimpleBaseFrag {
    private ImageView mShow1;
    private ImageView mShow2;
    private ImageView mShow3;
    private ImageView mShow4;

    private CommentPopup mCommentPopup;

    @Override
    public void bindEvent() {
        mShow1 = (ImageView) mFragment.findViewById(R.id.show_1);
        mShow2 = (ImageView) mFragment.findViewById(R.id.show_2);
        mShow3 = (ImageView) mFragment.findViewById(R.id.show_3);
        mShow4 = (ImageView) mFragment.findViewById(R.id.show_4);

        mShow1.setOnClickListener(this);
        mShow2.setOnClickListener(this);
        mShow3.setOnClickListener(this);
        mShow4.setOnClickListener(this);

        mCommentPopup = new CommentPopup(mContext);

        mCommentPopup.setOnCommentPopupClickListener(new CommentPopup.OnCommentPopupClickListener() {
            @Override
            public void onLikeClick(View v, TextView likeText) {
                if (v.getTag() == null) {
                    v.setTag(1);
                    likeText.setText("取消");
                }
                else {
                    switch ((int) v.getTag()) {
                        case 0:
                            v.setTag(1);
                            likeText.setText("取消");
                            break;
                        case 1:
                            v.setTag(0);
                            likeText.setText("赞  ");
                            break;
                    }
                }
            }

            @Override
            public void onCommentClick(View v) {
                ToastUtils.ToastMessage(mContext, "评论");
            }
        });
    }

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
        return mInflater.inflate(R.layout.frag_comment_popup, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_1:
                mCommentPopup.showAutoLeftRightCenter(v);
                break;
            case R.id.show_2:
                mCommentPopup.showAutoLeftRightCenter(v);
                break;
            case R.id.show_3:
                mCommentPopup.showAutoLeftRightCenter(v);
                break;
            case R.id.show_4:
                mCommentPopup.showAutoLeftRightCenter(v);
                break;
            default:
                break;
        }
    }
}
