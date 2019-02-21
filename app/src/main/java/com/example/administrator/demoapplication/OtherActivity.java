package com.example.administrator.demoapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import cn.icheny.transition.CySharedElementTransition;

public class OtherActivity extends AppCompatActivity {
    private String background;
    private ImageView imageViewB;
    private String IMGurl;
    private static String text;
    private TextToSpeech textToSpeech;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"Range", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        getWindow().setSharedElementEnterTransition(new ChangeBounds().setDuration(1500));

        Intent intent=getIntent();
        text=intent.getStringExtra("text");
        background=intent.getStringExtra("background");
        IMGurl=intent.getStringExtra("IMGurl");




        imageViewB=findViewById(R.id.imageView2);
        LinearLayout linearLayout=findViewById(R.id.other);
        Glide.with(this).load("/sdcard/dengdai/"+background).listener
                (new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new ViewTarget<View, Drawable>(linearLayout) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        this.view.setBackground(resource);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                });
        Glide.with(this).load(IMGurl).into(imageViewB);

        findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注意这里不使用finish
                ActivityCompat.finishAfterTransition(OtherActivity.this);

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityCompat.finishAfterTransition(OtherActivity.this);
            }
        },15000);
    }


}
