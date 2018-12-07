package br.com.neolog.cplmobile;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import android.os.Handler;
import android.os.Looper;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk
 * reads don't wait behind webservice requests).
 */
@Singleton
public class AppExecutors
{

    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    public AppExecutors(
        final Executor diskIO,
        final Executor networkIO,
        final Executor mainThread )
    {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    @Inject
    public AppExecutors()
    {
        this(
            Executors.newSingleThreadExecutor(),
            Executors.newFixedThreadPool( 3 ),
            new MainThreadExecutor() );
    }

    public Executor diskIO()
    {
        return diskIO;
    }

    public Executor networkIO()
    {
        return networkIO;
    }

    public Executor mainThread()
    {
        return mainThread;
    }

    private static class MainThreadExecutor
        implements
            Executor
    {
        private final Handler mainThreadHandler = new Handler( Looper.getMainLooper() );

        @Override
        public void execute(
            @Nonnull final Runnable command )
        {
            mainThreadHandler.post( command );
        }
    }
}
