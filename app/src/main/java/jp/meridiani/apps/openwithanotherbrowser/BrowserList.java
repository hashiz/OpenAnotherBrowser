package jp.meridiani.apps.openwithanotherbrowser;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kjcs_hashi on 2016/11/26.
 */

public class BrowserList {
    private static BrowserList mInstance = null;
    private SharedPreferences mStore = null;

    protected BrowserList(Context context) {
        mStore = context.getSharedPreferences("BrowserList", Context.MODE_PRIVATE);
    }

    public static synchronized BrowserList getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BrowserList(context);
        }
        return mInstance;
    }

    public Map<String, Boolean> getBrowserList() {
        return (Map<String, Boolean>)mStore.getAll();
    }

    public Map<String, Boolean> getBrowserList(boolean filter) {
        HashMap<String, Boolean> ret = new HashMap<String, Boolean>();
        for (Map.Entry<String, Boolean> entry : ((Map<String, Boolean>)mStore.getAll()).entrySet()) {
            if (entry.getValue() == filter) {
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return ret;
    }

    public void setBrowserEnable(String pkgName, Boolean enable) {
        mStore.edit().putBoolean(pkgName, enable).commit();
    }

    public boolean getBrowserEnable(String pkgName) {
        return mStore.getBoolean(pkgName, false);
    }
}
