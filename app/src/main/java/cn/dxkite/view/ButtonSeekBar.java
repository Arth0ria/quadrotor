package cn.dxkite.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import cn.dxkite.quadrotor.R;

/**
 * 带按钮的进度条
 * @author DXkite
 */
public class ButtonSeekBar extends LinearLayout {

    int progress = 0;
    int max = 100;
    int size =10;

    String namespace ="http://schemas.android.com/apk/res/android";
    Button down;
    Button up;
    SeekBar seekBar;
    SeekBar.OnSeekBarChangeListener changeListener;
    View layout;

    public ButtonSeekBar(Context context) {
        super(context);
        layout =  LayoutInflater.from(context).inflate(R.layout.button_seekbar,null);
        init();
    }

    public ButtonSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        layout =  LayoutInflater.from(context).inflate(R.layout.button_seekbar,null);
        progress = attrs.getAttributeIntValue(namespace,"progress",progress);
        max = attrs.getAttributeIntValue(namespace,"max",max);
        init();
    }

    public void setChangeListener(SeekBar.OnSeekBarChangeListener listener)
    {
        changeListener = listener;
    }

    public void setMax(int max) {
        this.max = max;
        seekBar.setMax(max);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        seekBar.setProgress(progress);
    }

    public void init() {
        setOrientation(HORIZONTAL);

        seekBar = layout.findViewById(R.id.seekBar);
        down = layout.findViewById(R.id.down);
        up = layout.findViewById(R.id.up);;

        seekBar.setMax(max);
        seekBar.setProgress(progress);

        up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progress < max) {
                    progress += size;
                    seekBar.setProgress(progress);
                    if (changeListener != null)
                        changeListener.onProgressChanged(seekBar, progress, true);
                }
            }
        });

        down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progress >= 0) {
                    progress -= size;
                    seekBar.setProgress(progress);
                    if (changeListener != null)
                        changeListener.onProgressChanged(seekBar, progress, true);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ButtonSeekBar.this.progress = progress;
                if (changeListener != null)
                    changeListener.onProgressChanged(seekBar, progress, fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (changeListener != null)
                    changeListener.onStartTrackingTouch(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (changeListener != null)
                    changeListener.onStopTrackingTouch(seekBar);
            }
        });
        layout.setLayoutParams( new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT)
        );
        addView(layout);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
