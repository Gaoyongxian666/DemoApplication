package com.example.administrator.demoapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.chad.library.adapter.base.BaseViewHolder;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class HomeAdapter extends BaseQuickAdapter<HomeItem, BaseViewHolder> {
    public HomeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItem item) {
        Glide.with(mContext).load(item.getimageUrl()).into((ImageView) helper.getView(R.id.icon));
    }
}
