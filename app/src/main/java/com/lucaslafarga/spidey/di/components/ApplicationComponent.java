package com.lucaslafarga.spidey.di.components;

import com.lucaslafarga.spidey.di.modules.MarvelApiModule;
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
