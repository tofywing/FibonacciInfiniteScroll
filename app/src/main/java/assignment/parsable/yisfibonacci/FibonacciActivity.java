package assignment.parsable.yisfibonacci;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Yee on 6/19/17.
 */

public class FibonacciActivity extends AppCompatActivity {
    public static final String APP_TAG = "FibonacciActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(R.string.app_subtitle);
        setSupportActionBar(toolbar);
        createFibonacciFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createFibonacciFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FibonacciFragment mFragment = (FibonacciFragment) manager.findFragmentById(R.id.fragment_container);
        if (mFragment != null) {
            manager.beginTransaction().remove(mFragment).commit();
        }
        mFragment = FibonacciFragment.newInstance();
        manager.beginTransaction().add(R.id.fragment_container, mFragment).commit();
    }
}
