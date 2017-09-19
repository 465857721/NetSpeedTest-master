package com.android11.netspeed.floatspeed;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import static com.android11.netspeed.floatspeed.SpeedFloatFragment.INIT_X;
import static com.android11.netspeed.floatspeed.SpeedFloatFragment.INIT_Y;

/**
 * Created by mrrobot on 16/12/28.
 */

public class SpeedCalculationService extends Service {
    private WindowUtil windowUtil;
    private boolean changed = false;

    @Override
    public void onCreate() {
        super.onCreate();
        WindowUtil.initX = (int) SharedPreferencesUtils.getFromSpfs(this, INIT_X, 0);
        WindowUtil.initY = (int) SharedPreferencesUtils.getFromSpfs(this, INIT_Y, 0);
        windowUtil = new WindowUtil(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            changed = intent.getBooleanExtra(SpeedFloatFragment.CHANGED, false);
            if (changed) {
                windowUtil.onSettingChanged();
            } else {
                if (!windowUtil.isShowing()) {
                    windowUtil.showSpeedView();
                }
                SharedPreferencesUtils.putToSpfs(this, SpeedFloatFragment.IS_SHOWN, true);
            }
        }
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) windowUtil.speedView.getLayoutParams();
        if (params == null)
            return;
        SharedPreferencesUtils.putToSpfs(this, INIT_X, params.x);
        SharedPreferencesUtils.putToSpfs(this, INIT_Y, params.y);
        if (windowUtil.isShowing()) {
            windowUtil.closeSpeedView();
            SharedPreferencesUtils.putToSpfs(this, SpeedFloatFragment.IS_SHOWN, false);
        }
    }
}
