package com.firstapp.myapplication.thirdhomework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.Toast;
import com.orhanobut.logger.Logger;

/**
 * Ресивер, получающий уведомление, о результатах закачки файла.
 * Если к моменту финиша успешной закачки файла будет открыта эта активити, в image загрузится полученный файл
 */
public class FileDownloadResultBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = FileDownloadResultBroadcastReceiver.class.getCanonicalName();

    /**
     * В этот image кладем рисунок при получении интента, о том, что файл закачан успешно
     */
    private ImageView image;

    public void setImage(ImageView image) {
        this.image = image;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null
            && intent.getAction() != null
            && intent.getAction().equals(FileRetrieverService.ACTION_FILE_DOWNLOADED)) {

            final boolean success = intent.getBooleanExtra(FileRetrieverService.PARAMETER_RESULT, false);
            if (success) {
                final String fileName = intent.getStringExtra(FileRetrieverService.PARAMETER_FILE_NAME);
                loadImage(fileName);
                Logger.i(">> onReceive(success), file " + fileName);
                Toast.makeText(context, "Загрузили " + fileName, Toast.LENGTH_SHORT)
                    .show();
            } else {
                Logger.i(">> onReceive(error)");
                Toast.makeText(context, "Ошибка закачки файла", Toast.LENGTH_SHORT)
                    .show();
            }
        } else {
            throw new RuntimeException("GOT unknown intent");
        }
    }

    /**
     * Загружает картинку из локаьного файла и показывает в ImageView
     */
    private void loadImage(String filePath) {
        Logger.d(TAG, "image = " + image + " show " + filePath);
        if (image != null) {
            image.setImageBitmap(BitmapFactory.decodeFile(filePath));
        }
    }

    /**
     * Создает intent-filter для срабатывания данного broadcast-receiver-а
     */
    public static IntentFilter createIntentFilter() {
        return new IntentFilter(FileRetrieverService.ACTION_FILE_DOWNLOADED);
    }
}
