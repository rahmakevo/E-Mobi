package ke.co.dev.siundu344.emobi.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ke.co.dev.siundu344.emobi.R;
import ke.co.dev.siundu344.emobi.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        // ToolBar
        Toolbar mMainBar = findViewById(R.id.toolBarMain);
        setSupportActionBar(mMainBar);
        getSupportActionBar().setTitle("EMobi");
    }
    // check if user is authenticated
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Intent mStartIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(mStartIntent);
            finish();
        }
    }
    // Get Menu Inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mMainInflater = getMenuInflater();
        mMainInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return false;
        }
    }
    // Log out a User
    private void logout() {
        Intent mLogoutIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(mLogoutIntent);
        finish();
    }
}
