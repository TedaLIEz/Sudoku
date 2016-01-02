package jianguo.ds.se.hust.com.sudoku;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import jianguo.ds.se.hust.com.sudoku.ui.GameFragment;
import jianguo.ds.se.hust.com.sudoku.ui.SettingFragment;
import jianguo.ds.se.hust.com.sudoku.util.LogUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private MenuItem mCurrItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mCurrItem = navigationView.getMenu().findItem(R.id.lv_easy);
        setFragment(mCurrItem,"Easy", null);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setFragment(item, (String) item.getTitle(), mCurrItem);
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void setFragment(MenuItem item, String title, @Nullable MenuItem oldItem) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldItem == item) {
            return;
        }
        if (oldItem != null) {
            ft.remove(getInstance(oldItem));
        }
        ft.replace(R.id.content, getInstance(item), (String) item.getTitle()).commit();
        item.setChecked(true);
        mCurrItem = item;
        setTitle(title);
    }

    @NonNull
    private Fragment getInstance(MenuItem item) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        Fragment fragment = fragmentManager.findFragmentByTag((String) item.getTitle());
        if (fragment == null) {
            switch (item.getItemId()) {
                case R.id.lv_easy:
                    fragment = GameFragment.newInstance("easy");
                    break;
                case R.id.lv_mid:
                    fragment = GameFragment.newInstance("mid");
                    break;
                case R.id.lv_hard:
                    fragment = GameFragment.newInstance("hard");
                    break;
                case R.id.setting:
                    fragment = new SettingFragment();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown item");
            }
        }
        return fragment;
    }
}
