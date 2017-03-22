package ru.anna.android_rk1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import ru.anna.android_rk1.helpers.ServiceHelper;
import ru.anna.android_rk1.helpers.Storage;
import ru.anna.android_rk1.models.Currency;


public class SettingActivity extends Activity {

    private final View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Spinner spinner = (Spinner) findViewById(R.id.activity_setting_spinner);
            if (spinner != null) {
                if (spinner.getSelectedItem() != null && spinner.getSelectedItem() instanceof String) {
                    Currency currency = Currency.getCurrency((String) spinner.getSelectedItem());
                    Storage.getInstance(SettingActivity.this).setCurrency(currency);
                    Toast.makeText(SettingActivity.this, "Валюта изменена", Toast.LENGTH_LONG).show();
                    ServiceHelper.requestUpdate(SettingActivity.this, null);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinner = (Spinner) findViewById(R.id.activity_setting_spinner);
        if (spinner != null) {
            String[] keys = Currency.getKeys();
            String selectedString = Storage.getInstance(this).getCurrency().getKey();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, keys);

            spinner.setAdapter(adapter);
            for (int i = 0; i < keys.length; i++)
                if (keys[i].equals(selectedString)) {
                    spinner.setSelection(i);
                    break;
                }
        }
        Button buttonSave = (Button) findViewById(R.id.activity_setting_save);
        if (buttonSave != null)
            buttonSave.setOnClickListener(onSave);
    }
}
