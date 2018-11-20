package com.firstapp.myapplication.thirdhomework;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;

/**
 * Сервис, выкачивающий переданный URL
 */
public class FileRetrieverService extends IntentService {

    public static final String TAG = FileRetrieverService.class.getCanonicalName();

    private static final String ACTION_DOWNLOAD_FILE = "com.firstapp.myapplication.thirdhomework.action.DOWNLOAD_FILE";
    private static final String PARAMETER_URL = "com.firstapp.myapplication.thirdhomework.extra.URL";
    private static final String PARAMETER_MESSENGER = "com.firstapp.myapplication.thirdhomework.extra.MESSENGER";

    public FileRetrieverService() {
        super("FileRetrieverService");
    }

    /**
     * Враппер метод, запускающий сервис с командой исполнить загрузку файла по указанному URL
     */
    public static void submitDownloadFileTask(Context context, String url, Messenger callerMessenger) {
        Intent startServiceIntent = new Intent(context, FileRetrieverService.class);
        startServiceIntent.setAction(ACTION_DOWNLOAD_FILE);
        startServiceIntent.putExtra(PARAMETER_URL, url);
        startServiceIntent.putExtra(PARAMETER_MESSENGER, callerMessenger);
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
                    Messenger callerMessenger = intent.getParcelableExtra(PARAMETER_MESSENGER);

                    final String resultFileName;
                    try {
                        resultFileName = doDownload(url);
                        // оповещаем вызывающего клиента об успехе
                        sendMessageSilently(callerMessenger, ConstantsHomework3.FILE_DOWNLOADED, resultFileName);
                    } catch (IOException e) {
                        // оповещаем вызывающего клиента о фэйле
                        Log.e(TAG, "Ошибка: ", e);
                        sendMessageSilently(callerMessenger, ConstantsHomework3.FILE_NOT_DOWNLOADED, null);
                    }
                    break;
            }
        }
    }

    /**
     * Отправка через messenger результата работы сервиса
     * @param messenger
     * @param what
     * @param fileName
     */
    public void sendMessageSilently(Messenger messenger, int what, String fileName) {
        final Message mes = Message.obtain(null, what);
        final Bundle resultData = new Bundle();
        resultData.putString(ConstantsHomework3.FILE_NAME, fileName);
        mes.setData(resultData);
        try {
            messenger.send(mes);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send message ", e);
        }
    }

    /**
     * Метод, выкачивающий файл и сохраняющий его как tempfile.
     */
    private String doDownload(String url) throws IOException {
        try {
            Log.d(TAG, "doDownload >> Thread " + Thread.currentThread().getName() + " starting download " + url);

            File tempFile = File.createTempFile("pref", ".tmp");
            try (final InputStream is = new URL(url).openStream();
                 final FileOutputStream fos = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[8192];
                int readBytes = 0;
                while ((readBytes = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, readBytes);
                }
                fos.flush();
                return tempFile.getAbsolutePath();
            }
        } finally {
            Log.d(TAG, "doDownload << Thread " + Thread.currentThread().getName() + " starting download " + url);
        }
    }
}
