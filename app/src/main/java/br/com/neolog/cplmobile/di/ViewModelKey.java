package br.com.neolog.cplmobile.di;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.arch.lifecycle.ViewModel;

import dagger.MapKey;

@Documented
@Target( {
    ElementType.METHOD
} )
@Retention( RetentionPolicy.RUNTIME )
@MapKey
public @interface ViewModelKey
{
    Class<? extends ViewModel> value();
}