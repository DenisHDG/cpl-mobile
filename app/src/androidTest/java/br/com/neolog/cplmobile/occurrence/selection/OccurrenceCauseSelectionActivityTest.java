package br.com.neolog.cplmobile.occurrence.selection;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.neolog.cplmobile.utils.RecyclerViewMatchers.adapterIsEmpty;
import static br.com.neolog.cplmobile.utils.RecyclerViewMatchers.hasItemCount;
import static br.com.neolog.cplmobile.utils.RecyclerViewMatchers.itemCount;
import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.occurrence.NewOccurrenceActivity;

@RunWith( AndroidJUnit4.class )
public class OccurrenceCauseSelectionActivityTest
{
    @Rule
    public IntentsTestRule<OccurrenceCauseSelectionActivity> intentsTestRule = new IntentsTestRule<>(
        OccurrenceCauseSelectionActivity.class );

    @Test
    public void shouldLoadActivity()
    {
        assertThat( intentsTestRule.getActivity() ).isNotNull();
    }

    @Test
    public void shouldDisplayRecyclerView()
    {
        onView( withId( R.id.activity_report_occurrence_recycler_view ) ).check( matches( isDisplayed() ) );
    }

    @Test
    public void shouldDisplayItemsWhenRecyclerViewHasItems()
    {
        onView( withId( R.id.activity_report_occurrence_recycler_view ) ).check( matches( itemCount( greaterThan( 0 ) ) ) );
    }

    @Test
    public void shouldNavigateToNewOccurrenceActivityWhenItemClicked()
    {
        onView( withId( R.id.activity_report_occurrence_recycler_view ) ).perform( actionOnItemAtPosition( 0, click() ) );
        intended( hasComponent( NewOccurrenceActivity.class.getName() ) );
        intended( hasExtra( equalTo( "causeId" ), any( Integer.class ) ) );
    }

    @Test
    public void shouldSearchCausesByName()
    {
        onView( withId( R.id.activity_menu_toolbar_search ) ).perform( click() );
        onView( withId( R.id.search_src_text ) ).perform( typeText( "acidente" ) );
        onView( withId( R.id.activity_report_occurrence_recycler_view ) ).check( matches( hasItemCount( 1 ) ) );
    }

    @Test
    public void shouldClearAdapterWhenSearchReturnsNoValues()
    {
        onView( withId( R.id.activity_menu_toolbar_search ) ).perform( click() );
        onView( withId( R.id.search_src_text ) ).perform( typeText( "lafhkjadfhjkashd" ) );
        onView( withId( R.id.activity_report_occurrence_recycler_view ) ).check( matches( adapterIsEmpty() ) );
    }
}