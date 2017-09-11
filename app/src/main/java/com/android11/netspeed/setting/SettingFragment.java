package com.android11.netspeed.setting;

import android.content.Intent;
import android.net.Uri;
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

    @OnClick({R.id.rl_gocomment, R.id.rl_about, R.id.rl_goqq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_gocomment:
                Tools.goMarket(getActivity());
                break;
            case R.id.rl_about:
                Intent goabout = new Intent(getActivity(), AboutActivity.class);
                getActivity().startActivity(goabout);
                break;
            case R.id.rl_goqq:
                joinQQGroup("QNlp37JfyLP9meWIPrzVwj9K3m2MzDnG");
                break;
        }
    }

    /****************
     *
     * 发起添加群流程。群号：网速显示反馈群(256227946) 的 key 为： QNlp37JfyLP9meWIPrzVwj9K3m2MzDnG
     * 调用 joinQQGroup(QNlp37JfyLP9meWIPrzVwj9K3m2MzDnG) 即可发起手Q客户端申请加群 网速显示反馈群(256227946)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
