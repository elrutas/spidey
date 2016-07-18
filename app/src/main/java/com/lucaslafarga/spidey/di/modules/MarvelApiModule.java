package com.lucaslafarga.spidey.di.modules;

import com.lucaslafarga.spidey.SpideyApp;
import com.lucaslafarga.spidey.model.rest.MarvelApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MarvelApiModule {
    private final SpideyApp app;

    public MarvelApiModule(SpideyApp app) {
        this.app = app;

    }

    @Provides
    @Singleton
    MarvelApi provideMarvelApi() {
        return new MarvelApi(app);
    }
}
