package com.lucaslafarga.spidey;


import com.lucaslafarga.spidey.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MarvelApiModule.class})
public interface ApplicationComponent {

    // allow to inject into MainActivity
    // method name not important
    void inject(MainActivity mainActivity);
}
