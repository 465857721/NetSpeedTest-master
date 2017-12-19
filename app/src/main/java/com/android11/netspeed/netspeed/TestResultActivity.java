package com.android11.netspeed.netspeed;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android11.netspeed.BuildConfig;
import com.android11.netspeed.R;
import com.android11.netspeed.main.BaseActivity;
import com.android11.netspeed.utils.Tools;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestResultActivity extends BaseActivity implements NativeExpressAD.NativeExpressADListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_big)
    TextView tvBig;
    @BindView(R.id.tv_ava)
    TextView tvAva;
    @BindView(R.id.tv_u1)
    TextView tvU1;
    @BindView(R.id.tv_u2)
    TextView tvU2;
    @BindView(R.id.tv_delay)
    TextView tvDelay;

    private final String TAG = "AD_DEMO";
    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;
    private ViewGroup container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_speed_result);
        ButterKnife.bind(this);

        tvAva.setText(getStringSpeed(getIntent().getIntExtra("ava", 0)));
        tvBig.setText(getStringSpeed(getIntent().getIntExtra("big", 0)));
        tvU1.setText(getStringSpeedUnit(getIntent().getIntExtra("ava", 0)));
        tvU2.setText(getStringSpeedUnit(getIntent().getIntExtra("big", 0)));
        initViw();
        tvDelay.setText(getIntent().getStringExtra("delay"));
        // ad
        container = findViewById(R.id.container);

        String[] cArray = getResources().getStringArray(R.array.channel);
        for (String c : cArray) {
            String channel = Tools.getAppMetaData(this, "UMENG_CHANNEL");
            if (c.equals(channel)
                    && (System.currentTimeMillis() - Long.valueOf(BuildConfig.releaseTime) < 3 * 24 * 60 * 60 * 1000)) {

                return;
            }
        }
        refreshAd();
    }

    private void refreshAd() {
        nativeExpressAD = new NativeExpressAD(this, new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), "1106433744", "6040327846089230", this); // 传入Activity
        // 注意：如果您在联盟平台上新建原生模板广告位时，选择了“是”支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        nativeExpressAD.loadAD(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initViw() {
        setSupportActionBar(mToolbar);
        //显示那个箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("测试结果");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private String getStringSpeed(int downloadSpeed) {

        NumberFormat df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(2);
        String downSpeed;
        if (downloadSpeed > 1024 * 1024) {
            downloadSpeed /= (1024 * 1024);
            downSpeed = df.format(downloadSpeed);
        } else if (downloadSpeed > 1024) {
            downloadSpeed /= (1024);
            downSpeed = df.format(downloadSpeed);
        } else {
            downSpeed = df.format(downloadSpeed);
        }
        return downSpeed;
    }

    private String getStringSpeedUnit(int downloadSpeed) {

        NumberFormat df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(2);
        String downSpeed;
        if (downloadSpeed > 1024 * 1024) {
            downloadSpeed /= (1024 * 1024);
            downSpeed = "MB/S";
        } else if (downloadSpeed > 1024) {
            downloadSpeed /= (1024);
            downSpeed = "KB/S";
        } else {
            downSpeed = "B/S";
        }
        return downSpeed;
    }

    public void onNoAD(AdError error) {
        Log.i("AD_DEMO", String.format("onADError, error code: %d, error msg: %s", error.getErrorCode(), error.getErrorMsg()));
    }

    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        Log.i(TAG, "onADLoaded: " + adList.size());
        // 释放前一个NativeExpressADView的资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
        // 3.返回数据后，SDK会返回可以用于展示NativeExpressADView列表
        nativeExpressADView = adList.get(0);
        nativeExpressADView.render();
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }

        // 需要保证View被绘制的时候是可见的，否则将无法产生曝光和收益。
        container.addView(nativeExpressADView);
    }

    @Override
    public void onRenderFail(NativeExpressADView adView) {
        Log.i(TAG, "onRenderFail");
    }

    @Override
    public void onRenderSuccess(NativeExpressADView adView) {
        Log.i(TAG, "onRenderSuccess");
    }

    @Override
    public void onADExposure(NativeExpressADView adView) {
        Log.i(TAG, "onADExposure");
    }

    @Override
    public void onADClicked(NativeExpressADView adView) {
        Log.i(TAG, "onADClicked");
    }

    @Override
    public void onADClosed(NativeExpressADView adView) {
        Log.i(TAG, "onADClosed");
    }

    @Override
    public void onADLeftApplication(NativeExpressADView adView) {
        Log.i(TAG, "onADLeftApplication");
    }

    @Override
    public void onADOpenOverlay(NativeExpressADView adView) {
        Log.i(TAG, "onADOpenOverlay");
    }

    @Override
    public void onADCloseOverlay(NativeExpressADView adView) {
        Log.i(TAG, "onADCloseOverlay");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 4.使用完了每一个NativeExpressADView之后都要释放掉资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
    }
}
