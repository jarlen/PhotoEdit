package cn.jarlen.photoedit.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ImageCropActivity extends Activity implements Toolbar.OnMenuItemClickListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_mosaic);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle("剪切");


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
