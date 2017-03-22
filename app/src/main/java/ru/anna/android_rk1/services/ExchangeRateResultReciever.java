package ru.anna.android_rk1.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;

import ru.anna.android_rk1.listeners.IExchangeRateListener;
import ru.anna.android_rk1.models.ExchangeRate;

public class ExchangeRateResultReciever extends ResultReceiver {
    private List<IExchangeRateListener> listeners = new ArrayList<>();


    public ExchangeRateResultReciever(Handler handler) {
        super(handler);
    }

    public void addListener(IExchangeRateListener listener) {
        if (listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(IExchangeRateListener listener) {
        if (listener != null)
            listeners.remove(listener);
    }

    public List<IExchangeRateListener> getListeners() {
        return listeners;
    }

    public void destroy() {
        if (listeners != null) {
            for (IExchangeRateListener listener : listeners)
                if (listener != null) {
                    listener.onDataRecieve(false, null);
                }

            listeners.clear();
        }
        listeners = null;
    }

    /**
     * Считается что данный метод будет вызван единожды.
     */
    @Override
    protected void onReceiveResult(final int code, final Bundle data) {
        boolean success = (code == ExchangeRateServiceIntent.RESULT_SUCCESS);
        ExchangeRate exchangeRate = null;
        if (data != null && success)
            exchangeRate = data.getParcelable(ExchangeRateServiceIntent.EXTRA_PARCELABLE_TAG);

        if (listeners != null) {
            for (IExchangeRateListener listener : listeners)
                if (listener != null)
                    if (success)
                        listener.onDataRecieve(success, exchangeRate);
                    else {
                        if (data != null && data.getString("error", null) != null)
                            listener.onError(data.getString("error"));
                    }

        }
    }
}
