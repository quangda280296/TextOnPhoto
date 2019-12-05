package jack.com.servicekeep.act;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private static List<Activity> activities = new ArrayList();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
        activity.finish();
    }

    public static Activity getTopActivity() {
        if (activities == null || activities.isEmpty()) {
            return null;
        }
        return activities.get(activities.size() - 1);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static boolean getActivityActive(String nameClass) {
        if (activities != null) {
            return getTopActivity().getClass().getName().equals(nameClass);
        }
        return false;
    }
}