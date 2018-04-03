package cn.dxkite.quadrotor.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.dxkite.gec.connector.GecMessage;
import cn.dxkite.quadrotor.QuadrotorService;
import cn.dxkite.quadrotor.R;

public abstract class ControlFragment extends Fragment implements QuadrotorService.OnMessageReceivedListener {
    ViewPager viewPager;
    View view;
    final static String TAG = "ControlFragment";

    QuadrotorService.QuadrotorBinder binder;

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public QuadrotorService.QuadrotorBinder getBinder() {
        return binder;
    }

    public void setBinder(QuadrotorService.QuadrotorBinder binder) {
        this.binder = binder;
        //TODO Fix Attempt to invoke virtual method 'void cn.dxkite.quadrotor.QuadrotorService$QuadrotorBinder.addMessageReceivedListener(cn.dxkite.quadrotor.QuadrotorService$OnMessageReceivedListener)' on a null object reference,
        this.binder.addMessageReceivedListener(this);
        Log.d(TAG,"binder addMessageReceivedListener");
    }

    public static ControlFragment createFragment(Class<? extends ControlFragment> fragmentClass, ViewPager viewPager) {
        ControlFragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
            fragment.setViewPager(viewPager);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    abstract public View onInitView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = onInitView(inflater,container,savedInstanceState);
        }
        return view;
    }

    public static ControlFragment createFragment(Class<? extends ControlFragment> fragmentClass, ViewPager viewPager, QuadrotorService.QuadrotorBinder binder) {
        ControlFragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
            fragment.setViewPager(viewPager);
            fragment.setBinder(binder);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public void send(GecMessage message) {
        if (binder != null) {
            binder.send(message);
        }else{
            Log.e(TAG,"send message error , binder is null");
        }
    }

    public void send(final GecMessage message, final MessageReceiver receiver) {
        new Thread(new Runnable() {
            GecMessage msg = null;
            @Override
            public void run() {
                msg = binder.sendAndReturn(message);
               view.post(new Runnable() {
                   @Override
                   public void run() {
                        receiver.onReceived(msg);
                   }
               });
            }
        }).start();
    }

    @Override
    public void onReceived(GecMessage message) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onServerConnected(String name, int address, String mac) {

    }

    @Override
    public void onError(Exception e) {

    }

    public void setPackageFrequency(int pf) {
        if (binder != null) {
            binder.setPackageFrequency(pf);
        }
    }

    public GecMessage getSimpleMessage() {
        if (binder != null) {
            if (binder.getSimple() != null) {
                return binder.getSimple();
            }
        }
        GecMessage simpleMessage = new GecMessage();
        simpleMessage.setPower(0);
        simpleMessage.setPitch(1500);
        simpleMessage.setRoll(1500);
        simpleMessage.setCourse(1500);
        return simpleMessage;
    }

    interface MessageReceiver {
        void onReceived(GecMessage message);
    }
}
