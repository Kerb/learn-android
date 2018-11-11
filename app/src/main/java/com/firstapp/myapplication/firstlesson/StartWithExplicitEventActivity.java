package com.firstapp.myapplication.firstlesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.firstapp.myapplication.R;

/**
 * Пример активити, которая запускается явным intent-ом, и в нее передаются какие-то данные через intent.extras
 */
public class StartWithExplicitEventActivity extends Activity {

    public static final String ATTRIBUTE = "attribute";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlesson_explicit_intent);

        textView = findViewById(R.id.editText);

        final Intent intent = getIntent();
        final String myText = intent.getExtras().getString(ATTRIBUTE);
        textView.setText(myText);

    }
}
