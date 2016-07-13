package com.lucaslafarga.spidey.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.databinding.ActivityDetailBinding;
import com.lucaslafarga.spidey.models.Comic;
import com.lucaslafarga.spidey.models.ComicImage;

import java.util.Random;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    public static final String COMIC_KEY = "comic";

    private ActivityDetailBinding viewBinding;
    private Comic comic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra(COMIC_KEY);

        if(strObj == null || strObj.isEmpty())
            finish();

        comic = gson.fromJson(strObj, Comic.class);

        setTitle(comic.title == null? getString(R.string.no_title) : comic.title);
        viewBinding.detailTitle.setText(comic.title == null? getString(R.string.no_title) : comic.title);
        viewBinding.detailDescription.setText(comic.description == null? getString(R.string.no_description) : comic.description);

        loadImage();
    }

    private void loadImage() {
        if (comic.images != null && !comic.images.isEmpty()) {
            ComicImage image = comic.images.get(new Random().nextInt(comic.images.size()));
            Glide.with(this)
                .load(image.path + "." + image.extension)
                .centerCrop()
                .error(R.drawable.no_image_available)
                .into(viewBinding.detailImage);
        } else {
            Glide.with(this)
                .load(R.drawable.no_image_available)
                .fitCenter()
                .into(viewBinding.detailImage);
        }
    }
}
