package br.com.neolog.cplmobile.occurrence.comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.occurrence.selection.OccurrenceCauseSelectionActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OccurrenceCommentsActivity extends AppCompatActivity {

    @BindView(R.id.floatingActionButton_send)
    FloatingActionButton floatingActionButton_send;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurrence_comments);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.floatingActionButton_send)
    void occurrenceSaveCommment() {
        startActivity(new Intent(this, OccurrenceCauseSelectionActivity.class));
    }
}