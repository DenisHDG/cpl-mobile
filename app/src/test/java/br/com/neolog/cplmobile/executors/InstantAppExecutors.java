package br.com.neolog.cplmobile.executors;

import java.util.concurrent.Executor;

import br.com.neolog.cplmobile.AppExecutors;

public class InstantAppExecutors
    extends
        AppExecutors
{
    private static Executor instant = command -> command.run();

    public InstantAppExecutors()
    {
        super( instant, instant, instant );
    }
}
