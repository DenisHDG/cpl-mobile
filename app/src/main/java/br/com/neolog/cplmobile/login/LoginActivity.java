package br.com.neolog.cplmobile.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.neolog.cplmobile.R;

public class LoginActivity
    extends
        AppCompatActivity
{
    @Override
    protected void onCreate(
        final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
    }
}