package com.firstapp.myapplication.thirdhomework;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.firstapp.myapplication.R;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Домашка №3:
 *
 * Активити с 2мя кнопками.
 *
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
     * Мессенджер, который ловит результаты выполнения вызова сервиса
     * TODO: Как обрабатывать кейс: Если activity была скрыта, и мы не хотим показывать toast-ы?
     */
    private Messenger messenger = new Messenger(new IncomingHandler(this));

    /**
     * Хэндлер для обработки сообщений от сервиса о результатах работы, напр. файл скачался успешно.
     *
     * Без static ругался на то, что возможны утечки памяти, из-за того, что inner-class содержит неявную
     * ссылку на его enclosing-class.
     *
     * На SO рекомендуют делать его static и прокидывать активити как weak-reference.
     */
    public static class IncomingHandler extends Handler {

        private WeakReference<ServiceStarterActivity> ref;

        public IncomingHandler(ServiceStarterActivity serviceStarterActivity) {
            this.ref = new WeakReference<>(serviceStarterActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ServiceStarterActivity activity = ref.get();
            if (activity != null) {
                final Bundle data = msg.getData();
                switch (msg.what) {
                    case ConstantsHomework3.FILE_DOWNLOADED:
                        if (data != null) {
                            final String downloadedFile = data.getString(ConstantsHomework3.FILE_NAME);

                            Toast.makeText(activity, "Скачивание файла завершено. Сохранен в " + downloadedFile, Toast.LENGTH_SHORT)
                                .show();
                            activity.loadImage(downloadedFile);
                        }
                        break;
                    case ConstantsHomework3.FILE_NOT_DOWNLOADED:
                        Toast.makeText(activity, "Ошибка при закачивании файла", Toast.LENGTH_SHORT)
                            .show();
                        break;
                }
                Log.i(TAG, data != null ? data.toString() : null);
            }
        }
    }

    /**
     * Подвязка всех компонентов в рантайме
     */
    private void bindUI() {
        startServiceButton = findViewById(R.id.homework3_start_service_button);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileRetrieverService.submitDownloadFileTask(
                    ServiceStarterActivity.this,
                    imageUrls[random.nextInt(imageUrls.length)],
                    messenger);
            }
        });

        startActivityButton = findViewById(R.id.homework3_start_activity_button);
        imageView = findViewById(R.id.homework3_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_starter);
        bindUI();
    }

    /**
     * Загружает картинку из локаьного файла и показывает в ImageView
     */
    private void loadImage(String filePath) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }
}
