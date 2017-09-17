package wqlin.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import wqlin.basepopup.BaseOldPopupWindow;
import wqlin.basepopup.R;
import wqlin.basepopup.callback.OnDismissListener;
import wqlin.demo.popup.ScalePopup;

/**
 * Created by wqlin on 2016/1/15.
 */
public class ScalePopupFrag extends SimpleBaseFrag {

    private ScalePopup scalePopup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {
        scalePopup=new ScalePopup(mContext);
        scalePopup.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                Toast.makeText(mContext,"dismiss",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public BaseOldPopupWindow getPopup() {
        return scalePopup;
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_scale_popup, container, false);
    }
}
