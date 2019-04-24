package com.genius.imageloader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.genius.R;

public class ImageLoaderActivity extends AppCompatActivity {
    private ImageView mImageView;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ImageLoaderActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);

        mImageView = findViewById(R.id.imageView);

//        GlideLoader mGlideLoader = new GlideLoader();
//        mGlideLoader.loadImage(this,"http://pic75.nipic.com/file/20150821/9448607_145742365000_2.jpg",mImageView);
        String url = "http://pic75.nipic.com/file/20150821/9448607_145742365000_2.jpg";
//        BaseImageLoaderOptions imageLoaderOptions = new BaseImageLoaderOptions().setPlaceholderResId(R.mipmap.ic_launcher).setCenterCrop(true).setBitmapAngle(60);
        ImageLoader.getInstance().loadImage(this,
                mImageView,
                ImageLoaderOptions.builder(url).setBitmapAngle(60).setPlaceholderResId(R.mipmap.ic_launcher).setCircleCrop(true).build());
    }
}
