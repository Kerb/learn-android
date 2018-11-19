package com.firstapp.myapplication.secondhomework;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.firstapp.myapplication.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.net.http.SslCertificate.restoreState;

/**
 * Игра "Напёрстки"
 */
public class SecondHomeworkGameActivity extends Activity {

    private static final String TAG = SecondHomeworkGameActivity.class.getCanonicalName();
    public static final String OPENED_ATTR = "opened";
    public static final String GAME_STATE_ATTR = "gameState";

    // новая игра
    private Button newGameButton;

    private ClickHandler clickHandler = new ClickHandler();
    // состояние игры
    private boolean[] gameState = new boolean[3];
    //
    private ImageView[] imageViews = new ImageView[3];
    private Random random = new Random();

    // флаг того, что вскрыли стаканы (если true - не надо обрабатывать клики по ним)
    private boolean opened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_homework_game);

        imageViews[0] = findViewById(R.id.second_homework_image1);
        imageViews[0].setOnClickListener(clickHandler);

        imageViews[1] = findViewById(R.id.second_homework_image2);
        imageViews[1].setOnClickListener(clickHandler);

        imageViews[2] = findViewById(R.id.second_homework_image3);
        imageViews[2].setOnClickListener(clickHandler);

        newGameButton = findViewById(R.id.button_new_game);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
                repaintCurrentGameState();
            }
        });
        if (savedInstanceState == null) {
            newGame();
        } else {
            restoreGameState(savedInstanceState);
        }
        repaintCurrentGameState();
    }

    private void repaintCurrentGameState() {
        // отрисовали текущее состояние
        if (opened) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setImageResource(gameState[i] ? R.drawable.ic_opened_full : R.drawable.ic_opened_empty);
            }
        } else {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setImageResource(R.drawable.ic_closed);
            }
        }
    }

    /**
     * Сохранение состояния игры
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(OPENED_ATTR, opened);
        outState.putBooleanArray(GAME_STATE_ATTR, gameState);
    }

    /**
     * Восстановление состояния
     * @param savedInstanceState
     */
    private void restoreGameState(Bundle savedInstanceState) {
        opened = savedInstanceState.getBoolean(OPENED_ATTR);
        gameState = savedInstanceState.getBooleanArray(GAME_STATE_ATTR);
        newGameButton.setVisibility(opened ? View.VISIBLE : View.INVISIBLE);
    }

    public void newGame() {
        opened = false;
        // генерируем стакан, под которым
        final int winIndex = random.nextInt(2);
        Log.d(TAG, "Выигрышный стакан: " + winIndex);
        for (int i = 0; i < 3; i++) {
            gameState[i] = (winIndex == i);
        }
        newGameButton.setVisibility(View.INVISIBLE);
    }

    private int getIndexByView(ImageView imageView) {
        for (int i = 0; i < imageViews.length; i++) {
            if (imageView == imageViews[i]) {
                return i;
            }
        }
        return -1;
    }

    public class ClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (opened) {
                Toast.makeText(SecondHomeworkGameActivity.this, "Стаканы открыты, начните новую игру!!", Toast.LENGTH_SHORT)
                    .show();
                return;
            }
            newGameButton.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) v;
            for (int i = 0; i < imageViews.length; i++) {
                if (gameState[i]) {
                    imageViews[i].setImageResource(R.drawable.ic_opened_full);
                } else {
                    imageViews[i].setImageResource(R.drawable.ic_opened_empty);
                }
            }

            if (gameState[getIndexByView(imageView)]) {
                Toast.makeText(SecondHomeworkGameActivity.this, "Вы выиграли!!", Toast.LENGTH_SHORT)
                    .show();
            } else {
                Toast.makeText(SecondHomeworkGameActivity.this, "Вы проиграли!!", Toast.LENGTH_SHORT)
                    .show();
            }
            opened = true;
        }
    }
}
