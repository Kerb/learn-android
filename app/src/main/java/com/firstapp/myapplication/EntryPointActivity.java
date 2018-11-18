package com.firstapp.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.firstapp.myapplication.firstlesson.StartWithExplicitEventActivity;
import com.firstapp.myapplication.secondhomework.SecondHomeworkGameActivity;

/**
 * Стартовая Activity, точка входа в приложение откуда запускаются все остальные activity
 */
public class EntryPointActivity extends Activity {

    /**
     * Урок 1: явный запуск активити
     */
    private View firstLessonButton;
    /**
     * Домашка 2: запуск игры "Пятнашки"
     */
    private View secondHomeworkGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point);

        configureFirstHomeworkButton();
        configureSecondHomeworkButton();
    }

    private void configureFirstHomeworkButton() {
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

    private void configureSecondHomeworkButton() {
        secondHomeworkGameButton = findViewById(R.id.second_homework_game_button);
        secondHomeworkGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPointActivity.this, SecondHomeworkGameActivity.class);
                startActivity(intent);
            }
        });
    }
}
