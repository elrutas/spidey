package com.lucaslafarga.spidey.presenters;

import android.util.Log;

import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.SpideyApp;
import com.lucaslafarga.spidey.models.Comic;
import com.lucaslafarga.spidey.models.ComicDataWrapper;
import com.lucaslafarga.spidey.rest.MarvelApi;
import com.lucaslafarga.spidey.ui.MainActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenter {
    private static final String TAG = MainActivityPresenter.class.getName();

    @Inject
    MarvelApi marvelApi;

    private MainActivity activity;
    private Subscription apiSubscription;
    private int retryCount;

    public MainActivityPresenter (MainActivity activity) {
        this.activity = activity;
        ( (SpideyApp) activity.getApplication()).getComponent().inject(this);
    }

    public void init() {
        ArrayList<Comic> cachedComics = marvelApi.getCachedComics();

        if(cachedComics.isEmpty()) {
            Log.d(TAG, "Initial load from api");
            getMoreComics(0);
        } else {
            Log.d(TAG, "Loaded from cache");
            activity.addToAdapter(cachedComics);
        }
    }

    public void getMoreComics(final int offset) {
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
                            getMoreComics(offset);
                        } else {
                            activity.showToast(R.string.error_loading);
                        }
                    }

                    @Override
                    public void onNext(ComicDataWrapper comicDataWrapper) {
                        Log.d(TAG, "Load more data received");
                        if (comicDataWrapper.data.count == 0) {
                            activity.showToast(R.string.no_comics);
                        }

                        activity.addToAdapter(comicDataWrapper.data.comicList);
                    }
                });
    }

    public void onDestroy() {
        if (apiSubscription != null) {
            apiSubscription.unsubscribe();
        }
    }
}
