package com.android11.netspeed.netspeed;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android11.netspeed.R;
import com.android11.netspeed.bean.Info;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class TestSpeedFragment extends Fragment {
    @Bind(R.id.connection_type)
    TextView tv_type;
    @Bind(R.id.now_speed)
    TextView tv_now_speed;
    @Bind(R.id.ave_speed)
    TextView tv_ave_speed;
    @Bind(R.id.ll_topshow)
    LinearLayout llTopshow;
    @Bind(R.id.tester)
    ImageView tester;
    @Bind(R.id.needle)
    ImageView needle;
    @Bind(R.id.heart)
    ImageView heart;
    @Bind(R.id.start_btn)
    Button btn;

    private Info info;
    private byte[] imageBytes;
    private boolean flag;
    private int last_degree = 0, cur_degree;

    private int bigest = 0;
    private int avg = 0;

    private long testtime;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 0x123) {
                tv_now_speed.setText(getStringSpeed(msg.arg1));
                tv_ave_speed.setText(getStringSpeed(msg.arg2));
                startAnimation(msg.arg1 / 1024);
            }
            if (msg.what == 0x100) {
                tv_now_speed.setText("0KB/S");
                startAnimation(0);
                btn.setText("开始测试");
                btn.setAlpha(1.0f);
                btn.setEnabled(true);
                llTopshow.setVisibility(View.INVISIBLE);
                Intent go = new Intent(getActivity(), TestResultActivity.class);
                go.putExtra("ava", avg);
                go.putExtra("big", bigest);
                getActivity().startActivity(go);
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_netspeed, null);
        ButterKnife.bind(this, v);
        info = new Info();

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                tv_type.setText(networkInfo.getTypeName());
                testtime = System.currentTimeMillis();

                btn.setText("测试中");
                btn.setAlpha(0.4f);
                btn.setEnabled(false);
                info.hadfinishByte = 0;
                info.speed = 0;
                info.totalByte = 1024;
                llTopshow.setVisibility(View.VISIBLE);
                tv_now_speed.setText("0KB/S");
                tv_ave_speed.setText("0KB/S");
                ObjectAnimator//
                        .ofFloat(llTopshow, "alpha", 0.0F, 1F)//
                        .setDuration(1000)//
                        .start();
                new DownloadThread().start();
                new GetInfoThread().start();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class DownloadThread extends Thread {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String url_string = "http://imtt.dd.qq.com/16891/3AFA21F3690FB27C82A6AB6024E56852.apk";
            long start_time, cur_time;
            URL url;
            URLConnection connection;
            InputStream iStream;

            try {
                url = new URL(url_string);
                connection = url.openConnection();

                info.totalByte = connection.getContentLength();

                iStream = connection.getInputStream();
                start_time = System.currentTimeMillis();
                while (iStream.read() != -1 && flag && isTestTimeOut()) {

                    info.hadfinishByte++;
                    cur_time = System.currentTimeMillis();
                    if (cur_time - start_time == 0) {
                        info.speed = 1000;
                    } else {
                        info.speed = info.hadfinishByte / (cur_time - start_time) * 1000;
                    }
                }
                iStream.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    class GetInfoThread extends Thread {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            double sum, counter;
            int cur_speed, ave_speed;
            try {
                sum = 0;
                counter = 0;
                while (info.hadfinishByte < info.totalByte && flag && isTestTimeOut()) {
                    Thread.sleep(1000);

                    sum += info.speed;
                    counter++;

                    cur_speed = (int) info.speed;
                    ave_speed = (int) (sum / counter);
                    Message msg = new Message();
//                    msg.arg1 = ((int) cur_speed / 1024);
                    msg.arg1 = cur_speed;
//                    msg.arg2 = ((int) ave_speed / 1024);
                    msg.arg2 = ave_speed;
                    msg.what = 0x123;
                    bigest = bigest > cur_speed ? bigest : cur_speed;
                    avg = ave_speed;

                    handler.sendMessage(msg);
                }
                if ((info.hadfinishByte == info.totalByte && flag) || !isTestTimeOut()) {
                    handler.sendEmptyMessage(0x100);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        flag = true;
        super.onResume();
    }

    private void startAnimation(int cur_speed) {
        cur_degree = getDegree(cur_speed);

        RotateAnimation rotateAnimation = new RotateAnimation(last_degree, cur_degree, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(1000);
        last_degree = cur_degree;
        needle.startAnimation(rotateAnimation);
    }

    private int getDegree(double cur_speed) {
        int ret = 0;
        if (cur_speed >= 0 && cur_speed <= 512) {
            ret = (int) (15.0 * cur_speed / 128.0);
        } else if (cur_speed >= 512 && cur_speed <= 1024) {
            ret = (int) (60 + 15.0 * cur_speed / 256.0);
        } else if (cur_speed >= 1024 && cur_speed <= 10 * 1024) {
            ret = (int) (90 + 15.0 * cur_speed / 1024.0);
        } else {
            ret = 180;
        }
        return ret;
    }

    private boolean isTestTimeOut() {
        return System.currentTimeMillis() - testtime < 5000;
    }


    private String getStringSpeed(int downloadSpeed) {

        NumberFormat df = java.text.NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(2);
        String downSpeed;
        if (downloadSpeed > 1024 * 1024) {
            downloadSpeed /= (1024 * 1024);
            downSpeed = df.format(downloadSpeed) + "MB/S";
        } else if (downloadSpeed > 1024) {
            downloadSpeed /= (1024);
            downSpeed = df.format(downloadSpeed) + "KB/S";
        } else {
            downSpeed = df.format(downloadSpeed) + "B/S";
        }
        return downSpeed;
    }
}