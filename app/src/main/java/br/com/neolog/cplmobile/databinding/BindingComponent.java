package br.com.neolog.cplmobile.databinding;

import br.com.neolog.cplmobile.di.AppComponent;
import dagger.Component;

@DataBinding
@Component( dependencies = AppComponent.class, modules = DataBindingModule.class )
public interface BindingComponent
    extends
        android.databinding.DataBindingComponent
{
}