package ru.anna.android_rk1.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;

import ru.anna.android_rk1.App;
import ru.anna.android_rk1.listeners.IExchangeRateListener;
import ru.anna.android_rk1.listeners.ISubscribeOnTerminate;
import ru.anna.android_rk1.models.ExchangeRate;
import ru.anna.android_rk1.services.ExchangeRateResultReciever;
import ru.anna.android_rk1.services.ExchangeRateServiceIntent;

public class ServiceHelper implements ISubscribeOnTerminate, IExchangeRateListener {
    private static ServiceHelper INSTANCE = null;
    private ExchangeRateResultReciever exchangeRateResultReciever = null;

    private ServiceHelper(Context context) {
        if (context != null)
            if (context.getApplicationContext() instanceof App)
                ((App) context.getApplicationContext()).subscribeOnTerminate(this);
    }

    public void removeListener(IExchangeRateListener exchangeRateListener) {
        if (exchangeRateResultReciever != null && exchangeRateListener != null)
            exchangeRateResultReciever.removeListener(exchangeRateListener);
    }

    public void requestExchangeRate(Context context, IExchangeRateListener listener) {
        if (exchangeRateResultReciever != null && exchangeRateResultReciever.getListeners().size() > 0)
            exchangeRateResultReciever.addListener(listener);
        else {
            ExchangeRateResultReciever exchangeRateResultReciever = getExchangeRateResultReciever();
            if (exchangeRateResultReciever != null) {
                this.exchangeRateResultReciever = exchangeRateResultReciever;
                if (listener != null)
                    exchangeRateResultReciever.addListener(listener);
                Intent intent = createIntent(context, exchangeRateResultReciever);
                if (intent != null)
                    context.startService(intent);
            }
        }
    }

    private ExchangeRateResultReciever getExchangeRateResultReciever() {
        ExchangeRateResultReciever exchangeRateResultReciever = new ExchangeRateResultReciever(new Handler());
        exchangeRateResultReciever.addListener(this);
        return exchangeRateResultReciever;
    }

    private Intent createIntent(Context context, ExchangeRateResultReciever exchangeRateResultReciever) {
        Intent intent = new Intent(context, ExchangeRateServiceIntent.class);
        intent.setAction(ExchangeRateServiceIntent.ACTION_RATES);
        intent.putExtra(ExchangeRateServiceIntent.EXTRA_RATES_RESULT_RECEIVER, exchangeRateResultReciever);
        return intent;

    }

    public static ServiceHelper getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new ServiceHelper(context);

        return INSTANCE;
    }

    public static void requestUpdate(Context context, IExchangeRateListener listener) {
        getInstance(context).requestExchangeRate(context, listener);
    }

    @Override
    public void onTerminate() {
        if (exchangeRateResultReciever != null)
            exchangeRateResultReciever.destroy();
        exchangeRateResultReciever = null;
    }

    @Override
    public void onDataRecieve(boolean success, @Nullable ExchangeRate exchangeRate) {
        exchangeRateResultReciever = null;
    }

    @Override
    public void onError(String reason) {
        //Do nothing
    }
}
