package ru.anna.android_rk1;

import android.app.Application;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ru.anna.android_rk1.listeners.ISubscribeOnTerminate;

public class App extends Application {
    private List<WeakReference<ISubscribeOnTerminate>> subscribeOnTerminate = new ArrayList<>();

    public void subscribeOnTerminate(ISubscribeOnTerminate listener) {
        if (subscribeOnTerminate != null && listener != null)
            subscribeOnTerminate.add(new WeakReference<ISubscribeOnTerminate>(listener));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        for (WeakReference<ISubscribeOnTerminate> listener : subscribeOnTerminate)
            if (listener.get() != null)
                listener.get().onTerminate();

    }

}
