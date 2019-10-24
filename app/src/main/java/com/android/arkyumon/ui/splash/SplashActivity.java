/*
 *  Copyright (C) 2019
 *
 *  Licensed under the MIT License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://spit.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.android.arkyumon.ui.splash;

import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.arkyumon.ViewModelProviderFactory;
import com.android.arkyumon.ui.base.BaseActivity;
import com.android.arkyumon.BR;
import com.android.arkyumon.R;
import com.android.arkyumon.databinding.ActivitySplashBinding;
import com.android.arkyumon.ui.login.LoginActivity;
import com.android.arkyumon.ui.main.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

/**
 * Created by CodersClan on 08/07/17.
 */

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    private static final String TAG = "SplashActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Inject
    ViewModelProviderFactory factory;
    
    private SplashViewModel mSplashViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        mSplashViewModel = ViewModelProviders.of(this,factory).get(SplashViewModel.class);
        return mSplashViewModel;
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashViewModel.setNavigator(this);
        mSplashViewModel.startSeeding();
    }

    public boolean isServicesOk()
    {
        Log.d(TAG, "isServicesOk: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SplashActivity.this);

        if(available == ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "isServicesOk: Google Play Services is Working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "You Can't make Map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
