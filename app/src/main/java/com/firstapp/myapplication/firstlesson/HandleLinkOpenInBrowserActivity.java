package com.firstapp.myapplication.firstlesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import com.firstapp.myapplication.R;
import com.orhanobut.logger.Logger;

/**
 * Перехватывает ссылку из стандартного браузера и открывает в WebView в данной activity.
 * Для корректной работы требуется добавить в манифесте:
 *
 * <uses-permission android:name="android.permission.INTERNET"/>
 */
public class HandleLinkOpenInBrowserActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlesson_handle_browser_link);

        final Intent intent = getIntent();
        webView = findViewById(R.id.first_lesson_webview);
        webView.loadUrl(getIntent().getData().toString());

        Logger.i("LESSON", intent.toString());
    }
}
