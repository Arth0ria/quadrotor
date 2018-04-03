package cn.dxkite.quadrotor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;

import cn.dxkite.gec.connector.GecMessage;
import cn.dxkite.quadrotor.R;
import cn.dxkite.quadrotor.adapter.ControllerListAdapter;
import cn.dxkite.quadrotor.model.ControllerModel;

public class DebugFragment extends ControlFragment {

    ListView listView;

    @Override
    public View onInitView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_debug, null, false);
        listView = view.findViewById(R.id.controllers);
        ControllerModel[] controllerInfos = {
                new ControllerModel(0, "发包", 10, 5),
                new ControllerModel(1, "油门", 1000, 0),
                new ControllerModel(3, "横滚(右<->左)", 3000, 1500),
                new ControllerModel(4, "俯仰(后<->前)", 3000, 1500),
                new ControllerModel(2, "航向(顺时针<->逆时针)", 3000, 1500),
        };

        ControllerListAdapter adapter = new ControllerListAdapter(getContext(), R.layout.adapter_controller_list, controllerInfos);
        adapter.setChangeListener(new ControllerListAdapter.OnChangeListener() {
            @Override
            public void change(SeekBar seekBar, int progress, String name, int id, int position) {
                GecMessage simpleMessage = getSimpleMessage();
                switch (id) {
                    case 0:
                        setPackageFrequency(progress);
                        break;
                    case 1:
                        simpleMessage.setPower(progress);
                        break;
                    case 2:
                        simpleMessage.setCourse(progress);
                        break;
                    case 3:
                        simpleMessage.setRoll(progress);
                        break;
                    case 4:
                        simpleMessage.setPitch(progress);
                        break;
                }
                send(simpleMessage);
            }
        });
        listView.setAdapter(adapter);
        return view;
    }
}
