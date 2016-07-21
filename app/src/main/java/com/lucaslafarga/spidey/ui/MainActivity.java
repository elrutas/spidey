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
import com.lucaslafarga.spidey.adapters.ComicListAdapter;
import com.lucaslafarga.spidey.adapters.EndlessRecyclerViewScrollListener;
import com.lucaslafarga.spidey.databinding.ActivityMainBinding;
import com.lucaslafarga.spidey.di.components.DaggerMainActivityComponent;
import com.lucaslafarga.spidey.di.modules.MainActivityModule;
import com.lucaslafarga.spidey.model.entities.Comic;
import com.lucaslafarga.spidey.presenters.MainActivityPresenter;
import com.lucaslafarga.spidey.widgets.AutofitGridRecyclerView;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ComicListAdapter.MainActivityInterface {
    private static final String TAG = MainActivity.class.getName();

    private ActivityMainBinding viewBinding;

    private ComicListAdapter listAdapter;

    @Inject
    public MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure to set style again before calling super.onCreate
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeInjector();
        setGridView();

        presenter.init();
    }

    private void initializeInjector() {
        DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
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
                presenter.getMoreComics(totalItemsCount);
            }
        });
    }

    public void addToAdapter(List<Comic> list) {
        listAdapter.addComics(list);
    }

    public void showToast(int id) {
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
