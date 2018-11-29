package com.firstapp.myapplication.fourthhomework;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.orhanobut.logger.Logger;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.firstapp.myapplication.fourthhomework.StateImpl.A;

/**
 * Сервис, переключающий состояние машины
 */
public class SwitchStateService extends Service implements SafeStateSwitcher {
    /**
     * Переключить состояние
     */
    public static final int SWITCH_STATE = 1;
    /**
     * Узнать текущее состояние
     */
    public static final int CURRENT_STATE = 2;

    /**
     * Текущее состояние стейт-машины
     */
    private static State state = A;

    /**
     * thread-safe обновление текущего состояния в статическом поле
     */
    public State safelyChangeState() {
        final ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return state = state.next();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Получение текущего состояния (через readLock, т.к. пишем через writeLock)
     */
    public State safelyGetState() {
        final ReentrantReadWriteLock.ReadLock readLock = new ReentrantReadWriteLock().readLock();
        readLock.lock();
        try {
            return state;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Активити будет передавать команды сервису через этот мессенджер
     */
    private Messenger messenger = new Messenger(new MessageHandler(this));

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i(">> enter");
        return messenger.getBinder();
    }

    /**
     * Хэндлер сообщений
     */
    static class MessageHandler extends Handler {

        private SafeStateSwitcher safeStateSwitcher;

        // TODO есть ли смысл передавать интефейс в хендлеры (чтобы нельзя было дернуть чего лишнего - в случае если передавать целиком activity)?
        public MessageHandler(SafeStateSwitcher safeStateSwitcher) {
            this.safeStateSwitcher = safeStateSwitcher;
        }

        @Override
        public void handleMessage(final Message msg) {
            // если не сохранять msg.replyTo в replyBackMessenger, то в новом потоке это поле == null
            final Messenger replyBackMessenger = msg.replyTo;
            new Thread() {
                @Override
                public void run() {
                    com.firstapp.myapplication.fourthhomework.State oldState = safeStateSwitcher.safelyGetState();
                    com.firstapp.myapplication.fourthhomework.State newState = safeStateSwitcher.safelyChangeState();
                    Logger.i("%s выполнили переход %s -> %s", msg, oldState, newState);

                    final Message replyMessage = Message.obtain(null, CURRENT_STATE);
                    replyMessage.obj = oldState + " -> " + newState;
                    try {
                        replyBackMessenger.send(replyMessage);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }.start();
        }
    }
}
