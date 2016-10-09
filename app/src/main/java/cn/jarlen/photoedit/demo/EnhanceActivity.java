package cn.jarlen.photoedit.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import cn.jarlen.photoedit.enhance.PhotoEnhance;
import cn.jarlen.photoedit.utils.FileUtils;

public class EnhanceActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    private ImageButton cancelBtn, okBtn;

    private ImageView pictureShow;

    private SeekBar saturationSeekBar, brightnessSeekBar, contrastSeekBar;

    private String imgPath;
    private Bitmap bitmapSrc;

    private PhotoEnhance pe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_enhance);

        Intent intent = getIntent();

        imgPath = intent.getStringExtra("camera_path");
        bitmapSrc = BitmapFactory.decodeFile(imgPath);

        initView();
        pictureShow.setImageBitmap(bitmapSrc);
    }

    private void initView()
    {
        cancelBtn = (ImageButton) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        okBtn = (ImageButton) findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);

        pictureShow = (ImageView) findViewById(R.id.enhancePicture);

        saturationSeekBar = (SeekBar) findViewById(R.id.saturation);
        saturationSeekBar.setMax(255);
        saturationSeekBar.setProgress(128);
        saturationSeekBar.setOnSeekBarChangeListener(this);

        brightnessSeekBar = (SeekBar) findViewById(R.id.brightness);
        brightnessSeekBar.setMax(255);
        brightnessSeekBar.setProgress(128);
        brightnessSeekBar.setOnSeekBarChangeListener(this);

        contrastSeekBar = (SeekBar) findViewById(R.id.contrast);
        contrastSeekBar.setMax(255);
        contrastSeekBar.setProgress(128);
        contrastSeekBar.setOnSeekBarChangeListener(this);

        pe = new PhotoEnhance(bitmapSrc);

    }

    private int pregress = 0;
    private Bitmap bit = null;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser)
    {

        pregress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        // TODO Auto-generated method stub

        int type = 0;

        switch (seekBar.getId())
        {
            case R.id.saturation :
                pe.setSaturation(pregress);
                type = pe.Enhance_Saturation;

                break;
            case R.id.brightness :
                pe.setBrightness(pregress);
                type = pe.Enhance_Brightness;

                break;

            case R.id.contrast :
                pe.setContrast(pregress);
                type = pe.Enhance_Contrast;

                break;

            default :
                break;
        }

        bit = pe.handleImage(type);
        pictureShow.setImageBitmap(bit);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.btn_ok :

                FileUtils.writeImage(bit, imgPath, 100);
                Intent okData = new Intent();
                okData.putExtra("camera_path", imgPath);
                setResult(RESULT_OK, okData);
                recycle();
                this.finish();
                break;

            case R.id.btn_cancel :

                Intent cancelData = new Intent();
                setResult(RESULT_CANCELED, cancelData);
                recycle();
                this.finish();
                break;

            default :
                break;
        }

    }

    private void recycle()
    {
        if (bitmapSrc != null)
        {
            bitmapSrc.recycle();
            bitmapSrc = null;
        }

        if (bit != null)
        {
            bit.recycle();
            bit = null;
        }
    }
}
