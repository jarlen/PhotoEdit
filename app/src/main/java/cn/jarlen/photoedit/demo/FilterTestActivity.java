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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;

import cn.jarlen.photoedit.filters.NativeFilter;

public class FilterTestActivity extends Activity {

    ImageView img;
    Bitmap bitSrc;
    Bitmap bitDest;
    int srcWidth,srcHeight;

    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_test);
        img = (ImageView) findViewById(R.id.filter_test);
        pbar = (ProgressBar) findViewById(R.id.pbar);

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true ;
        option.inSampleSize = 1;

        File imgPath = new File("/mnt/sdcard/girl2.jpg");

        if(!imgPath.exists()){
            return;
        }


        bitSrc = BitmapFactory.decodeFile("/mnt/sdcard/girl2.jpg");
        srcWidth = bitSrc.getWidth();
        srcHeight = bitSrc.getHeight();
        img.setImageBitmap(bitSrc);

//        new FilterTask().execute();

        pbar.setVisibility(View.VISIBLE);

        int[] pixs = new int[srcWidth * srcHeight];
        bitSrc.getPixels(pixs, 0, srcWidth, 0, 0, srcWidth, srcHeight);

        NativeFilter nativeFilter = new NativeFilter();
        int[] dataResult = nativeFilter.nostalgic(pixs, srcWidth, srcHeight, 10);

        bitDest = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                Bitmap.Config.ARGB_8888);


        pbar.setVisibility(View.GONE);
        img.setImageBitmap(bitDest);
    }


    private class FilterTask extends AsyncTask<Object, View, Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            pbar.setVisibility(View.GONE);
            img.setImageBitmap(bitDest);
        }

        @Override
        protected Object doInBackground(Object... params) {

            int[] pixs = new int[srcWidth * srcHeight];
            bitSrc.getPixels(pixs, 0, srcWidth, 0, 0, srcWidth, srcHeight);

            NativeFilter nativeFilter = new NativeFilter();
            int[] dataResult = nativeFilter.gray(pixs, srcWidth, srcHeight, 1);

            bitDest = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                    Bitmap.Config.ARGB_8888);
            return null;
        }
    }

}
