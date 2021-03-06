package com.lucaslafarga.spidey.model.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.model.entities.Comic;
import com.lucaslafarga.spidey.utils.CreateHash;
import com.lucaslafarga.spidey.model.entities.ComicDataWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MarvelApi {
    private static final String CAPTAIN_AMERICA = "1009220";
    private static final String SPIDEY = "1009610";
    private static final int RESULT_AMOUNT = 47;

    private final Retrofit mRetrofit;
    private final String mApiPrivateKey;
    private final String mApiPublicKey;
    private final String mBaseUrl;
    private final MarvelApiInterface apiService;
    private final Long mTimestamp;
    private final String mHash;
    private ArrayList<Comic> comicListCache = new ArrayList<>();

    public MarvelApi(Context context) {

        mApiPrivateKey = context.getString(R.string.private_key);
        mApiPublicKey = context.getString(R.string.public_key);

        if (mApiPrivateKey.equalsIgnoreCase(context.getString(R.string.fake_key))
            || mApiPublicKey.equalsIgnoreCase(context.getString(R.string.fake_key))) {
            throw new RuntimeException("You must set a real key in config.xml");
        }

        mBaseUrl = "http://gateway.marvel.com";

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        mTimestamp = calendar.getTimeInMillis()/ 1000L;

        mHash = CreateHash.md5(String.valueOf(mTimestamp)  + mApiPrivateKey + mApiPublicKey);

        GsonBuilder gBuilder = new GsonBuilder();
        Gson gson = gBuilder.create();

        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl).build();


        apiService = mRetrofit.create(MarvelApiInterface.class);
    }

    public Observable<ComicDataWrapper> getComicsResponseData(int offset) {

        Observable<ComicDataWrapper> observable;

        observable = apiService.getCharacterComicsData(SPIDEY, String.valueOf(mTimestamp),
                mApiPublicKey, mHash, RESULT_AMOUNT, offset).map(new Func1<ComicDataWrapper, ComicDataWrapper>() {
            @Override
            public ComicDataWrapper call(ComicDataWrapper comicDataWrapper) {
                addToCache(comicDataWrapper.data.comicList);
                return comicDataWrapper;
            }
        });

        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void addToCache(List<Comic> comics) {
        comicListCache.addAll(comics);
    }

    public ArrayList<Comic> getCachedComics() {
        return comicListCache;
    }
}
