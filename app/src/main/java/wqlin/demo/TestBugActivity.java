package wqlin.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import wqlin.basepopup.R;

public class TestBugActivity extends FragmentActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bug);
        findViewById(R.id.tv_test).setOnClickListener(this);
    }
//LinearLayout ListView MATCH是测量 子View为0  高度测量礽0
    private Activity getAct() {
        return this;
    }
    private void test() {
        View view=getAct().getLayoutInflater().inflate(R.layout.test_ll, null);
//        RelativeLayout sv = new RelativeLayout(getAct());
//        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        sv.addView(view);
//        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int ws=View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.UNSPECIFIED);
        int hs=View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.UNSPECIFIED);
        view.measure(ws, hs);
//        sv.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();
    }

    @Override
    public void onClick(View v) {
        test();
    }
}
