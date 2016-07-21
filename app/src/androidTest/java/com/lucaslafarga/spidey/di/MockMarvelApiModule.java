package com.lucaslafarga.spidey.di;

import com.lucaslafarga.spidey.model.rest.MarvelApi;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockMarvelApiModule {
    @Provides
    @Singleton
    MarvelApi provideMockMarvelApi() {
        return Mockito.mock(MarvelApi.class);
    }
}
