package hp.sfs.sales.dashboard.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.ui.fragment.DashboardFragment;
import hp.sfs.sales.dashboard.ui.fragment.LoginFragment;
import hp.sfs.sales.dashboard.ui.fragment.OperatorFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;

    private FragmentManager fragmentManager;

    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startAddingFragment();
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().show();
        if(TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(title);
        }
    }

    private void startAddingFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.main_activity_frame_layout, new DashboardFragment(), DashboardFragment.class.getSimpleName());
        fragmentTransaction.replace(R.id.main_activity_frame_layout, new LoginFragment(), LoginFragment.class.getSimpleName());
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}