package com.lucaslafarga.spidey.di.modules;

import com.lucaslafarga.spidey.presenters.MainActivityPresenter;
import com.lucaslafarga.spidey.ui.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    private final MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    MainActivityPresenter provideMainActivityPresenter() {
        return new MainActivityPresenter(mainActivity);
    }
}
