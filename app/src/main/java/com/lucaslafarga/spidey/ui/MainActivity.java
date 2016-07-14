package com.lucaslafarga.spidey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private int comicTotalCount;

    private Subscription apiSubscription;

    private Subscriber<ComicDataWrapper> firstCallSubscriber = new Subscriber<ComicDataWrapper>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Error:" + e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onNext(ComicDataWrapper comicDataWrapper) {
            comicTotalCount = comicDataWrapper.data.total;
            addToAdapter(comicDataWrapper.data.comicList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SpideyApp) getApplication()).getComponent().inject(this);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setGridView();

        Observable<ComicDataWrapper> comicList = marvelApi.getComicsResponseData(0);

        apiSubscription = comicList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(firstCallSubscriber);
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
        if (offset >= comicTotalCount) {
            Log.i(TAG, "No more comics to load");
            Toast.makeText(this, R.string.no_comics, Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "Load more data from:" + offset);
        Observable<ComicDataWrapper> comicList = marvelApi.getComicsResponseData(offset);

        apiSubscription = comicList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ComicDataWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Load more completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error:" + e.getMessage());
                        e.printStackTrace();
                        loadMoreDataFromApi(offset);
                    }

                    @Override
                    public void onNext(ComicDataWrapper comicDataWrapper) {
                        Log.d(TAG, "Load more data received");
                        addToAdapter(comicDataWrapper.data.comicList);
                        comicTotalCount = comicDataWrapper.data.total;
                    }
                });
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

    @Override
    protected void onPause() {
        super.onPause();
        if (apiSubscription != null) {
            apiSubscription.unsubscribe();
        }
    }
}
