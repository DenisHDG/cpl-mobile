package br.com.neolog.cplmobile.monitorable.job;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.content.Context;

import br.com.neolog.cplmobile.job.JobScheduler;
import br.com.neolog.cplmobile.signal.job.RemoveOldSignalJobService;

public class ResendMonitorableFinishJob
    extends
        JobService

{
    public static final String TAG = ResendMonitorableFinishJob.class.getSimpleName();

    public static void schedule(
        final Context context )
    {
        new ResendMonitorableFinishAsyncTask().execute();

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
        new ResendMonitorableFinishAsyncTask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(
        final JobParameters job )
    {
        return false;
    }
}