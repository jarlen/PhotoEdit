package cn.jarlen.photoedit.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cn.jarlen.phototedit.filter.NativeFilter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NativeFilter nativeFilter = new NativeFilter();

        String test = nativeFilter.test();

        ((TextView)findViewById(R.id.test)).setText(test);
    }
}
