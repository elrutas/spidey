package com.lucaslafarga.spidey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lucaslafarga.spidey.models.Comic;
import com.lucaslafarga.spidey.models.GetAllComicsResponse;
import com.lucaslafarga.spidey.rest.MarvelApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private MarvelApi marvelApi;

    private Action1<? super GetAllComicsResponse> mItemReceivedAction = new Action1<GetAllComicsResponse>() {
        @Override
        public void call (GetAllComicsResponse comics) {
            Log.d(TAG, "Received list:" + comics);
            for(Comic data : comics.data.comicList) {
                Log.d(TAG, "Comic:" + data.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        marvelApi = new MarvelApi(getApplicationContext());

        Observable<GetAllComicsResponse> comicList = marvelApi.getComicsResponseData();

        comicList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mItemReceivedAction);
    }
}
