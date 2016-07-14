package com.lucaslafarga.spidey.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.models.Comic;
import com.lucaslafarga.spidey.utils.CreateHash;
import com.lucaslafarga.spidey.models.ComicDataWrapper;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarvelApi {
    private static final String CAPTAIN_AMERICA = "1009220";
    private static final String SPIDEY = "1009610";
    private static final int RESULT_AMOUNT = 50;

    private final Retrofit mRetrofit;
    private final String mApiPrivateKey;
    private final String mApiPublicKey;
    private final String mBaseUrl;
    private final MarvelApiInterface apiService;
    private final Long mTimestamp;
    private final String mHash;
    private List<Comic> comicList;

    public MarvelApi(Context context) {

        mApiPrivateKey = context.getString(R.string.private_key);
        mApiPublicKey = context.getString(R.string.public_key);
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
                mApiPublicKey, mHash, RESULT_AMOUNT, offset);

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
