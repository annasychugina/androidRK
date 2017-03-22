package ru.anna.android_rk1.models;

public enum Currency {
    UNKNOWN(-1, "UNKNOWN"),
    RUB(0, "RUB"),
    USD(1, "USD");

    int numberOfValue = -1;
    String key = "UNKNOWN";

    Currency(int numberOfValue, String key) {
        this.numberOfValue = numberOfValue;
        this.key = key;
    }

    public int getValue() {
        return numberOfValue;
    }

    public String getKey() {
        return key;
    }

    public static Currency getCurrency(int value) {
        for (Currency currency : Currency.values())
            if (currency.getValue() == value)
                return currency;
        return UNKNOWN;
    }

    public static Currency getCurrency(String key) {
        if (key != null)
            for (Currency currency : Currency.values())
                if (currency.getKey().equals(key))
                    return currency;
        return UNKNOWN;
    }

    public static String[] getKeys() {
        String[] strings = new String[Currency.values().length];
        for (int i = 0; i < strings.length && i < Currency.values().length; i++)
            strings[i] = Currency.values()[i].getKey();
        return strings;
    }
}
