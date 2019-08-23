package com.android.arkyumon;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.arkyumon.ui.feed.FeedViewModel;
import com.android.arkyumon.data.DataManager;
import com.android.arkyumon.ui.about.AboutViewModel;
import com.android.arkyumon.ui.feed.blogs.BlogViewModel;
import com.android.arkyumon.ui.feed.opensource.OpenSourceViewModel;
import com.android.arkyumon.ui.login.LoginViewModel;
import com.android.arkyumon.ui.main.MainViewModel;
import com.android.arkyumon.ui.main.rating.RateUsViewModel;
import com.android.arkyumon.ui.splash.SplashViewModel;
import com.android.arkyumon.utils.rx.SchedulerProvider;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jyotidubey on 22/02/19.
 */
@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

  private final DataManager dataManager;
  private final SchedulerProvider schedulerProvider;

  @Inject
  public ViewModelProviderFactory(DataManager dataManager,
      SchedulerProvider schedulerProvider) {
    this.dataManager = dataManager;
    this.schedulerProvider = schedulerProvider;
  }


  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    if (modelClass.isAssignableFrom(AboutViewModel.class)) {
      //noinspection unchecked
      return (T) new AboutViewModel(dataManager,schedulerProvider);
    } else if (modelClass.isAssignableFrom(FeedViewModel.class)) {
      //noinspection unchecked
      return (T) new FeedViewModel(dataManager,schedulerProvider);
    } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
      //noinspection unchecked
      return (T) new LoginViewModel(dataManager,schedulerProvider);
    } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
      //noinspection unchecked
      return (T) new MainViewModel(dataManager,schedulerProvider);
    }
    else if (modelClass.isAssignableFrom(BlogViewModel.class)) {
      //noinspection unchecked
      return (T) new BlogViewModel(dataManager,schedulerProvider);
    }
    else if (modelClass.isAssignableFrom(RateUsViewModel.class)) {
      //noinspection unchecked
      return (T) new RateUsViewModel(dataManager,schedulerProvider);
    }
    else if (modelClass.isAssignableFrom(OpenSourceViewModel.class)) {
      //noinspection unchecked
      return (T) new OpenSourceViewModel(dataManager,schedulerProvider);
    }
    else if (modelClass.isAssignableFrom(SplashViewModel.class)) {
      //noinspection unchecked
      return (T) new SplashViewModel(dataManager,schedulerProvider);
    }
    throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
  }
}