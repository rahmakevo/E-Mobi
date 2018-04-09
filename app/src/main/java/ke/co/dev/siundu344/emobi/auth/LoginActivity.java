package ke.co.dev.siundu344.emobi.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ke.co.dev.siundu344.emobi.R;
import ke.co.dev.siundu344.emobi.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail, mPass;
    private FirebaseAuth mAuth;
    private ProgressDialog mLogDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set ToolBar
        Toolbar mLoginBar = findViewById(R.id.toolBarLog);
        setSupportActionBar(mLoginBar);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLogDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.editTextLoginEmail);
        mPass = findViewById(R.id.editTextLoginPass);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String pass = mPass.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    mLogDialog.setTitle("Authenticating");
                    mLogDialog.setMessage("Please wait as we verify your user details");
                    mLogDialog.setCanceledOnTouchOutside(false);
                    mLogDialog.show();

                    sign_in(email, pass);
                } else{
                    Toast.makeText(LoginActivity.this, "All input fields must be correctly inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sign_in(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent mLogIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mLogIntent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
