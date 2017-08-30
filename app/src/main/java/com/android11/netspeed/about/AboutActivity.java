package com.android11.netspeed.about;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android11.netspeed.R;
import com.android11.netspeed.main.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AboutActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about);
        ButterKnife.bind(this);
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
        mToolbar.setTitle("关于");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
