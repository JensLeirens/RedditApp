package mafken.redditapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private static DBHelperSubreddits dbHelper;
    RedditPostListener rl;
    String type;
    public int currentReddit;
    private Fragment fragment = new SubredditsFragment();
    public static List<Subreddit> subreddits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("REDDIT_PREFERENCES",MODE_PRIVATE);
        type = sharedPreferences.getString("type", null);

        if (type != null) {
            toolbar.setTitle(type);
            System.out.println("setted type " + type);
        } else {
            displaySelectedScreen(R.id.announcements);
        }

        dbHelper = new DBHelperSubreddits(MainActivity.this);
        if(dbHelper.numberOfRows() <= 1) {
            displaySelectedScreen(R.id.announcements);
        }else{
            subreddits = dbHelper.getSubreddits();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("id", currentReddit);
        bundle.putString("type",type);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public interface RedditPostListener {
        void refreshRedditPosts(String type);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        rl = (RedditPostListener) getSupportFragmentManager().findFragmentById(R.id.content_frame) ;
        rl.refreshRedditPosts(type);
        return super.onOptionsItemSelected(item);
    }


    public void displaySelectedScreen(int itemId) {
        // Handle navigation view item clicks here.
        subreddits = null ;
        type = "";
        if (itemId == R.id.announcements) {
            type = "Announcements";
            toolbar.setTitle(type);
        } else if (itemId == R.id.GoT) {
            type = "gameofthrones";
            toolbar.setTitle("Game Of Thrones");
        } else if (itemId == R.id.funny) {
            type = "Funny";
            toolbar.setTitle(type);
        } else if (itemId == R.id.science) {
            type = "Science";
            toolbar.setTitle(type);
        } else if (itemId == R.id.belgium) {
            type = "Belgium";
            toolbar.setTitle(type);
        }else if (itemId == R.id.gaming) {
            type = "Gaming";
            toolbar.setTitle(type);
        }

        fragment = new SubredditsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("id", currentReddit);
        bundle.putString("type",type);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        drawer.closeDrawer(GravityCompat.START);

    }
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("REDDIT_PREFERENCES",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type", type);
        editor.apply();

        //saving the data to the DB
        new SaveData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private class SaveData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            Log.d("Database","backgroundtask started");

            //clearing any previous rows
            dbHelper.clearDatabase(dbHelper.getWritableDatabase());

            //adding the subreddits to the DB
            for(Subreddit sr : SubredditsFragment.subreddits){
                dbHelper.insertPerson(sr.getName(),sr.getTitle(),sr.getAuthor(),sr.getCreated(),sr.getSelftext(),
                sr.getThumbnail(),sr.getNum_comments(),sr.getUrl(),sr.getUps());
            }
            Log.d("Database","databank saved & aantal rows: " + dbHelper.numberOfRows());

            return null;

        }
    }
}