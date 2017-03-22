package ru.anna.android_rk1.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExchangeRate implements Parcelable {
    public Currency currency = Currency.UNKNOWN;
    public float value = -1;

    protected ExchangeRate(Parcel in) {
        currency = Currency.getCurrency(in.readInt());
        value = in.readFloat();
    }

    public ExchangeRate(Currency currency, float value) {
        this.currency = currency;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public static final Creator<ExchangeRate> CREATOR = new Creator<ExchangeRate>() {
        @Override
        public ExchangeRate createFromParcel(Parcel in) {
            return new ExchangeRate(in);
        }

        @Override
        public ExchangeRate[] newArray(int size) {
            return new ExchangeRate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(currency.getValue());
        parcel.writeFloat(value);
    }

    @Override
    public String toString() {
        return currency.getKey() + "->BitCoint: " + getValue();
    }


}
