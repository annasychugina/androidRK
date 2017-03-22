package ru.anna.android_rk1.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.anna.android_rk1.models.Currency;
import ru.anna.android_rk1.models.ExchangeRate;

public class CourseLoader {
    public static final String TESTING_MASHAPE_KEY = "7pwoFUe1Lwmsh5bXQycWhQlTqNVQp1J9uH5jsnUCrnSMIe3sge";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(ExchangeRate.class, new ExchangeRateDeserializer()).create();

    private final OkHttpClient httpClient = new OkHttpClient();

    public ExchangeRate getExchangeRates(Currency currency) throws IOException {
        if (currency == Currency.UNKNOWN)
            return null;

        Request request = new Request.Builder().url("https://community-bitcointy.p.mashape.com/average/" + currency.getKey()).get().
                addHeader("X-Mashape-Key", TESTING_MASHAPE_KEY).
                addHeader("Accept", "text/json").build();

        Response response = this.httpClient.newCall(request).execute();
        try {
            if (response.isSuccessful()) {
                String answer = response.body().string();
                return (ExchangeRate) gson.fromJson(answer, ExchangeRate.class);
            }
            throw new IOException("Wrong status: " + response.code() + "; body: " + response.body().string());
        } finally {
            response.body().close();
        }
    }
}
