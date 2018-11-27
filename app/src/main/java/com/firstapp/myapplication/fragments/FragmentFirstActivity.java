package com.firstapp.myapplication.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import com.firstapp.myapplication.R;

/**
 * @user: kerb
 * @created: 26/11/2018.
 */
public class FragmentFirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_first);
        buildFragments();
    }

    public void buildFragments() {
        //TODO всегда вызывать getSupportFragmentManager(), а не getFragmentManager()
        FragmentManager manager = this.getFragmentManager();

        manager.beginTransaction()
            .add(R.id.fragment_container, new MyFragment())
            .add(R.id.fragment_container, new MyFragment())
            .add(R.id.fragment_container, new MyFragment())
            .add(R.id.fragment_container, new MyFragment())
            .commit();
    }
}