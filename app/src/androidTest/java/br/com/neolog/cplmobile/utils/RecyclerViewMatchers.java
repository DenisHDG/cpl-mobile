package br.com.neolog.cplmobile.utils;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewMatchers
{
    private RecyclerViewMatchers()
    {
        throw new AssertionError();
    }

    public static Matcher<View> adapterIsEmpty()
    {
        return hasItemCount( 0 );
    }

    public static Matcher<View> hasItemCount(
        final int count )
    {
        return itemCount( equalTo( count ) );
    }

    public static Matcher<View> itemCount(
        final Matcher<Integer> matcher )
    {
        return new BoundedMatcher<View,RecyclerView>( RecyclerView.class ) {
            @Override
            protected boolean matchesSafely(
                final RecyclerView item )
            {
                return matcher.matches( getItemCount( item ) );
            }

            @Override
            public void describeTo(
                final Description description )
            {
                description.appendText( "item count: " );
                matcher.describeTo( description );
            }
        };
    }

    private static int getItemCount(
        final View view )
    {
        final RecyclerView.Adapter adapter = ( (RecyclerView) view ).getAdapter();
        return requireNonNull( adapter, "adapter of recycler view " + view + "is null" )
            .getItemCount();
    }
}
