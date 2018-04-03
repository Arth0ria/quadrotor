package cn.dxkite.quadrotor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.dxkite.gec.connector.GecMessage;
import cn.dxkite.quadrotor.adapter.ControlFragmentAdapter;
import cn.dxkite.quadrotor.fragment.ControlFragment;
import cn.dxkite.quadrotor.fragment.DebugFragment;
import cn.dxkite.quadrotor.fragment.GamePadFragment;
import cn.dxkite.quadrotor.fragment.SettingFragment;
import cn.dxkite.view.StaticViewPager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView ppsView, messageView, uavName;
    QuadrotorService.QuadrotorBinder binder;
    ServiceConnection connection;
    Context context;
    Button connect;
    ImageView uavView;
    StaticViewPager viewPager;
    boolean prepareSend = false;
    List<ControlFragment> fragmentList = new ArrayList<>();

    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Binary XML file line #17: Error inflating class Button
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        context = getApplicationContext();

        messageView = findViewById(R.id.lastMessageView);
        ppsView = findViewById(R.id.ppsView);
        connect = findViewById(R.id.connect);
        uavView = findViewById(R.id.uavView);
        uavName = findViewById(R.id.uavName);
        viewPager = findViewById(R.id.contentView);

        uavView.setOnClickListener(this);
        uavName.setOnClickListener(this);

//        initFragment();


        connect.setOnClickListener(this);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                binder = (QuadrotorService.QuadrotorBinder) service;


                binder.addMessageReceivedListener(new QuadrotorService.OnMessageReceivedListener() {
                    protected  String wifiName;
                    protected int wifiAddress;
                    protected String wifiMac;

                    @Override
                    public void onReceived(final GecMessage message) {
//                        Log.d(TAG, "receive message " + message.toHexString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageView.setText(message.toHexString());
                            }
                        });
                    }

                    @Override
                    public void onConnected() {
                        Log.d(TAG, "onConnected");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "飞机连接成功", Toast.LENGTH_SHORT).show();
                                uavView.setImageResource(R.drawable.uav_connect);
                                initFragment();
                                for (ControlFragment fragment : fragmentList) {
                                    fragment.setBinder(binder);
                                }
                            }
                        });
                    }

                    @Override
                    public void onDisconnected() {
                        prepareSend = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "飞机连接断开", Toast.LENGTH_SHORT).show();
                                uavView.setImageResource(R.drawable.uav_error);
                            }
                        });
                    }

                    @Override
                    public void onServerConnected(String name, int address, String mac) {
                        Log.d(TAG, "onServerConnected");
                        prepareSend = true;
                        wifiName= name;
                        wifiMac=mac;
                        wifiAddress=address;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                connect.setBackgroundResource(R.drawable.power_on);
                                uavName.setText(wifiName + " - " + wifiMac);
                            }
                        });
                        binder.waitingMessage();
                        GecMessage message = new GecMessage(GecMessage.WRITE);
                        message.setPitch(1500);
                        message.setCourse(1500);
                        message.setRoll(1500);
                        message.setPower(0);
                        binder.setSimple(message);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void setPackageFrequency(final int frequency) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ppsView.setText(String.valueOf(frequency));
                            }
                        });
                    }
                });

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Toast.makeText(context, "服务连接断开", Toast.LENGTH_SHORT).show();
            }
        };

    }

    public void initFragment() {
        fragmentList.add(ControlFragment.createFragment(SettingFragment.class, viewPager));
        fragmentList.add(ControlFragment.createFragment(GamePadFragment.class, viewPager));
        fragmentList.add(ControlFragment.createFragment(DebugFragment.class, viewPager));
        viewPager.setAdapter(new ControlFragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setScrollable(true);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uavView:
            case R.id.uavName:
                if (prepareSend) {
                    Log.d(TAG,"reconnecting server");
                    binder.connect();
                }
                break;
            case R.id.connect:
                Log.d(TAG,"connecting server");
                startService(new Intent(this, QuadrotorService.class));
                bindService(new Intent(this, QuadrotorService.class), connection, BIND_AUTO_CREATE);
                break;
        }
    }
}
