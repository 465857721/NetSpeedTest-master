package com.android11.netspeed.floatspeed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android11.netspeed.R;

import java.lang.reflect.Field;


public class SpeedFloatFragment extends Fragment {
    private int REQUEST_CODE = 0;
    private Button showBt;
    private Button closeBt;
    private RadioButton radioBt1;
    private RadioButton radioBt2;
    private RadioButton radioBt3;
    private Toolbar mToolbar;
    public static final String SETTING = "setting";
    public static final String BOTH = "both";
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String CHANGED = "changed";
    public static final String INIT_X = "init_x";
    public static final String INIT_Y = "init_y";
    public static final String IS_SHOWN = "is_shown";
    private ViewGroup bannerContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_speed, null);
        init(v);
        return v;
    }

    private void init(View v) {


        showBt = (Button) v.findViewById(R.id.bt_show);
        closeBt = (Button) v.findViewById(R.id.bt_close);
        radioBt1 = (RadioButton) v.findViewById(R.id.radio_1);
        radioBt2 = (RadioButton) v.findViewById(R.id.radio_2);
        radioBt3 = (RadioButton) v.findViewById(R.id.radio_3);

        showBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startService(new Intent(getActivity(), SpeedCalculationService.class));
            }
        });
        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().stopService(new Intent(getActivity(), SpeedCalculationService.class));
            }
        });
        WindowUtil.statusBarHeight = getStatusBarHeight();
        String setting = (String) SharedPreferencesUtils.getFromSpfs(getActivity(), SETTING, BOTH);
        if (setting.equals(BOTH)) {
            radioBt1.setChecked(true);
        } else if (setting.equals(UP)) {
            radioBt2.setChecked(true);
        } else {
            radioBt3.setChecked(true);
        }
        v.findViewById(R.id.bt_gosetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCommonPermission(getActivity());
            }
        });
        bannerContainer = (ViewGroup) v.findViewById(R.id.bv);
        RadioGroup group = (RadioGroup) v.findViewById(R.id.radio_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
//                int radioButtonId = arg0.getCheckedRadioButtonId();
                onRadioButtonClick(arg0);
            }
        });
    }

    private void applyCommonPermission(Context context) {
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));

            startActivity(localIntent);
        } else {
            try {
                Class clazz = Settings.class;
                Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
                Intent intent = new Intent(field.get(null).toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            } catch (Exception e) {

                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));

                startActivity(localIntent);
            }
        }

    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getActivity())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE) {
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
//                if (Settings.canDrawOverlays(this)) {
//                    init();
//                } else {
//                    Toast.makeText(this, "请授予悬浮窗权限", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }
//    }

    private int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        return statusBarHeight;
    }

    public void onRadioButtonClick(RadioGroup view) {
        switch (view.getCheckedRadioButtonId()) {
            case R.id.radio_1:
//                radioBt1.setChecked(true);
                SharedPreferencesUtils.putToSpfs(getActivity(), SETTING, BOTH);
                break;
            case R.id.radio_2:
//                radioBt2.setChecked(true);
                SharedPreferencesUtils.putToSpfs(getActivity(), SETTING, UP);
                break;
            case R.id.radio_3:
//                radioBt3.setChecked(true);
                SharedPreferencesUtils.putToSpfs(getActivity(), SETTING, DOWN);
                break;
            default:
                break;
        }
        getActivity().startService(new Intent(getActivity(), SpeedCalculationService.class)
                .putExtra(CHANGED, true));
    }

}
