package com.lucaslafarga.spidey.ui;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.SpideyApp;
import com.lucaslafarga.spidey.di.MockMarvelApiModule;
import com.lucaslafarga.spidey.di.components.ApplicationComponent;
import com.lucaslafarga.spidey.model.entities.Comic;
import com.lucaslafarga.spidey.model.entities.Thumbnail;
import com.lucaslafarga.spidey.model.rest.MarvelApi;
import com.lucaslafarga.spidey.utils.RecyclerViewMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Inject
    MarvelApi marvelApi;

    @Singleton
    @Component(modules = MockMarvelApiModule.class)
    public interface TestComponent extends ApplicationComponent {
        void inject(MainActivityTest mainActivityTest);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        SpideyApp app = (SpideyApp) instrumentation.getTargetContext().getApplicationContext();
        TestComponent component = (TestComponent) app.getComponent();
        component.inject(this);
    }

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void comicsAreLoadedFromCacheFirst() {
        Mockito.when(marvelApi.getCachedComics()).thenReturn(sampleList());

        activityRule.launchActivity(new Intent());

        onView(withRecyclerView(R.id.comic_grid)
                .atPositionOnView(0, R.id.comic_title))
                .check(matches(withText(getSampleComic().title)));
    }

    private Comic getSampleComic() {
        Comic sampleComic = new Comic();
        sampleComic.title = "Sample title";
        sampleComic.description = "Sample description";
        sampleComic.thumbnail = new Thumbnail();
        sampleComic.thumbnail.path = "test.path/thumbnail";
        sampleComic.thumbnail.extension = "png";
        return  sampleComic;
    }

    private ArrayList<Comic> sampleList() {
        ArrayList<Comic> sampleList = new ArrayList<>();
        sampleList.add(getSampleComic());
        return sampleList;
    }
}
