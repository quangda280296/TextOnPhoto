package jack.com.servicekeep.utils;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

public class ThreadUtils {

    public static void runOnMainThread(Action action) {
        Completable.fromAction(action).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
