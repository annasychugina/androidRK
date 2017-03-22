package ru.anna.android_rk1.helpers;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ru.anna.android_rk1.models.Currency;
import ru.anna.android_rk1.models.ExchangeRate;

public class ExchangeRateDeserializer implements JsonDeserializer<ExchangeRate> {
    @Override
    public ExchangeRate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ExchangeRate exchangeRate = null;
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            float value = jsonObject.get("value").getAsFloat();
            Currency currency = Currency.getCurrency(jsonObject.get("currency").getAsString());
            exchangeRate = new ExchangeRate(currency, value);
        } catch (Exception e) {
            Log.e("Deserialize", "error", e);
        }
        return exchangeRate;
    }
}
