package ru.anna.android_rk1.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.anna.android_rk1.helpers.CourseLoader;
import ru.anna.android_rk1.helpers.Storage;
import ru.anna.android_rk1.models.ExchangeRate;

public class ExchangeRateServiceIntent extends IntentService {
    public static final String SERVICE_NAME = "ExchangeRateServiceIntent";
    public static final String ACTION_RATES = "action.ExchangeRate";
    public static final String EXTRA_RATES_RESULT_RECEIVER = "extra.ExchangeRate_RESULT_RECEIVER";
    public static final String EXTRA_PARCELABLE_TAG = "exchangerate";

    public final static int RESULT_SUCCESS = 0;
    public final static int RESULT_ERROR = 1;

    public ExchangeRateServiceIntent() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(ACTION_RATES)) {
                final Bundle data = new Bundle();
                final ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RATES_RESULT_RECEIVER);
                if (resultReceiver != null)
                    try {
                        Storage storage = Storage.getInstance(this);
                        ExchangeRate exchangeRate = new CourseLoader().getExchangeRates(storage.getCurrency());

                        if (exchangeRate != null) {
                            storage.setExchangeRates(exchangeRate);
                            data.putString("title", "Успешно!");
                            data.putParcelable(EXTRA_PARCELABLE_TAG, exchangeRate);
                            resultReceiver.send(RESULT_SUCCESS, data);
                        }
                    } catch (Exception e) {
                        Log.e(SERVICE_NAME.substring(0, 21), "error", e);
                        data.putString("error", e.getLocalizedMessage());
                        resultReceiver.send(RESULT_ERROR, data);
                    }
            }
        }
    }
}
