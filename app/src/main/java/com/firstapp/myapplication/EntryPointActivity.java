package com.firstapp.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.firstapp.myapplication.firstlesson.StartWithExplicitEventActivity;

/**
 * Стартовая Activity, точка входа в приложение откуда запускаются все остальные activity
 */
public class EntryPointActivity extends Activity {

    private View firstLessonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point);

        configureFirstLessonButton();
    }

    private void configureFirstLessonButton() {
        firstLessonButton = findViewById(R.id.first_lesson_button);
        firstLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPointActivity.this, StartWithExplicitEventActivity.class);
                intent.putExtra(StartWithExplicitEventActivity.ATTRIBUTE, "Пыщ пыщ");
                startActivity(intent);
            }
        });
    }
}
