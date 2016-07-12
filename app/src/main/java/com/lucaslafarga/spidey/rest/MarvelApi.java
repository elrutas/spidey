package com.lucaslafarga.spidey.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.Utils.CreateHash;
import com.lucaslafarga.spidey.models.GetAllComicsResponse;

import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarvelApi {

    private final Retrofit mRetrofit;
    private final String mApiPrivateKey;
    private final String mApiPublicKey;
    private final String mBaseUrl;
    private final MarvelApiInterface apiService;
    private final Long mTimestamp;
    private final String mHash;

    public MarvelApi(Context context) {

        mApiPrivateKey = context.getString(R.string.private_key);
        mApiPublicKey = context.getString(R.string.public_key);
        mBaseUrl = "http://gateway.marvel.com";

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        mTimestamp = calendar.getTimeInMillis()/ 1000L;

        mHash = CreateHash.md5(String.valueOf(mTimestamp)  + mApiPrivateKey + mApiPublicKey);

        GsonBuilder gBuilder = new GsonBuilder();
        gBuilder.excludeFieldsWithoutExposeAnnotation();
        gBuilder.disableHtmlEscaping();
        Gson gson = gBuilder.create();

        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl).build();


        apiService = mRetrofit.create(MarvelApiInterface.class);
    }

    public Observable<GetAllComicsResponse> getComicsResponseData() {

        Observable<GetAllComicsResponse> observable;

        observable = apiService.getMarvelComicsData("1009220", String.valueOf(mTimestamp), mApiPublicKey, mHash);

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}