package com.firstapp.myapplication.fourthhomework;

/**
 * Thread-safe переключатель состояний стэйт-машины
 */
public interface SafeStateSwitcher {

    State safelyChangeState();

    State safelyGetState();
}
