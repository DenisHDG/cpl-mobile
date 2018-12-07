package br.com.neolog.cplmobile.transition;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class TransitionModule
{
    @Singleton
    @Provides
    public TransitionApi transitionApi(
        final Retrofit retrofit )
    {
        return retrofit.create( TransitionApi.class );
    }
}
