package com.larswerkman.holocolorpicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorSelectedListener;

import cn.jarlen.photoedit.demo.R;

public class SelectColorPopup extends PopupWindow implements OnColorChangedListener, OnColorSelectedListener{


    private View mMenuView;
    private ColorPicker picker;

    public int getColor()
    {
        return picker.getColor();
    }

    public SelectColorPopup(Activity context,OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_color, null);
        picker = (ColorPicker) mMenuView.findViewById(R.id.picker);
        SVBar svBar = (SVBar) mMenuView.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) mMenuView.findViewById(R.id.opacitybar);
        SaturationBar saturationBar = (SaturationBar) mMenuView.findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) mMenuView.findViewById(R.id.valuebar);
        Button  submit = (Button) mMenuView.findViewById(R.id.submit);
        submit.setOnClickListener(itemsOnClick);
        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        //To get the color
        picker.getColor();

        //To set the old selected color u can do it like this
        picker.setOldCenterColor(picker.getColor());
        // adds listener to the colorpicker which is implemented
        //in the activity
        picker.setOnColorChangedListener(this);
        picker.setOnColorSelectedListener(this);
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    @Override
    public void onColorChanged(int color)
    {
    }

    @Override
    public void onColorSelected(int color)
    {
    }

}