package ru.anna.android_rk1.listeners;

import android.support.annotation.Nullable;

import ru.anna.android_rk1.models.ExchangeRate;

public interface IExchangeRateListener {
    void onDataRecieve(boolean success, @Nullable ExchangeRate exchangeRate);

    void onError(String reason);
}
