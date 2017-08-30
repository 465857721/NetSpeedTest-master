package com.android11.netspeed.floatspeed;

import android.content.Context;
import android.graphics.PixelFormat;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.text.NumberFormat;

/**
 * Created by mrrobot on 16/12/28.
 */

public class WindowUtil {
    public static int statusBarHeight = 0;
    //记录悬浮窗的位置
    public static int initX, initY;
    private WindowManager windowManager;
    public SpeedView speedView;
    private WindowManager.LayoutParams params;
    private Context context;

    public boolean isShowing() {
        return isShowing;
    }

    public static boolean isShowing = false;

    public static final int INTERVAL = 2000;
    private long preRxBytes, preSeBytes;
    private long rxBytes, seBytes;
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            calculateNetSpeed();
            sendEmptyMessageDelayed(0, INTERVAL);
            Log.d("zkzk", "handler");
        }
    };

    public void onSettingChanged() {
        String setting = (String) SharedPreferencesUtils.getFromSpfs(context, SpeedFloatFragment.SETTING, SpeedFloatFragment.BOTH);
        if (setting.equals(SpeedFloatFragment.BOTH)) {
            speedView.upText.setVisibility(View.VISIBLE);
            speedView.downText.setVisibility(View.VISIBLE);
        } else if (setting.equals(SpeedFloatFragment.UP)) {
            speedView.upText.setVisibility(View.VISIBLE);
            speedView.downText.setVisibility(View.GONE);
        } else {
            speedView.upText.setVisibility(View.GONE);
            speedView.downText.setVisibility(View.VISIBLE);
        }
    }

    private void calculateNetSpeed() {
        //获取总的接受字节数，包含Mobile和WiFi等
        rxBytes = TrafficStats.getTotalRxBytes();
//        总的发送字节数，包含Mobile和WiFi等
        seBytes = TrafficStats.getTotalTxBytes();
        double downloadSpeed = (rxBytes - preRxBytes) / 2;//两秒更新一次 所以除以2
        double uploadSpeed = (seBytes - preSeBytes) / 2;
        preRxBytes = rxBytes;
        preSeBytes = seBytes;
        //根据范围决定显示单位
        String upSpeed = null;
        String downSpeed = null;

        NumberFormat df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(2);

        if (downloadSpeed > 1024 * 1024) {
            downloadSpeed /= (1024 * 1024);
            downSpeed = df.format(downloadSpeed) + "MB/S";
        } else if (downloadSpeed > 1024) {
            downloadSpeed /= (1024);
            downSpeed = df.format(downloadSpeed) + "KB/S";
        } else {
            downSpeed = df.format(downloadSpeed) + "B/S";
        }

        if (uploadSpeed > 1024 * 1024) {
            uploadSpeed /= (1024 * 1024);
            upSpeed = df.format(uploadSpeed) + "MB/S";
        } else if (uploadSpeed > 1024) {
            uploadSpeed /= (1024);
            upSpeed = df.format(uploadSpeed) + "KB/S";
        } else {
            upSpeed = df.format(uploadSpeed) + "B/S";
        }

        updateSpeed(downSpeed, upSpeed);
    }

    public WindowUtil(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        speedView = new SpeedView(context);
        params = new WindowManager.LayoutParams();
        params.x = initX;
        params.y = initY;
        params.width = params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //设置悬浮窗可以拖拽至状态栏的位置
//        | WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

    }

    public void showSpeedView() {
        windowManager.addView(speedView, params);
        isShowing = true;
        preRxBytes = TrafficStats.getTotalRxBytes();
        preSeBytes = TrafficStats.getTotalTxBytes() - preRxBytes;
        handler.sendEmptyMessage(0);
    }

    public void closeSpeedView() {
        windowManager.removeView(speedView);
        isShowing = false;
    }

    public void updateSpeed(String downSpeed, String upSpeed) {
        speedView.upText.setText(upSpeed);
        speedView.downText.setText(downSpeed);
    }
}
