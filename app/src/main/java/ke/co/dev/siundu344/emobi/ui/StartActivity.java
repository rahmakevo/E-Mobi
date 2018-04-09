package ke.co.dev.siundu344.emobi.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ke.co.dev.siundu344.emobi.R;
import ke.co.dev.siundu344.emobi.auth.LoginActivity;
import ke.co.dev.siundu344.emobi.auth.RegisterActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // navigate to register page
        Button btnReg = findViewById(R.id.btnRegStart);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mStartRegIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(mStartRegIntent);
            }
        });

        // navigate to login page
        Button btnLogin = findViewById(R.id.btnLogStart);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mStartLogIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(mStartLogIntent);
            }
        });
    }
}
