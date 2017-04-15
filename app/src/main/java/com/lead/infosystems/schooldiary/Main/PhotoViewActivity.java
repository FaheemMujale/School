package com.lead.infosystems.schooldiary.Main;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.lead.infosystems.schooldiary.R;
import com.squareup.picasso.Picasso;

public class PhotoViewActivity extends AppCompatActivity {

    public static final String IMAGE_PATH = "image_path";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photo_view);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        String path = getIntent().getExtras().getString(IMAGE_PATH);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        Picasso.with(getApplicationContext()).load(path).into(photoView);
    }
}
