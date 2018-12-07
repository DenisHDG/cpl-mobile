package br.com.neolog.cplmobile;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

@RunWith( AndroidJUnit4.class )
public class MainActivityTest
{
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>( MainActivity.class );

    @Test
    public void shouldInstantiateMainActivity()
    {
    }
}
