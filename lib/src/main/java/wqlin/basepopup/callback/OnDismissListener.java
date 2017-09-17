package wqlin.basepopup.callback;

import android.widget.PopupWindow;

/**
 * Created by wqlin on 2017/9/17.
 * 在执行popupwindow dismiss()前调用 onBeforeDismiss() ;dismiss() 后调用onDismiss();
 */

public  abstract class OnDismissListener implements PopupWindow.OnDismissListener {
    /**
     * <b>return ture for perform dismiss</b>
     *
     * @return
     */
    public boolean onBeforeDismiss() {
        return true;
    }
}
