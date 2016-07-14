package com.lucaslafarga.spidey;

import com.lucaslafarga.spidey.rest.MarvelApi;

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
