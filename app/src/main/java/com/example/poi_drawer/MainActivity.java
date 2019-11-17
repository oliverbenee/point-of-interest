package com.example.poi_drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.poi_drawer.ui.Welcome.WelcomeFragment;
import com.example.poi_drawer.ui.map.MapFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;

/**
 * The MainActivity handles various common variables, creates and shows fragments as well as the items to be shown on those fragments.
 * Also handles setting up the database.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @author Blink at https://www.youtube.com/watch?v=b5wzySeFVRk - For guiding the implementation of firebase sign in.
 * @version 2.0
 * @since 17-11-2019
 */
public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    // Sign in with google button on WelcomeFragment.
    private Button signInButton;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private String TAG = "MainActivity";


    // The strings firebase uses
    String name = "Not Signed in.", email, photoUrl;

    // User name and email on drawer.
    private TextView navUsername;
    private TextView navEmail;

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
                R.id.nav_home, R.id.nav_map, R.id.nav_slideshow, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.nav_header_title);
        navUsername.setText("Not Signed in.");
        navEmail = (TextView) headerView.findViewById(R.id.nav_header_email);
        navEmail.setText("");


        /*
         * Handle Sign In with Google Accounts.
         */

        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Addition: Configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /*
     * Allows users to sign in with their google account. Starts the signin intent.
     */
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*
     * Handles response to signing in to the google api.
     * @param requestCode The state of the attempt to contact the server.
     * @param resultCode Used to override superclass method. Carries the result sign in proceedure.
     * @param data Used to override superclass method. Carries sign in intent.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    /*
     * Attempt to sign in to the google api.
     * @param GoogleSignInAccount The account the user is trying to sign in with.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //Sign in success, update UI with the signed-in user's information.
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.nav_host_fragment), "Authentication failed. ", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
    }

    /*
     * If the sign in succeeds, update drawer to show username.
     * @param user - the Google Account of the signed in user.
     */
    private void updateUI(FirebaseUser user){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(acct != null) {
            String personName = acct.getDisplayName();
            String personMail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            navUsername.setText(user.getDisplayName());
            navEmail.setText(user.getEmail());
            Toast.makeText(this, personName + " signed in!", Toast.LENGTH_SHORT).show();
        }
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
