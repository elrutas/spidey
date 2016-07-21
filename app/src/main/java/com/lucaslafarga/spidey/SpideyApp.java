package com.lucaslafarga.spidey;

import android.app.Application;

import com.lucaslafarga.spidey.di.components.ApplicationComponent;
import com.lucaslafarga.spidey.di.components.DaggerApplicationComponent;
import com.lucaslafarga.spidey.di.modules.MarvelApiModule;

public class SpideyApp extends Application {
    private final ApplicationComponent component = createComponent();

    protected ApplicationComponent createComponent() {
        return DaggerApplicationComponent.builder()
                .marvelApiModule(new MarvelApiModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
