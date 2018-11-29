package com.firstapp.myapplication.fourthhomework;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firstapp.myapplication.R;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * Домашка 4: работа со стейт-машиной
 */
public class StateMachineActivity extends Activity {

    private Button currentStateButton;

    /**
     * Мессенджер для отправки сообщений
     */
    private Messenger outMessenger;

    /**
     * Мессенджер для приема ответных сообщений от сервиса
     */
    private Messenger inMessenger = new Messenger(new SwitchStateServiceReplyHandler(this));

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.i("onServiceConnected");
            outMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.i("onServiceDisconnected");
            outMessenger = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_machine);

        currentStateButton = findViewById(R.id.homework4_get_current_state_button);
        currentStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outMessenger != null) {
                    // отправляем переключение состояния в сервис
                    final Message switchStateMessage = Message.obtain(null, SwitchStateService.SWITCH_STATE);
                    switchStateMessage.replyTo = inMessenger;
                    Logger.i("Sending message " + switchStateMessage);
                    try {
                        outMessenger.send(switchStateMessage);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                } else {
                    throw new RuntimeException("outMessenger == null, такого не должно быть");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("bindService");
        bindService(newSwitchStateServiceIntent(), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i("unbindService");
        unbindService(serviceConnection);
    }

    /**
     * Интент для байнда сервиса
     */
    private Intent newSwitchStateServiceIntent() {
        return new Intent(this, SwitchStateService.class);
    }

    private static class SwitchStateServiceReplyHandler extends Handler {

        private WeakReference<StateMachineActivity> activity;

        public SwitchStateServiceReplyHandler(StateMachineActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final StateMachineActivity activityValue = activity.get();
            if (activityValue != null) {
                Logger.i("Получили сообщение " + msg);
                Toast.makeText(activityValue, "Получили сообщение!" + msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
