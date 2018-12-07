package br.com.neolog.cplmobile.signal.job;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.content.Context;

import br.com.neolog.cplmobile.job.JobScheduler;

public class ResendSignalJobService
    extends
        JobService

{
    public static final String TAG = ResendSignalJobService.class.getSimpleName();

    public static void schedule(
        final Context context )
    {
        new JobScheduler.Builder( context, TAG, ResendSignalJobService.class )
            .periodic()
            .withIntervalInMinutes( 10 )
            .build()
            .startJob();
    }

    @Override
    public boolean onStartJob(
        final JobParameters job )
    {
        new ResendSignalAsyncTask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(
        final JobParameters job )
    {
        return false;
    }
}
