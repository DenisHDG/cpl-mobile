package br.com.neolog.cplmobile.signal.job;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.content.Context;

import br.com.neolog.cplmobile.job.JobScheduler;

public class RemoveOldSignalJobService
    extends
        JobService

{
    public static final String TAG = RemoveOldSignalJobService.class.getSimpleName();

    public static void schedule(
        final Context context )
    {
        new JobScheduler.Builder( context, TAG, RemoveOldSignalJobService.class )
            .periodic()
            .withIntervalInHours( 6 )
            .build()
            .startJob();
    }

    @Override
    public boolean onStartJob(
        final JobParameters job )
    {
        new RemoveOldSignalAsyncTask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(
        final JobParameters job )
    {
        return false;
    }
}
