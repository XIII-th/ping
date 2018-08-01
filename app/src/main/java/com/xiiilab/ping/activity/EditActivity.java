package com.xiiilab.ping.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.xiiilab.ping.R;
import com.xiiilab.ping.repository.Repository;
import com.xiiilab.ping.viewmodel.edit.EditViewModel;

/**
 * Created by XIII-th on 21.07.2018
 */
public class EditActivity extends AppCompatActivity {

    public static final String EDIT_ENTITY_ID = "com.xiiilab.ping.activity.EditActivity EDIT_ENTITY_ID";
    private EditViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialise view model only first call of Activity.onCreate()
        mViewModel = ViewModelProviders.of(this).get(EditViewModel.class);
        String hostForEdit = getIntent().getStringExtra(EDIT_ENTITY_ID);
        if (!mViewModel.isInitialised()) {
            mViewModel.setRepository(Repository.getInstance());
            mViewModel.load(hostForEdit);
        }
        setContentView(R.layout.activity_edit);

        // setup toolbar controls and title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(hostForEdit == null ? R.string.add_new_host : R.string.host_edit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create menu with one option - save
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public void onSaveClicked(MenuItem item) {
        mViewModel.save(this).observe(this, this::onSaveCompleted);
    }

    private void onSaveCompleted(Boolean success) {
        if (success)
            // changes successfully applied
            finish();
    }
}
