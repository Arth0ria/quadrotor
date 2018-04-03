package cn.dxkite.quadrotor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.dxkite.gec.connector.GecMessage;
import cn.dxkite.quadrotor.R;
import cn.dxkite.view.NumberEdit;

public class PidFragment extends  ControlFragment implements NumberEdit.OnNumberChangeListener,View.OnClickListener {

    GecMessage inPid,outPid;
    NumberEdit inP,inI,inD,outP,outI,outD;
    Button sendIn,sendOut,readIn,readOut;

    @Override
    public View onInitView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_pid,null,false);
//        inP =view.findViewById(R.id.inP);
//        inP.setOnNumberChangeListener(this);
//        inI =view.findViewById(R.id.inI);
//        inI.setOnNumberChangeListener(this);
//        inD =view.findViewById(R.id.inD);
//        inD.setOnNumberChangeListener(this);
//        outP =view.findViewById(R.id.outP);
//        outP.setOnNumberChangeListener(this);
//        outI =view.findViewById(R.id.outI);
//        outI.setOnNumberChangeListener(this);
//        outD =view.findViewById(R.id.outD);
//        outD.setOnNumberChangeListener(this);
//        sendIn = view.findViewById(R.id.writeInPid);
//        sendOut = view.findViewById(R.id.writeOutPid);
//        readIn = view.findViewById(R.id.readInPid);
//        readOut = view.findViewById(R.id.readOutPid);
//        sendIn.setOnClickListener(this);
//        sendOut.setOnClickListener(this);
//        readOut.setOnClickListener(this);
//        readIn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onNumberChange(NumberEdit numberEdit, int number) {

    }

    @Override
    public void onReceived(GecMessage message) {
        switch (message.getType()) {
            case GecMessage.READ_INPID:
                inPid =message;
                inPid.setType(GecMessage.WRITE_INPID);
                break;
            case GecMessage.READ_OUTPID:
                inPid =message;
                inPid.setType(GecMessage.READ_OUTPID);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readInPid:
                send(new GecMessage(GecMessage.READ_INPID));
                break;
            case R.id.readOutPid:
                send(new GecMessage(GecMessage.READ_OUTPID));
                break;
            case R.id.writeInPid:
                send(inPid);
                break;
            case R.id.writeOutPid:
                send(outPid);
                break;
        }
    }
}
