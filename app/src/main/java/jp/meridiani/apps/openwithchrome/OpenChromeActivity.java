package jp.meridiani.apps.openwithchrome;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class OpenChromeActivity extends AppCompatActivity {

    private Uri mUri = null;
    private final static String mPkgChrome = "com.android.chrome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_chrome);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        if (!(Intent.ACTION_SEND.equals(intent.getAction()) &&
                intent.getType().equals("text/plain") &&
                intent.getStringExtra(Intent.EXTRA_TEXT) == null)) {
            finish();
            return;
        }
        try {
            mUri = Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
        catch (Exception e) {
            finish();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(mPkgChrome);
            if (intent == null) {
                throw new PackageManager.NameNotFoundException();
            }
            intent.setAction(Intent.ACTION_VIEW);
            for ( String cat : intent.getCategories() ) {
                intent.removeCategory(cat);
            }
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("text/plain");
            intent.setData(mUri);
            startActivity(intent);
            finish();
            return;
        }
        catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(),mPkgChrome+" not found.", Toast.LENGTH_LONG);
            finish();
            return;
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"can't start "+mPkgChrome, Toast.LENGTH_LONG);
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
