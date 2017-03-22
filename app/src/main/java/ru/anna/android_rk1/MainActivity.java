package ru.anna.android_rk1;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.anna.android_rk1.helpers.ServiceHelper;
import ru.anna.android_rk1.helpers.Storage;
import ru.anna.android_rk1.listeners.IExchangeRateListener;
import ru.anna.android_rk1.models.Currency;
import ru.anna.android_rk1.models.ExchangeRate;

public class MainActivity extends AppCompatActivity implements IExchangeRateListener {
    static {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .penaltyLog()
                .penaltyDeath()
                .build()
        );
    }

    private final View.OnClickListener onRefresh = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ServiceHelper.requestUpdate(view.getContext(), MainActivity.this);
        }
    };

    private final View.OnClickListener onClickSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Storage storage = Storage.getInstance(this);
        if (storage.getCurrency() == Currency.UNKNOWN)
            storage.setCurrency(Currency.RUB);

        TextView textView = (TextView) findViewById(R.id.main_exchangeRate_text);
        ExchangeRate exchangeRate = storage.getExchangeRates();
        if (textView != null && exchangeRate != null)
            textView.setText(exchangeRate.toString());

        Button button = (Button) findViewById(R.id.main_exchangeRate_refresh);
        if (button != null)
            button.setOnClickListener(onRefresh);

        Button buttonSetting = (Button) findViewById(R.id.main_exchangeRate_settings);
        if (buttonSetting != null)
            buttonSetting.setOnClickListener(onClickSettings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceHelper.requestUpdate(getApplicationContext(), MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ServiceHelper.getInstance(getApplicationContext()).removeListener(MainActivity.this);
    }

    @Override
    public void onDataRecieve(boolean success, @Nullable ExchangeRate exchangeRate) {
        if (success && exchangeRate != null) {
            TextView textView = (TextView) findViewById(R.id.main_exchangeRate_text);
            if (textView != null)
                textView.setText(exchangeRate.toString());
            Storage.getInstance(this).setExchangeRates(exchangeRate);
        }
    }

    @Override
    public void onError(String reason) {
        if (reason != null)
            Toast.makeText(this, reason, Toast.LENGTH_LONG).show();
    }
}
