package com.lucaslafarga.spidey.ui;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.models.Comic;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<DetailActivity> activityRule = new ActivityTestRule<>(
            DetailActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Test
    public void titleAndDescriptionAreDisplayed() {
        String myTitle = "MyTestTitle";
        String myDescription = "MyTestDescription";

        Comic comic = new Comic();
        comic.title = myTitle;
        comic.description = myDescription;
        Gson gson = new Gson();

        Intent intent = new Intent();
        intent.putExtra(DetailActivity.COMIC_KEY, gson.toJson(comic));

        activityRule.launchActivity(intent);

        onView(withId(R.id.detail_title)).check(matches(withText(myTitle)));
        onView(withId(R.id.detail_description)).check(matches(withText(myDescription)));
    }
}
