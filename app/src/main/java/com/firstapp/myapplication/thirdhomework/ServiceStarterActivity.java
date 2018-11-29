package com.firstapp.myapplication.thirdhomework;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.firstapp.myapplication.R;
import com.orhanobut.logger.Logger;

import java.util.Random;

/**
 * Домашка №3:
 * <p>
 * Активити с 2мя кнопками.
 * <p>
 * 1я кнопка - запускает сервис, который что-то делает (в моем случае качает файл)
 * 2я кнопка - запускает активити, которая привязывается к сервису, и выводит все, что получает
 */
public class ServiceStarterActivity extends Activity {

    public static final String TAG = ServiceStarterActivity.class.getCanonicalName();

    /**
     * Массив URL-ов, которые сервис будет выкачивать
     */
    private static final String[] imageUrls = new String[]{
        "https://www.mvff.com/wp-content/uploads/2017/10/SS-INTLSS4-0412-MR_sm.jpg",
        "https://www.billboard.com/files/media/avril-lavigne-pre-grammys-2016-billboard-1548_0.jpg",
        "https://i.pinimg.com/originals/71/6e/59/716e59b917dcf8da72e87a3ca56ede60.jpg",
        "https://data1.ibtimes.co.in/en/full/603180/kaley-cuoco.jpg",
        "https://mysoulsonice.files.wordpress.com/2013/12/angelina_jolie-ha.jpg"
    };

    /**
     * По клику запускается сервис
     */
    private Button startServiceButton;

    /**
     * По клику запускается активити, которая начинает биндится к сервису
     */
    private Button startActivityButton;

    /**
     * В этом компоненте показываем скачанные картинки
     */
    private ImageView imageView;

    /**
     * Рандомайзер для выборки фотографий
     */
    private Random random = new Random();

    /**
     * Сервис загрузки файлов
     */
    private FileRetrieverService.LocalBinder fileRetrieverBinder;

    private FileDownloadResultBroadcastReceiver fileDownloadBroadcastReceiver = new FileDownloadResultBroadcastReceiver();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            fileRetrieverBinder = (FileRetrieverService.LocalBinder) service;
            Logger.i(">> onServiceConnected, Connected to service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            fileRetrieverBinder = null;
            Logger.i(">> onServiceDisconnected, disconnected from service");
        }
    };

    /**
     * Подвязка всех компонентов в рантайме
     */
    private void initializeUIComponents() {
        startServiceButton = findViewById(R.id.homework3_start_service_button);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileRetrieverBinder != null) {
                    fileRetrieverBinder.getService().submitDownloadFileTask(
                        ServiceStarterActivity.this,
                        imageUrls[random.nextInt(imageUrls.length)]);

                }
            }
        });

        startActivityButton = findViewById(R.id.homework3_start_activity_button);
        startActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceStarterActivity.this, ServiceDataShowerActivity.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.homework3_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, FileRetrieverService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        // TODO не очень красиво каждый раз тут сетить Image. Узнать как сделать красиво
        fileDownloadBroadcastReceiver.setImage(imageView);
        registerReceiver(fileDownloadBroadcastReceiver, FileDownloadResultBroadcastReceiver.createIntentFilter());
        Logger.i(">> onResume, bindService, registerReceiver");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
        unregisterReceiver(fileDownloadBroadcastReceiver);
        Logger.i(">> unbindService, unbindService, unregisterReceiver");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_starter);
        initializeUIComponents();
    }

    /**
     * Загружает картинку из локаьного файла и показывает в ImageView
     */
    private void loadImage(String filePath) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }
}
