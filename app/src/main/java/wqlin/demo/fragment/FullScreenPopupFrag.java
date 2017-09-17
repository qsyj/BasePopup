package wqlin.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import wqlin.basepopup.BaseOldPopupWindow;
import wqlin.basepopup.R;
import wqlin.demo.popup.FullScreenPopup;

/**
 * Created by wqlin on 2016/10/11.
 */
public class FullScreenPopupFrag extends SimpleBaseFrag {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public BaseOldPopupWindow getPopup() {
        return new FullScreenPopup(mContext);
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_full_popup, container, false);
    }
}
