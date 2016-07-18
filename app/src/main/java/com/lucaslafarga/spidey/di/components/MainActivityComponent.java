package com.lucaslafarga.spidey.di.components;

import com.lucaslafarga.spidey.di.modules.MainActivityModule;
import com.lucaslafarga.spidey.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainActivityModule.class})
public interface MainActivityComponent {
    // allow to inject into MainActivity
    // method name not important
    void inject(MainActivity mainActivity);
}
