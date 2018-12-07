package br.com.neolog.cplmobile;

import static br.com.neolog.cplmobile.BuildConfig.DEBUG;

import javax.inject.Inject;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;

import br.com.neolog.cplmobile.databinding.BindingComponent;
import br.com.neolog.cplmobile.databinding.DaggerBindingComponent;
import br.com.neolog.cplmobile.di.AppComponent;
import br.com.neolog.cplmobile.di.AppInjector;
import br.com.neolog.cplmobile.timber.FirebaseLogTree;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class CPLApplication
    extends
        MultiDexApplication
    implements
        HasActivityInjector,
        HasSupportFragmentInjector
{
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidFragmentInjector;

    private static CPLApplication instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initTimber();
        initCrashlytics();
        AppInjector.init( this );
        Stetho.initializeWithDefaults( getApplicationContext() );
        instance = this;
        initDataBinding( AppInjector.init( this ) );
    }

    private void initCrashlytics()
    {
        final CrashlyticsCore core = new CrashlyticsCore.Builder().disabled( BuildConfig.DEBUG ).build();
        final Crashlytics crashlytics = new Crashlytics.Builder().core( core ).build();
        Fabric.with( this, crashlytics );
    }

    private void initDataBinding(
        final AppComponent appComponent )
    {
        final BindingComponent bindingComponent = DaggerBindingComponent.builder()
            .appComponent( appComponent )
            .build();
        DataBindingUtil.setDefaultComponent( bindingComponent );
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector()
    {
        return dispatchingAndroidInjector;
    }

    private void initTimber()
    {
        if( DEBUG ) {
            Timber.plant( new Timber.DebugTree() );
        } else {
            Timber.plant( new FirebaseLogTree() );
        }
    }

    public static CPLApplication getInstance()
    {
        return instance;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector()
    {
        return dispatchingAndroidFragmentInjector;
    }
}
