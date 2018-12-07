package br.com.neolog.cplmobile.databinding;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

/**
 * Escopo para ciclo de vida de DataBinding
 */
@Scope
@Documented
@Retention( RUNTIME )
public @interface DataBinding
{
}
