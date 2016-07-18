package com.lucaslafarga.spidey;

import com.lucaslafarga.spidey.presenters.MainActivityPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MarvelApiModule.class})
public interface ApplicationComponent {

    // allow to inject into MainActivityPresenter
    // method name not important
    void inject(MainActivityPresenter mainActivityPresenter);
}
