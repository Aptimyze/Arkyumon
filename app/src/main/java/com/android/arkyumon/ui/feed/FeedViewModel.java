package com.android.arkyumon.ui.feed;

import com.android.arkyumon.data.DataManager;
import com.android.arkyumon.ui.base.BaseViewModel;
import com.android.arkyumon.utils.rx.SchedulerProvider;

/**
 * Created by Jyoti on 29/07/17.
 */

public class FeedViewModel extends BaseViewModel {

    public FeedViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
