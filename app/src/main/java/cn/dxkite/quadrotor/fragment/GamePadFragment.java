package cn.dxkite.quadrotor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import cn.dxkite.gec.connector.GecMessage;
import cn.dxkite.quadrotor.R;
import cn.dxkite.view.RockerView;

import static cn.dxkite.view.RockerView.DirectionMode.DIRECTION_2_VERTICAL;
import static cn.dxkite.view.RockerView.DirectionMode.DIRECTION_4_ROTATE_45;

public class GamePadFragment extends ControlFragment {

    final static String TAG ="GamePadFragment";

    Switch scrollLook,rockerLock;
    RockerView directionRockerView,powerRockerView;
    int directionScale =10,powerScale;
    TextView textView;
    boolean moveLock =false;
//    GecMessage message= getSimpleMessage();

    @Override
    public View onInitView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gamepad,null,false);
        scrollLook=view.findViewById(R.id.lock);
        scrollLook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewPager.setScrollable(!isChecked);
            }
        });
        rockerLock=view.findViewById(R.id.move);
        rockerLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                moveLock=isChecked;
            }
        });
        directionRockerView = view.findViewById(R.id.direction);
        powerRockerView = view.findViewById(R.id.power);

        textView =view.findViewById(R.id.debug);

        directionRockerView.setOnDistanceLevelListener(new RockerView.OnDistanceLevelListener() {
            @Override
            public void onDistanceLevel(int level) {
                directionScale = level;
            }
        });
        powerRockerView.setOnDistanceLevelListener(new RockerView.OnDistanceLevelListener() {
            @Override
            public void onDistanceLevel(int level) {
                powerScale = level;
            }
        });

        directionRockerView.setOnShakeListener(DIRECTION_4_ROTATE_45, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(RockerView.Direction direction) {
                Log.d(TAG,"directionRockerView direction -> "+direction);
                GecMessage message=getSimpleMessage();
                int value =0;

                switch (direction) {
                    case DIRECTION_UP:
                        value=message.getPitch() + directionScale;
                        if (value >3000) {
                            value = 3000;
                        }
                        message.setPitch(value);
                        break;
                    case DIRECTION_DOWN:
                         value=message.getPitch() - directionScale;
                        if (value < 0) {
                            value = 0;
                        }
                        message.setPitch(value);
                        break;
                    case DIRECTION_LEFT:
                        value=message.getRoll() + directionScale;
                        if (value >3000) {
                            value = 3000;
                        }
                        message.setRoll(value);
                        break;
                    case DIRECTION_RIGHT:
                        value=message.getRoll() - directionScale;
                        if (value < 0) {
                            value = 0;
                        }
                        message.setRoll(value);
                        break;
                    case DIRECTION_CENTER:
                        message.setRoll(1500);
                        message.setPitch(1500);
                        break;

                }
                if (!moveLock) {
                    setSimpleMessage(message);
                    refreshOutput(message);
                }
            }

            @Override
            public void onFinish() {

            }
        });


        powerRockerView.setOnShakeListener(DIRECTION_4_ROTATE_45, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(RockerView.Direction direction) {
                GecMessage message=getSimpleMessage();
                int value =0;
                Log.d(TAG,"powerRockerView direction -> "+direction);
                switch (direction) {
                    case DIRECTION_UP:
                        value=message.getPower() + powerScale;
                        if (value >1000) {
                            value = 1000;
                        }
                        message.setPower(value);
                        break;
                    case DIRECTION_DOWN:
                        value=message.getPower() - powerScale;
                        if (value < 0) {
                            value = 0;
                        }
                        message.setPower(value);
                        break;
                    case DIRECTION_LEFT:
                        value=message.getCourse() + powerScale;
                        if (value >3000) {
                            value = 3000;
                        }
                        message.setCourse(value);
                        break;
                    case DIRECTION_RIGHT:
                        value=message.getCourse() - powerScale;
                        if (value < 0) {
                            value = 0;
                        }
                        message.setCourse(value);
                        break;
                    case DIRECTION_CENTER:
                        message.setCourse(1500);
                        break;
                }
                if (!moveLock) {
                    setSimpleMessage(message);
                    refreshOutput(message);
                }
            }

            @Override
            public void onFinish() {

            }
        });


        return view;
    }

    void refreshOutput(GecMessage message) {
        String text="油门 = " + message.getPower() +"\r\n" +
                "翻滚 = " + message.getRoll() + "\r\n" +
                "俯仰 = " + message.getPitch() +"\r\n" +
                "航向 = " + message.getCourse() + "\r\n" +
                "连接 = " + (getBinder() != null);
        textView.setText(text);
    }
}
