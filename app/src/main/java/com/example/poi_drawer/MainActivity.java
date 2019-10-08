package com.example.poi_drawer;

import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.EditText;

/**
 * The MainActivity handles various common variables, creates and shows fragments as well as the items to be shown on those fragments.
 */
public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    // Buttons and text fields used in the registration form. MUST NOT BE PRIVATE!
    EditText _txttitle, _txtimage, _txtcomments, _txtlatitude, _txtlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * Create and show navigation drawers.
         * Those are the ones used in the sidebar.
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_map, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*
         * Create and show the elements used in the form, when they appear.
         */
        _txttitle = findViewById(R.id.txt_title);
        _txtcomments = findViewById(R.id.txt_comments);
        _txtimage = findViewById(R.id.txt_image);
        _txtlatitude = findViewById(R.id.txt_latitude);
        _txtlongitude = findViewById(R.id.txt_longitude);
    }

    /*
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
