package br.com.neolog.cplmobile.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.AppExecutors;

public class CountingAppExecutors
{

    private final Object LOCK = new Object();

    private int taskCount = 0;

    private final AppExecutors appExecutors;

    public CountingAppExecutors()
    {
        final Runnable increment = () -> {
            synchronized( LOCK ) {
                taskCount--;
                if( taskCount == 0 ) {
                    LOCK.notifyAll();
                }
            }
        };
        final Runnable decrement = () -> {
            synchronized( LOCK ) {
                taskCount++;
            }
        };
        appExecutors = new AppExecutors(
            new CountingExecutor( increment, decrement ),
            new CountingExecutor( increment, decrement ),
            new CountingExecutor( increment, decrement ) );
    }

    public AppExecutors getAppExecutors()
    {
        return appExecutors;
    }

    public void drainTasks(
        final int time,
        final TimeUnit timeUnit )
        throws InterruptedException,
            TimeoutException
    {
        final long end = System.currentTimeMillis() + timeUnit.toMillis( time );
        while( true ) {
            synchronized( LOCK ) {
                if( taskCount == 0 ) {
                    return;
                }
                final long now = System.currentTimeMillis();
                final long remaining = end - now;
                if( remaining > 0 ) {
                    LOCK.wait( remaining );
                } else {
                    throw new TimeoutException( "could not drain tasks" );
                }
            }
        }
    }

    private static class CountingExecutor
        implements
            Executor
    {

        private final Executor delegate = Executors.newSingleThreadExecutor();

        private final Runnable increment;

        private final Runnable decrement;

        private CountingExecutor(
            final Runnable increment,
            final Runnable decrement )
        {
            this.increment = increment;
            this.decrement = decrement;
        }

        @Override
        public void execute(
            @NonNull final Runnable command )
        {
            increment.run();
            delegate.execute( () -> {
                try {
                    command.run();
                } finally {
                    decrement.run();
                }
            } );
        }
    }
}
