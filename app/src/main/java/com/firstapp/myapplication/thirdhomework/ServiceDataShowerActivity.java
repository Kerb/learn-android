package com.firstapp.myapplication.thirdhomework;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.firstapp.myapplication.R;

/**
 * Активити, которая получает данные о результатах закачки и показывает их
 */
public class ServiceDataShowerActivity extends Activity {

    public static final String TAG = ServiceDataShowerActivity.class.getCanonicalName();

    private ImageView image;

    private FileDownloadResultBroadcastReceiver fileDownloadBroadcastReceiver = new FileDownloadResultBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_data_shower);
        initializeUiComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO не очень красиво каждый раз тут сетить Image. Узнать как сделать красивый код
        fileDownloadBroadcastReceiver.setImage(image);
        registerReceiver(fileDownloadBroadcastReceiver, FileDownloadResultBroadcastReceiver.createIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(fileDownloadBroadcastReceiver);
    }

    private void initializeUiComponents() {
        image = findViewById(R.id.homework3_shower_image);
    }
}
