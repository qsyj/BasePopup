package wqlin.basepopup.callback;

import wqlin.basepopup.entity.LocationConsumer;
import wqlin.basepopup.entity.LocationSizeInfo;

/**
 * Created by wqlin on 2017/9/17.
 */

public interface LocationSizeIntercept {
    LocationSizeInfo getLocationSize(LocationConsumer consumer);
}
