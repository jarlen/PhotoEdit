/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.jarlen.photoedit.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import cn.jarlen.photoedit.mosaic.DrawMosaicView;
import cn.jarlen.photoedit.mosaic.MosaicUtil;
import cn.jarlen.photoedit.utils.FileUtils;

/**
 * 马赛克
 * @author jarlen
 */
public class MosaicActivity extends Activity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private Toolbar mToolbar;

    private DrawMosaicView mosaic;

    String mPath;
    private int mWidth, mHeight;

    Bitmap srcBitmap = null;

    private ImageButton cancelBtn, okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mosaic);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_mosaic);
        mToolbar.setOnMenuItemClickListener(this);
        initView();

        Intent intent = getIntent();
        mPath = intent.getStringExtra("camera_path");
        mosaic.setMosaicBackgroundResource(mPath);

        srcBitmap = BitmapFactory.decodeFile(mPath);

        mWidth = srcBitmap.getWidth();
        mHeight = srcBitmap.getHeight();
        Bitmap bit = MosaicUtil.getMosaic(srcBitmap);

        mosaic.setMosaicResource(bit);
        mosaic.setMosaicBrushWidth(10);
    }

    private void initView()
    {
        mosaic = (DrawMosaicView) findViewById(R.id.mosaic);

        cancelBtn = (ImageButton) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        okBtn = (ImageButton) findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);

    }

    int size = 5;
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_base:
                Bitmap bitmapMosaic = MosaicUtil.getMosaic(srcBitmap);
                mosaic.setMosaicResource(bitmapMosaic);
                break;
            case R.id.action_ground_glass:
                Bitmap bitmapBlur = MosaicUtil.getBlur(srcBitmap);
                mosaic.setMosaicResource(bitmapBlur);
                break;
            case R.id.action_flower:
                Bitmap bit = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.hi4);
                bit = FileUtils.ResizeBitmap(bit, mWidth, mHeight);
                mosaic.setMosaicResource(bit);
                break;
            case R.id.action_size:
                if (size >= 30)
                {
                    size = 5;
                } else
                {
                    size += 5;
                }
                mosaic.setMosaicBrushWidth(size);
                break;
            case R.id.action_eraser:
                mosaic.setMosaicType(MosaicUtil.MosaicType.ERASER);
                break;
            default:
                break;
        }


        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_cancel:
                Intent cancelData = new Intent();
                setResult(RESULT_CANCELED, cancelData);

                recycle();
                this.finish();
                break;
            case R.id.btn_ok:
                Bitmap bit = mosaic.getMosaicBitmap();

                FileUtils.writeImage(bit, mPath, 100);

                Intent okData = new Intent();
                okData.putExtra("camera_path", mPath);
                setResult(RESULT_OK, okData);
                recycle();
                MosaicActivity.this.finish();
                break;
            default:

                break;
        }
    }

    private void recycle()
    {
        if (srcBitmap != null)
        {
            srcBitmap.recycle();
            srcBitmap = null;
        }
    }
}
