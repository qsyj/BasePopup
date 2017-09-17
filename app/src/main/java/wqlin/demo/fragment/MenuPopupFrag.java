package wqlin.demo.fragment;

import android.view.View;
import android.widget.Button;

import wqlin.basepopup.BaseOldPopupWindow;
import wqlin.basepopup.R;
import wqlin.demo.popup.MenuPopup;

/**
 * Created by wqlin on 2016/1/22.
 * menu
 */
public class MenuPopupFrag extends SimpleBaseFrag{
    private Button mButton;
    private MenuPopup mMenuPopup;
    @Override
    public void bindEvent() {
        mButton= (Button) mFragment.findViewById(R.id.popup_show);
        mMenuPopup=new MenuPopup(mContext);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPopup.showPopupWindow(v);
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
        return mInflater.inflate(R.layout.frag_menu_popup,container,false);
    }
}
