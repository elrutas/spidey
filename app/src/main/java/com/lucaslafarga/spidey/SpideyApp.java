package com.lucaslafarga.spidey;

import android.app.Application;

import com.lucaslafarga.spidey.di.components.ApplicationComponent;
import com.lucaslafarga.spidey.di.components.DaggerApplicationComponent;
import com.lucaslafarga.spidey.di.modules.MarvelApiModule;

public class SpideyApp extends Application {
    ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .marvelApiModule(new MarvelApiModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
