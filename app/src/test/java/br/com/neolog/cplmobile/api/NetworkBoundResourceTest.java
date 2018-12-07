package br.com.neolog.cplmobile.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.executors.CountingAppExecutors;
import br.com.neolog.cplmobile.executors.InstantAppExecutors;
import br.com.neolog.exceptionmessage.ExceptionMessages;

@RunWith( Parameterized.class )
public class NetworkBoundResourceTest
{
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Function<Foo,Void> saveCallResult;

    private Function<Foo,Boolean> shouldFetch;

    private Function<Void,LiveData<ApiResponse<Foo>>> createCall;

    private final MutableLiveData<Foo> dbData = new MutableLiveData<>();

    private NetworkBoundResource<Foo,Foo> networkBoundResource;

    private final AtomicBoolean fetchedOnce = new AtomicBoolean( false );
    private CountingAppExecutors countingAppExecutors;
    private final boolean useRealExecutors;

    @Mock
    private Observer<Resource<Foo>> observer;

    @Parameterized.Parameters
    public static List<Boolean> param()
    {
        return Arrays.asList( true, false );
    }

    public NetworkBoundResourceTest(
        final boolean useRealExecutors )
    {
        this.useRealExecutors = useRealExecutors;
        if( useRealExecutors ) {
            countingAppExecutors = new CountingAppExecutors();
        }
    }

    @Before
    public void init()
    {
        final AppExecutors appExecutors = useRealExecutors
            ? countingAppExecutors.getAppExecutors()
            : new InstantAppExecutors();
        networkBoundResource = new NetworkBoundResource<Foo,Foo>( appExecutors ) {
            @Override
            protected void saveCallResult(
                @NonNull final Foo item )
            {
                saveCallResult.apply( item );
            }

            @Override
            protected boolean shouldFetch(
                @Nullable final Foo data )
            {
                // since test methods don't handle repetitive fetching, call it only once
                return shouldFetch.apply( data ) && fetchedOnce.compareAndSet( false, true );
            }

            @NonNull
            @Override
            protected LiveData<Foo> loadFromDb()
            {
                return dbData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Foo>> createCall()
            {
                return createCall.apply( null );
            }
        };
    }

    private void drain()
    {
        if( ! useRealExecutors ) {
            return;
        }
        try {
            countingAppExecutors.drainTasks( 1, TimeUnit.SECONDS );
        } catch( final Throwable t ) {
            throw new AssertionError( t );
        }
    }

    @Test
    public void basicFromNetwork()
    {
        final AtomicReference<Foo> saved = new AtomicReference<>();
        shouldFetch = Objects::isNull;
        final Foo fetchedDbValue = new Foo( 1 );
        saveCallResult = foo -> {
            saved.set( foo );
            dbData.setValue( fetchedDbValue );
            return null;
        };
        final Foo networkResult = new Foo( 1 );
        createCall = aVoid -> ApiUtil.success( networkResult );

        final InOrder inOrder = inOrder( observer );
        networkBoundResource.asLiveData().observeForever( observer );
        drain();
        inOrder.verify( observer ).onChanged( Resource.loading( null ) );
        dbData.setValue( null );
        drain();
        assertThat( saved.get(), is( networkResult ) );
        inOrder.verify( observer ).onChanged( Resource.success( fetchedDbValue ) );
    }

    @Test
    public void failureFromNetwork()
    {
        final AtomicBoolean saved = new AtomicBoolean( false );
        shouldFetch = Objects::isNull;
        saveCallResult = foo -> {
            saved.set( true );
            return null;
        };
        final ExceptionMessages errors = ExceptionMessages.from( "error", Collections.emptyList() );
        createCall = aVoid -> ApiUtil.fail( errors );

        networkBoundResource.asLiveData().observeForever( observer );
        drain();
        verify( observer ).onChanged( Resource.loading( null ) );
        reset( observer );
        dbData.setValue( null );
        drain();
        assertThat( saved.get(), is( false ) );
        verify( observer ).onChanged( Resource.error( errors, null ) );
        verifyNoMoreInteractions( observer );
    }

    @Test
    public void dbSuccessWithoutNetwork()
    {
        final AtomicBoolean saved = new AtomicBoolean( false );
        shouldFetch = Objects::isNull;
        saveCallResult = foo -> {
            saved.set( true );
            return null;
        };

        networkBoundResource.asLiveData().observeForever( observer );
        drain();
        verify( observer ).onChanged( Resource.loading( null ) );
        reset( observer );
        final Foo dbFoo = new Foo( 1 );
        dbData.setValue( dbFoo );
        drain();
        verify( observer ).onChanged( Resource.success( dbFoo ) );
        assertThat( saved.get(), is( false ) );
        final Foo dbFoo2 = new Foo( 2 );
        dbData.setValue( dbFoo2 );
        drain();
        verify( observer ).onChanged( Resource.success( dbFoo2 ) );
        verifyNoMoreInteractions( observer );
    }

    @Test
    public void dbSuccessWithFetchFailure()
    {
        final Foo dbValue = new Foo( 1 );
        final AtomicBoolean saved = new AtomicBoolean( false );
        shouldFetch = (
            foo ) -> foo == dbValue;
        saveCallResult = foo -> {
            saved.set( true );
            return null;
        };
        final MutableLiveData<ApiResponse<Foo>> apiResponseLiveData = new MutableLiveData<>();
        createCall = aVoid -> apiResponseLiveData;

        networkBoundResource.asLiveData().observeForever( observer );
        drain();
        verify( observer ).onChanged( Resource.loading( null ) );
        reset( observer );

        dbData.setValue( dbValue );
        drain();
        verify( observer ).onChanged( Resource.loading( dbValue ) );

        final ExceptionMessages errors = ExceptionMessages.from( "error", Collections.emptyList() );
        apiResponseLiveData.setValue( new ApiResponse<Foo>( 400, null, errors ) );
        drain();
        assertThat( saved.get(), is( false ) );
        verify( observer ).onChanged( Resource.error( errors, dbValue ) );

        final Foo dbValue2 = new Foo( 2 );
        dbData.setValue( dbValue2 );
        drain();
        verify( observer ).onChanged( Resource.error( errors, dbValue2 ) );
        verifyNoMoreInteractions( observer );
    }

    @Test
    public void dbSuccessWithReFetchSuccess()
    {
        final Foo dbValue = new Foo( 1 );
        final Foo dbValue2 = new Foo( 2 );
        final AtomicReference<Foo> saved = new AtomicReference<>();
        shouldFetch = foo -> foo == dbValue;
        saveCallResult = foo -> {
            saved.set( foo );
            dbData.setValue( dbValue2 );
            return null;
        };
        final MutableLiveData<ApiResponse<Foo>> apiResponseLiveData = new MutableLiveData<>();
        createCall = aVoid -> apiResponseLiveData;

        networkBoundResource.asLiveData().observeForever( observer );
        drain();
        verify( observer ).onChanged( Resource.loading( null ) );
        reset( observer );

        dbData.setValue( dbValue );
        drain();
        final Foo networkResult = new Foo( 1 );
        verify( observer ).onChanged( Resource.loading( dbValue ) );
        apiResponseLiveData.setValue( new ApiResponse<>( 200, networkResult, null ) );
        drain();
        assertThat( saved.get(), is( networkResult ) );
        verify( observer ).onChanged( Resource.success( dbValue2 ) );
        verifyNoMoreInteractions( observer );
    }

    static class Foo
    {

        int value;

        Foo(
            final int value )
        {
            this.value = value;
        }
    }
}