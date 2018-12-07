package br.com.neolog.cplmobile.di;

/**
 * Interface marcadora para dizer que uma Activity/Fragment são injetáveis.
 * <p>
 * Quando usando esta interface em Activity, é necessário que algum
 * {@link dagger.Module} tenha um método sem parâmetros anotado com
 * {@link dagger.android.ContributesAndroidInjector}, que retorne o tipo
 * concreto da instância, como no exemplo:
 * 
 * <pre>
 * &#64;ContributesAndroidInjector
 * abstract MainActivity contributeMainActivity();
 * </pre>
 */
public interface Injectable
{
}
