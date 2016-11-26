package jp.meridiani.apps.openwithchrome;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class OpenChromeActivity extends AppCompatActivity {

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
            Uri dummyUri = Uri.fromParts(targetUri.getScheme(), "", "");
            Intent dummy = new Intent();
            dummy.setAction(Intent.ACTION_VIEW);
            dummy.addCategory(Intent.CATEGORY_DEFAULT);
            dummy.setType("text/plain");
            dummy.setData(dummyUri);
            List<ResolveInfo> rInfos = pm.queryIntentActivities(dummy, 0);
            for ( ResolveInfo rInfo : rInfos ) {
                Intent target = new Intent();
                target.setAction(Intent.ACTION_VIEW);
                target.setPackage(rInfo.activityInfo.packageName);
                target.addCategory(Intent.CATEGORY_DEFAULT);
                target.setType("text/plain");
                target.setData(targetUri);
                targetIntents.add(target);
            }
            if ( targetIntents.size() < 1 ) {
                Toast.makeText(getApplicationContext(), "no more browser found", Toast.LENGTH_LONG).show();
                return;
            }

            Intent chooser = Intent.createChooser(
                    targetIntents.remove(0),
                    getString(R.string.select_browser)
            );
            if (targetIntents.size() > 0) {
                chooser.putExtra(
                        Intent.EXTRA_INITIAL_INTENTS,
                        targetIntents.toArray(new Parcelable[targetIntents.size()])
                );
            }

            startActivity(chooser);
            return;
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        finally {
            finish();
        }
    }
}
