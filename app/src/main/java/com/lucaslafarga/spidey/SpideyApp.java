package com.lucaslafarga.spidey;

import android.app.Application;

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
