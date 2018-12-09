package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.app.Activity;

import java.util.LinkedHashMap;

public interface ActivityGetter {
    public static LinkedHashMap<String, Activity> activities = new LinkedHashMap<>();

    public static Activity getActivity(String name) {
        if (!ActivityGetter.activities.containsKey(name)) {
            return null;
        }
        return ActivityGetter.activities.get(name);
    }

    public static boolean putActivity(String name, Activity activity) {
        if (!ActivityGetter.activities.containsKey(name)) {
            ActivityGetter.activities.put(name, activity);
            return true;
        }
        return false;
    }

    public static boolean removeActivity(String name) {
        if (ActivityGetter.activities.containsKey(name)) {
            ActivityGetter.activities.remove(name);
            return true;
        }
        return false;
    }
}
