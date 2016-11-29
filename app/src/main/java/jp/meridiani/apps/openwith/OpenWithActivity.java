package jp.meridiani.apps.openwith;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OpenWithActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();
            if (intent == null) {
                return;
            }
            Bundle extra;
            if (!(Intent.ACTION_SEND.equals(intent.getAction()) &&
                    intent.getType().equals("text/plain") &&
                    (extra = intent.getExtras()) != null)) {
                return;
            }
            Uri targetUri = Uri.parse(extra.getString(Intent.EXTRA_TEXT));

            ArrayList<Intent> targetIntents = new ArrayList<Intent>();

            PackageManager pm = getPackageManager();
            Intent query = new Intent();
            query.setAction(Intent.ACTION_VIEW);
            query.addCategory(Intent.CATEGORY_DEFAULT);
            query.setType("text/plain");
            query.setData(targetUri);

            List<ResolveInfo> resolveList = pm.queryIntentActivities(query, PackageManager.MATCH_ALL);
            if (resolveList == null && resolveList.size() < 1) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_more_browser_found), Toast.LENGTH_LONG).show();
                return;
            }
            for (ResolveInfo info : resolveList) {
                Intent target = new Intent(query);
                target.setClassName(info.activityInfo.packageName,info.activityInfo.name);
                targetIntents.add(target);
            }

            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INTENT, new Intent()); // dummy
            chooser.putExtra(Intent.EXTRA_TITLE, getString(R.string.select_browser));
            chooser.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    targetIntents.toArray(new Parcelable[targetIntents.size()])
            );
            startActivity(chooser);
            return;
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }
        finally {
            finish();
        }
    }
}
