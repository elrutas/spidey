package com.lucaslafarga.spidey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.SpideyApp;
import com.lucaslafarga.spidey.adapters.ComicListAdapter;
import com.lucaslafarga.spidey.adapters.EndlessRecyclerViewScrollListener;
import com.lucaslafarga.spidey.databinding.ActivityMainBinding;
import com.lucaslafarga.spidey.models.Comic;
import com.lucaslafarga.spidey.models.ComicDataWrapper;
import com.lucaslafarga.spidey.rest.MarvelApi;
import com.lucaslafarga.spidey.widgets.AutofitGridRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ComicListAdapter.MainActivityInterface {
    private static final String TAG = MainActivity.class.getName();

    @Inject
    MarvelApi marvelApi;

    private ActivityMainBinding viewBinding;

    private ComicListAdapter listAdapter;

    private Subscription apiSubscription;
    private int retryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SpideyApp) getApplication()).getComponent().inject(this);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setGridView();

        ArrayList<Comic> cachedComics = marvelApi.getCachedComics();

        if(cachedComics.isEmpty()) {
            Log.d(TAG, "Initial load from api");
            loadMoreDataFromApi(0);
        } else {
            Log.d(TAG, "Loaded from cache");
            addToAdapter(cachedComics);
        }
    }

    private void setGridView() {
        AutofitGridRecyclerView comicGridView = viewBinding.comicGrid;
        comicGridView.setHasFixedSize(true);

        listAdapter = new ComicListAdapter(this);
        comicGridView.setAdapter(listAdapter);

        viewBinding.comicGrid.addOnScrollListener(new EndlessRecyclerViewScrollListener(viewBinding.comicGrid.getManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "Load more. Page:" + page + ", totalItem:" + totalItemsCount);
                loadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    private void loadMoreDataFromApi(final int offset) {
        Log.d(TAG, "Load more data from:" + offset);
        Observable<ComicDataWrapper> comicList = marvelApi.getComicsResponseData(offset);

        apiSubscription = comicList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ComicDataWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Load more completed");
                        retryCount = 0;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error:" + e.getMessage());
                        e.printStackTrace();
                        if(retryCount < 4) {
                            retryCount++;
                            loadMoreDataFromApi(offset);
                        } else {
                            showToast(R.string.error_loading);
                        }
                    }

                    @Override
                    public void onNext(ComicDataWrapper comicDataWrapper) {
                        Log.d(TAG, "Load more data received");
                        if (comicDataWrapper.data.count == 0) {
                            showToast(R.string.no_comics);
                        }

                        addToAdapter(comicDataWrapper.data.comicList);
                    }
                });
    }

    private void addToAdapter(List<Comic> list) {
        listAdapter.addComics(list);
    }

    private void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }

    @Override
    public void itemClicked(View view, Comic comic) {
        Log.d(TAG, "Clicked on " + comic.title);
        Gson gson = new Gson();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.COMIC_KEY, gson.toJson(comic));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                new Pair<View, String>(view.findViewById(R.id.comic_title),
                        getString(R.string.tran_name_title))
        );
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (apiSubscription != null) {
            apiSubscription.unsubscribe();
        }

    }
}
