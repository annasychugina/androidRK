package ru.anna.android_rk1.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.anna.android_rk1.models.Currency;
import ru.anna.android_rk1.models.ExchangeRate;

public class Storage {
    private static Storage INSTANCE = null;
    private SharedPreferences preferences;

    public static synchronized Storage getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Storage(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private Storage(Context context) {
        this.preferences = context.getSharedPreferences("BitCoinStorage", 0);
    }

    public Currency getCurrency() {
        return Currency.getCurrency(preferences.getInt("exchangerate_currency", -1));
    }

    public Storage setCurrency(Currency currency) {
        preferences.edit().putInt("exchangerate_currency", currency.getValue()).
                putFloat("exchangerate_value", currency.getValue()).apply();
        return this;
    }

    public Storage setExchangeRates(@NonNull ExchangeRate exchangeRates) {
        if (exchangeRates != null)
            preferences.edit().putInt("exchangerate_currency", exchangeRates.getCurrency().getValue()).
                    putFloat("exchangerate_value", (float) exchangeRates.getValue()).apply();
        return this;
    }

    @Nullable
    public ExchangeRate getExchangeRates() {
        Currency currency = Currency.getCurrency(preferences.getInt("exchangerate_currency", -1));
        float value = preferences.getFloat("exchangerate_value", -1);
        if (currency == Currency.UNKNOWN || value == -1)
            return null;
        else return new ExchangeRate(currency, value);
    }
}
