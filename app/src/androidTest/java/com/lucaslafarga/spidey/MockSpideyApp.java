package com.lucaslafarga.spidey;

import com.lucaslafarga.spidey.di.MockMarvelApiModule;
import com.lucaslafarga.spidey.di.components.ApplicationComponent;
import com.lucaslafarga.spidey.ui.DaggerMainActivityTest_TestComponent;

public class MockSpideyApp extends SpideyApp {
    @Override
    protected ApplicationComponent createComponent() {
        return DaggerMainActivityTest_TestComponent
                .builder()
                .mockMarvelApiModule(new MockMarvelApiModule())
                .build();
    }
}