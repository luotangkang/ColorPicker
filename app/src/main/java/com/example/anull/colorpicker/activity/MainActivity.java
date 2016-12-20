package com.example.anull.colorpicker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.anull.colorpicker.R;
import com.example.anull.colorpicker.view.ColorPicker;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by null on 2016/12/20.
 */

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.colorPicker)
    ColorPicker colorPicker;
    @InjectView(R.id.activity_main)
    ConstraintLayout activity_main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        colorPicker.setOnColorSelectListener(new ColorPicker.OnColorSelectListener() {
            @Override
            public void onStartSelect(int color) {

            }

            @Override
            public void onColorSelect(int color) {
                activity_main.setBackgroundColor(color);
            }

            @Override
            public void onStopSelect(int color) {

            }
        });
    }

}
