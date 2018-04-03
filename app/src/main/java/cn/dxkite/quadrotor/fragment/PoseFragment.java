package cn.dxkite.quadrotor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import cn.dxkite.gec.connector.GecMessage;
import cn.dxkite.quadrotor.QuadrotorService;
import cn.dxkite.quadrotor.R;
import cn.dxkite.view.NumberEdit;

public class PoseFragment extends ControlFragment implements NumberEdit.OnNumberChangeListener, View.OnClickListener {

    NumberEdit gyroX, gyroY, gyroZ, accX, accY, accZ;
    Button write, read;

    GecMessage pose;
    final static String TAG = "PoseFragment";

    @Override
    public View onInitView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_pose, null, false);
        // 陀螺仪
        gyroX = view.findViewById(R.id.gyroX);
        gyroX.setMax(1000);
        gyroX.setNumber(500);
        gyroX.setOnNumberChangeListener(this);
        gyroY = view.findViewById(R.id.gyroY);
        gyroY.setMax(1000);
        gyroY.setNumber(500);
        gyroY.setOnNumberChangeListener(this);
        gyroZ = view.findViewById(R.id.gyroZ);
        gyroZ.setMax(1000);
        gyroZ.setNumber(500);
        gyroZ.setOnNumberChangeListener(this);


        read = view.findViewById(R.id.read);
        read.setOnClickListener(this);
        write = view.findViewById(R.id.write);
        write.setOnClickListener(this);

        accX = view.findViewById(R.id.accX);
        accX.setMax(1000);
        accX.setSize(50);
        accX.setNumber(500);
        accX.setOnNumberChangeListener(this);
        accY = view.findViewById(R.id.accY);
        accY.setMax(1000);
        accY.setSize(50);
        accY.setNumber(500);
        accY.setOnNumberChangeListener(this);
        accZ = view.findViewById(R.id.accZ);
        accZ.setMax(1000);
        accZ.setNumber(500);
        accZ.setSize(50);
        accZ.setOnNumberChangeListener(this);

        pose = new GecMessage(GecMessage.WRITE_POSE);
        pose.setAccelerationX(500);
        pose.setAccelerationY(500);
        pose.setAccelerationZ(500);
        pose.setGyroscopeX(500);
        pose.setGyroscopeY(500);
        pose.setGyroscopeZ(500);
        return view;
    }


    @Override
    public void onNumberChange(NumberEdit numberEdit, int number) {
        switch (numberEdit.getId()) {
            case R.id.gyroX:
                pose.setGyroscopeX(number);
                break;
            case R.id.gyroY:
                pose.setGyroscopeY(number);
                break;
            case R.id.gyroZ:
                pose.setGyroscopeZ(number);
                break;
            case R.id.accX:
                pose.setAccelerationX(number);
                break;
            case R.id.accY:
                pose.setAccelerationY(number);
                break;
            case R.id.accZ:
                pose.setAccelerationZ(number);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write:
                Log.d(TAG, "click write pose button");
                send(pose);
                Log.d(TAG,"Pose send -> "+pose);
            case R.id.read:
                Log.d(TAG, "click read pose button");
                send(new GecMessage(GecMessage.READ_POSE), new MessageReceiver() {
                    @Override
                    public void onReceived(GecMessage message) {
                        if (message == null) {
                            Toast.makeText(getContext(), "读取姿态数据失败", Toast.LENGTH_SHORT).show();
                        }else{
                            gyroX.setNumber(message.getGyroscopeX()+500);
                            gyroY.setNumber(message.getGyroscopeY()+500);
                            gyroZ.setNumber(message.getGyroscopeZ()+500);
                            accX.setNumber(message.getAccelerationX()+500);
                            accY.setNumber(message.getAccelerationY()+500);
                            accZ.setNumber(message.getAccelerationZ()+500);
                            Log.d(TAG,"MessageReceiver -> "+message);
                            Toast.makeText(getContext(), "读取姿态数据成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onReceived(GecMessage message) {
        if (message.getType() != GecMessage.TIMING_RETURN) {
            Log.d(TAG, "receive message " + message.toHexString());
        }
    }

    @Override
    public void setBinder(QuadrotorService.QuadrotorBinder binder) {
        Log.d(TAG, "set binder " + binder);
        super.setBinder(binder);
    }
}
