package com.android11.netspeed.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android11.netspeed.R;
import com.android11.netspeed.about.AboutActivity;
import com.android11.netspeed.main.BaseFragment;
import com.android11.netspeed.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingFragment extends BaseFragment {

    @Bind(R.id.sw_stan)
    CheckBox swStan;
    @Bind(R.id.tv_modlename)
    TextView tvModlename;
    @Bind(R.id.tv_modletime)
    TextView tvModletime;
    @Bind(R.id.rl_gocomment)
    RelativeLayout rlGocomment;
    @Bind(R.id.rl_about)
    RelativeLayout rlAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_setting, null);
        ButterKnife.bind(this, v);
        // 5秒为快速模式
        initView();

        return v;
    }

    private void initView() {
        if (spu.getValueInt("model") == 5) {
            swStan.setChecked(false);
            tvModlename.setText("快速测速");
            tvModletime.setText("标准测速需要耗时5秒左右");
        } else {
            swStan.setChecked(true);
            tvModlename.setText("标准测速");
            tvModletime.setText("标准测速需要耗时10秒左右");
        }
        swStan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvModlename.setText("标准测速");
                    tvModletime.setText("标准测速需要耗时10秒左右");
                    spu.setValueInt("model", 10);
                } else {
                    tvModlename.setText("快速测速");
                    tvModletime.setText("标准测速需要耗时5秒左右");
                    spu.setValueInt("model", 5);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_gocomment, R.id.rl_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_gocomment:
                Tools.goMarket(getActivity());
                break;
            case R.id.rl_about:
                Intent goabout = new Intent(getActivity(), AboutActivity.class);
                getActivity().startActivity(goabout);
                break;
        }
    }
}
