package com.lucaslafarga.spidey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.adapters.ComicListAdapter;
import com.lucaslafarga.spidey.databinding.ActivityMainBinding;
import com.lucaslafarga.spidey.models.Comic;
import com.lucaslafarga.spidey.models.ComicDataWrapper;
import com.lucaslafarga.spidey.rest.MarvelApi;
import com.lucaslafarga.spidey.widgets.AutofitGridRecyclerView;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ComicListAdapter.MainActivityInterface {
    private static final String TAG = MainActivity.class.getName();

    private MarvelApi marvelApi;
    private ActivityMainBinding viewBinding;

    private List<Comic> comicList;
    private ComicListAdapter listAdapter;

    private Action1<? super ComicDataWrapper> listReceivedAction = new Action1<ComicDataWrapper>() {
        @Override
        public void call (ComicDataWrapper comics) {
            comicList = comics.data.comicList;
            addToAdapter(comics.data.comicList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setGridView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        marvelApi = new MarvelApi(getApplicationContext());

        Observable<ComicDataWrapper> comicList = marvelApi.getComicsResponseData();

        comicList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listReceivedAction);
    }

    private void setGridView() {
        AutofitGridRecyclerView comicGridView = viewBinding.comicGrid;
        comicGridView.setHasFixedSize(true);

        listAdapter = new ComicListAdapter(this);
        comicGridView.setAdapter(listAdapter);
    }

    private void addToAdapter(List<Comic> list) {
        listAdapter.addComics(list);
    }

    @Override
    public void itemClicked(Comic comic) {
        Log.d(TAG, "Clicked on " + comic.title);
        Gson gson = new Gson();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.COMIC_KEY, gson.toJson(comic));
        startActivity(intent);
    }
}
