package com.example.poi_drawer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * The MainActivity handles various common variables, creates and shows fragments as well as the items to be shown on those fragments.
 */

public class MainActivity extends AppCompatActivity {

    // TODO: Implement spinner with categories.
    private AppBarConfiguration mAppBarConfiguration;
    // Fields used to create the database.
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    // Buttons and text fields used in the registration form.
    Button _btnreg;
    EditText _txttitle, _txtimage, _txtcomments, _txtlatitude, _txtlongitude;
    // fields used for drop-down in form. Currently commented out, as they cause errors.
    // Spinner _txtcategory;
    // Spinner spinner_d;
    // String categories[] = {"Events", "Nature", "Shopping", "Cozy Places", "Café"};
    ArrayAdapter<String>arrayAdapter;
    // search view
    // MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Handling of button that is supposed to add a Point of Interest.
         *
         * Only creates a snack-bar at the moment.
         *
         * Also not using the folder-concept.
         */

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is supposed to add a Point of Interest.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });

        /**
         * Create and show navigation drawers.
         * Those are the ones used in the sidebar.
         */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_map, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /**
         * Create and show the elements used in the form, when they appear.
         * TODO: Implement spinner with categories.
         */

        _txttitle = (EditText)findViewById(R.id.txt_title);
        //_txtcategory = (Spinner)findViewById(R.id.spinner_category);
        _txtcomments = (EditText)findViewById(R.id.txt_comments);
        _txtimage = (EditText)findViewById(R.id.txt_image);
        _txtlatitude = (EditText)findViewById(R.id.txt_latitude);
        _txtlongitude = (EditText)findViewById(R.id.txt_longitude);

        /**
         * Search toolbar


        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Material Search");
        toolbar1.setTitleTextColor(Color.parseColor("#FFFFFF"));

        MaterialSearchView searchView = (MaterialSearchView)findViewById(R.id.search_view);
         */
    }

    /**
     * Show the drawer-style menu.
     * @param menu the options menu show.
     * @return true, if the menu is shown.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        /** Show the search-bar menu.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
         */

        return true;
    }

    /**
     * Adds the previous fragment shown to the backstack, to allow users to navigate back to the previous fragment.
     * @return the previous fragment the user viewed.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
    }
}
