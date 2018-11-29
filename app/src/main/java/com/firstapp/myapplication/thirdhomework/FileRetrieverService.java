package com.firstapp.myapplication.thirdhomework;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

/**
 * Сервис, выкачивающий переданный URL
 */
public class FileRetrieverService extends IntentService {

    public static final String TAG = FileRetrieverService.class.getCanonicalName();

    // Параметры интента-результата закачки файла
    // 1. success/fail
    // 2. имя сохраненного файла
    public static final String ACTION_FILE_DOWNLOADED = "com.firstapp.myapplication.thirdhomework.action.ACTION_FILE_DOWNLOADED";
    public static final String PARAMETER_FILE_NAME = "com.firstapp.myapplication.thirdhomework.action.PARAMETER_FILE_NAME";
    public static final String PARAMETER_RESULT = "com.firstapp.myapplication.thirdhomework.action.PARAMETER_RESULT";

    // Параметры интента-запроса на закачку файла
    // 1. URL файла, который хотим скачать
    private static final String ACTION_DOWNLOAD_FILE = "com.firstapp.myapplication.thirdhomework.action.DOWNLOAD_FILE";
    private static final String PARAMETER_URL = "com.firstapp.myapplication.thirdhomework.extra.URL";

    public FileRetrieverService() {
        super("FileRetrieverService");
    }

    private LocalBinder localBinder = new LocalBinder();

    /**
     * Враппер метод, запускающий сервис с командой исполнить загрузку файла по указанному URL
     */
    public void submitDownloadFileTask(Context context, String url) {
        Intent startServiceIntent = new Intent(context, FileRetrieverService.class);
        startServiceIntent.setAction(ACTION_DOWNLOAD_FILE);
        startServiceIntent.putExtra(PARAMETER_URL, url);
        context.startService(startServiceIntent);
    }

    /**
     * Хэндлер запросов на запуск сервиса (исполняются в отдельном треде)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_DOWNLOAD_FILE:
                    String url = intent.getStringExtra(PARAMETER_URL);
                    try {
                        final String downloadedFileName = doDownload(url);
                        notifyFileDownloadResult(true, downloadedFileName);
                    } catch (IOException e) {
                        notifyFileDownloadResult(true, null);
                        Logger.e("Ошибка: " + e.getMessage(), e);
                    }
                    break;
            }
        }
    }

    /**
     * Оповещаем о результате работа джобы
     */
    private void notifyFileDownloadResult(boolean result, String fileName) {
        Logger.i(">>notifyFileDownloadResult, result: " + result + " fileName " + fileName);
        Intent resultIntent = new Intent(ACTION_FILE_DOWNLOADED);
        resultIntent.putExtra(PARAMETER_RESULT, result);
        resultIntent.putExtra(PARAMETER_FILE_NAME, fileName);
        sendBroadcast(resultIntent);
    }

    /**
     * Метод, выкачивающий файл и сохраняющий его как tempfile.
     */
    private String doDownload(String url) throws IOException {
        final String jobID = UUID.randomUUID().toString();
        try {
            Logger.d("doDownload START >> jobID=" + jobID + ") starting download " + url);

            File tempFile = File.createTempFile("pref", ".tmp");
            try (final InputStream is = new URL(url).openStream();
                 final FileOutputStream fos = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[8192];
                int readBytes;
                while ((readBytes = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, readBytes);
                }
                fos.flush();
                return tempFile.getAbsolutePath();
            }
        } finally {
            Logger.d("doDownload DONE >> jobID=" + jobID + ") done ");
        }
    }

    /**
     * Возвращаем Binder
     */
    @Override
    public IBinder onBind(Intent intent) {
        Logger.i(TAG, ">> onBind " + intent.toString());
        return localBinder;
    }

    /**
     * Локальный binder, который вернет нам этот сервис
     */
    public class LocalBinder extends Binder {
        public FileRetrieverService getService() {
            return FileRetrieverService.this;
        }
    }
}
