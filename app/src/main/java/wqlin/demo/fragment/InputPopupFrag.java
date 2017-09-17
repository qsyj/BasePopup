package wqlin.demo.fragment;

import android.view.View;
import android.widget.Button;

import wqlin.basepopup.BaseOldPopupWindow;
import wqlin.basepopup.R;
import wqlin.demo.popup.InputPopup;

/**
 * Created by wqlin on 2016/1/18.
 * 自动弹出输入框的popup
 */
public class InputPopupFrag extends SimpleBaseFrag {

    @Override
    public void bindEvent() {

    }

    @Override
    public BaseOldPopupWindow getPopup() {
        return new InputPopup(mContext);
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_input_popup,container,false);
    }
}
