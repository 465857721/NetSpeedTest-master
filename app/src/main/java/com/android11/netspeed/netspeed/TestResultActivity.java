package com.android11.netspeed.netspeed;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android11.netspeed.R;
import com.android11.netspeed.main.BaseActivity;

import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TestResultActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_big)
    TextView tvBig;
    @Bind(R.id.tv_ava)
    TextView tvAva;
    @Bind(R.id.tv_u1)
    TextView tvU1;
    @Bind(R.id.tv_u2)
    TextView tvU2;


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
}
