package cn.dxkite.quadrotor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.dxkite.quadrotor.R;
import cn.dxkite.quadrotor.model.ControllerModel;
import cn.dxkite.view.ButtonSeekBar;

public class ControllerListAdapter extends ArrayAdapter<ControllerModel> {

    int resource;
    OnChangeListener changeListener;
    ControllerModel[] models;

    public ControllerListAdapter(@NonNull Context context, int resource, @NonNull ControllerModel[] objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.models = objects;
    }

    public interface OnChangeListener {
        void change(SeekBar seekBar, int progress, String name, int id, int position);
    }

    public void setChangeListener(OnChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ControllerModel model = getItem(position);
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(getContext()).inflate(this.resource, null);

        TextView name= view.findViewById(R.id.adapterName);
        final TextView show =  view.findViewById(R.id.adapterValue);
        ButtonSeekBar seekBar =  view.findViewById(R.id.adapterSeekbar);

        seekBar.setMax(model.getMax());
        seekBar.setProgress(model.getProgress());

        show.setText(String.valueOf(model.getProgress()));

        seekBar.setChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                model.setProgress(i);
                show.setText(String.valueOf(model.getProgress()));
                changeListener.change(seekBar, i, model.getName(), model.getId(), position);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        name.setText(model.getName());
        this.models[position] = model;
        return view;
    }
    @Nullable
    @Override
    public ControllerModel getItem(int position) {
        return this.models[position];
    }
}
