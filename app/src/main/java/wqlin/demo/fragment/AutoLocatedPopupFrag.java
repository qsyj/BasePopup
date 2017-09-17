package wqlin.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import wqlin.basepopup.BaseOldPopupWindow;
import wqlin.basepopup.R;
import wqlin.demo.popup.AutoLocatedPopup;

/**
 * Created by wqlin on 2016/11/23.
 */
public class AutoLocatedPopupFrag extends SimpleBaseFrag implements View.OnClickListener {
    private Button popup_show;
    private Button popup_show1;
    private Button popup_show2;
    private Button popup_show3;

    private AutoLocatedPopup autoLocatedPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        autoLocatedPopup = new AutoLocatedPopup(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {
        popup_show = (Button) findViewById(R.id.popup_show);
        popup_show1 = (Button) findViewById(R.id.popup_show1);
        popup_show2 = (Button) findViewById(R.id.popup_show2);
        popup_show3 = (Button) findViewById(R.id.popup_show3);

        popup_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLocatedPopup.showAutoLocateNotCenter(v);
            }
        });

        popup_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLocatedPopup.showAutoLocateNotCenter(v);
            }
        });

        popup_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLocatedPopup.showAutoLocateNotCenter(v);
            }
        });

        popup_show3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLocatedPopup.showAutoLocateNotCenter(v);
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
        return mInflater.inflate(R.layout.frag_auto_located_popup, container, false);
    }
}
