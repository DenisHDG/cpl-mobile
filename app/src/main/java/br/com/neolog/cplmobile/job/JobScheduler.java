package br.com.neolog.cplmobile.job;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import android.content.Context;
import android.os.Bundle;

import timber.log.Timber;

public final class JobScheduler
{
    private final Class<? extends JobService> jobService;
    private final FirebaseJobDispatcher jobDispatcher;
    private final String tag;
    private int intervalInSeconds;
    private boolean shouldReplaceCurrent;
    private boolean isPeriodic;
    private Bundle extras;

    private JobScheduler(
        final Builder builder )
    {
        this.jobDispatcher = new FirebaseJobDispatcher( new GooglePlayDriver( builder.context ) );
        this.tag = builder.tag;
        this.jobService = builder.jobService;
        this.intervalInSeconds = builder.intervalInSeconds;
        this.shouldReplaceCurrent = builder.shouldReplaceCurrent;
        this.isPeriodic = builder.isPeriodic;
        this.extras = builder.extras;
    }

    public int startJob()
    {
        Timber.d( "starting job %s", jobService );
        final Job job = jobDispatcher.newJobBuilder()
            .setService( jobService )
            .setTag( tag )
            .setReplaceCurrent( shouldReplaceCurrent )
            .setRecurring( isPeriodic )
            .setLifetime( Lifetime.FOREVER )
            .setTrigger( intervalInSeconds == 0 ? Trigger.NOW : Trigger.executionWindow( intervalInSeconds, intervalInSeconds + 1 ) )
            .setConstraints( Constraint.ON_ANY_NETWORK )
            .setExtras( extras )
            .setRetryStrategy( RetryStrategy.DEFAULT_LINEAR )
            .build();
        return jobDispatcher.schedule( job );
    }

    public void stopJob()
    {
        Timber.d( "stopping job %s", tag );
        jobDispatcher.cancel( tag );
    }

    public static void cancellAllJobs(
        final Context context )
    {
        new FirebaseJobDispatcher( new GooglePlayDriver( context ) ).cancelAll();
    }

    public static void cancel(
        final Context context,
        final String tag )
    {
        new FirebaseJobDispatcher( new GooglePlayDriver( context ) ).cancel( tag );
    }

    public static class Builder
    {
        final Context context;
        final Class<? extends JobService> jobService;
        final String tag;
        int intervalInSeconds;
        boolean shouldReplaceCurrent = false;
        boolean isPeriodic = false;
        private Bundle extras;

        public Builder(
            final Context context,
            final String tag,
            final Class<? extends JobService> jobService )
        {
            this.context = context;
            this.tag = tag;
            this.jobService = jobService;
        }

        public Builder periodic()
        {
            isPeriodic = true;
            return this;
        }

        public Builder withIntervalInDays(
            int days )
        {
            return withIntervalInHours( days * 24 );
        }

        public Builder withIntervalInHours(
            int hours )
        {
            return withIntervalInMinutes( hours * 60 );
        }

        public Builder withIntervalInMinutes(
            int minutes )
        {
            return withIntervalInSeconds( minutes * 60 );
        }

        public Builder withIntervalInSeconds(
            int seconds )
        {
            this.intervalInSeconds = seconds;
            return this;
        }

        public Builder shouldReplaceCurrent()
        {
            shouldReplaceCurrent = true;
            return this;
        }

        public Builder withExtras(
            Bundle extras )
        {
            this.extras = extras;
            return this;
        }

        public JobScheduler build()
        {
            return new JobScheduler( this );
        }
    }
}
