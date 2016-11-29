package jp.meridiani.apps.openwith;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(),getString(R.string.this_app_found_in_appshare), Toast.LENGTH_LONG).show();
        finish();
    }
}
