package com.firstapp.myapplication.thirdhomework;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import com.firstapp.myapplication.R;

/**
 * Домашка №3:
 * Активити с 2мя кнопками.
 * 1я кнопка - запускает сервис
 * 2я кнопка - запускает активити, который привязывается к сервису, и выводит все, что получает
 */
public class ServiceStarterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_starter);
    }
}
